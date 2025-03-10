package dataaccess.user;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class SQLUserDataAccess implements UserDataAccess{
    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "DELETE FROM user";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (user == null || user.username().isEmpty()) {
            throw new DataAccessException("null username");
        }
        //get userData from db, if not there, then run this (duplicate username)
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(user);
        DatabaseManager.executeUpdate(statement, user.username(), user.password(), user.email(), json);
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
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
}
