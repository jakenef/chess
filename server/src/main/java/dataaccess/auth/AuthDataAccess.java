package dataaccess.auth;

import dataaccess.DataAccessException;
import model.AuthData;

public interface AuthDataAccess {
    void deleteAll() throws DataAccessException;
    AuthData createAuth(String username) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    boolean isAuthorized(String authToken) throws DataAccessException;
}
