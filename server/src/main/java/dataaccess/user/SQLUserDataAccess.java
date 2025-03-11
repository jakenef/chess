package dataaccess.user;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDataAccess implements UserDataAccess{
    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "DELETE FROM user";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (user == null || user.username().isEmpty() || user.password() == null || user.password().isEmpty()) {
            throw new DataAccessException("null username");
        }

        boolean isDuplicate = false;
        try {
            getUser(user.username());
            isDuplicate = true;
        } catch (DataAccessException ignored){}

        if(isDuplicate){
            throw new DataAccessException("already taken");
        }

        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        DatabaseManager.executeUpdate(statement, user.username(), hashedPassword, user.email());
    }

    private boolean dbContainsUser(String username) {
        try{
            var conn = DatabaseManager.getConnection();
            var statement = "SELECT 1 FROM user WHERE username = ? LIMIT 1";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, username);
            var rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException | DataAccessException e){
            return false;
        }
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        UserData user = getUser(username);
        if (BCrypt.checkpw(password, user.password())){
            return user;
        } else {
            throw new DataAccessException("unauthorized");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (username == null || username.isEmpty()) {
            throw new DataAccessException("username null");
        }
        if (dbContainsUser(username)) {
            var conn = DatabaseManager.getConnection();
            var statement = "SELECT username, password, email FROM user WHERE username = ? LIMIT 1";
            try {
                var ps = conn.prepareStatement(statement);
                ps.setString(1, username);
                var rs = ps.executeQuery();
                if (rs.next()){
                    return readUserFromRS(rs);
                } else {
                    throw new DataAccessException("unauthorized");
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } else {
            throw new DataAccessException("unauthorized");
        }
    }

    @Override
    public boolean isEmpty() {
        String statement = "SELECT COUNT(*) FROM user";
        return isDatabaseEmpty(statement);
    }

    public static boolean isDatabaseEmpty(String statement) {
        try {
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (DataAccessException | SQLException e) {
            return true;
        }
        return true;
    }

    private static UserData readUserFromRS(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }
}
