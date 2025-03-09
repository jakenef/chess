package dataaccess.auth;

import dataaccess.DataAccessException;
import model.AuthData;
import dataaccess.DatabaseManager;

public class SQLAuthDataAccess implements AuthDataAccess {
    @Override
    public void deleteAll() throws DataAccessException{
        var statement = "DELETE FROM AuthData";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return null;
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
