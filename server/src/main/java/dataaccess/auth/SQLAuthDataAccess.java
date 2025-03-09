package dataaccess.auth;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import dataaccess.DatabaseManager;

import java.util.UUID;

public class SQLAuthDataAccess implements AuthDataAccess {
    @Override
    public void deleteAll() throws DataAccessException{
        var statement = "DELETE FROM AuthData";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null || username.isEmpty()) {
            throw new DataAccessException("null username");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        var statement = "INSERT INTO auth (username, authToken, json) VALUES (?, ?, ?)";
        var json = new Gson().toJson(newAuth);
        var id = DatabaseManager.executeUpdate(statement, username, authToken, json);
        return newAuth;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public boolean isAuthorized(String authToken) throws DataAccessException {
        return false;
    }
}
