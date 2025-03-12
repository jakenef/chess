package dataaccess.auth;

import dataaccess.DataAccessException;
import model.AuthData;
import dataaccess.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static dataaccess.user.SQLUserDataAccess.isDatabaseEmpty;

public class SQLAuthDataAccess implements AuthDataAccess {
    @Override
    public void deleteAll() throws DataAccessException{
        var statement = "DELETE FROM auth";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null || username.isEmpty()) {
            throw new DataAccessException("null username");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, username, authToken);
        return newAuth;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if(authToken == null || authToken.isEmpty()){
            throw new DataAccessException("authToken null");
        }
        if (isAuthorized(authToken)){
            var statement = "DELETE FROM auth WHERE authToken = ?";
            DatabaseManager.executeUpdate(statement, authToken);
        } else {
            throw new DataAccessException("unauthorized");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if(authToken == null || authToken.isEmpty()){
            throw new DataAccessException("authToken null");
        }

        if (isAuthorized(authToken)){
            var conn = DatabaseManager.getConnection();
            var statement = "SELECT authToken, username FROM auth WHERE authToken = ? LIMIT 1";
            try {
                var ps = conn.prepareStatement(statement);
                ps.setString(1, authToken);
                var rs = ps.executeQuery();
                if (rs.next()){
                    return readAuthFromRS(rs);
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
    public boolean isAuthorized(String authToken) throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        var statement = "SELECT 1 FROM auth WHERE authToken = ? LIMIT 1";

        try{
            var ps = conn.prepareStatement(statement);
            ps.setString(1, authToken);
            var rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e){
            throw new DataAccessException("Database error: " + e.getMessage());
        }
    }

    @Override
    public boolean isEmpty() {
        String statement = "SELECT COUNT(*) FROM auth";
        return isDatabaseEmpty(statement);
    }

    private static AuthData readAuthFromRS(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String authToken = rs.getString("authToken");

        return new AuthData(authToken, username);
    }
}
