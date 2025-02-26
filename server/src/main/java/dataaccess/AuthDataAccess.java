package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
    void deleteAll();
    AuthData createAuth(String username) throws DataAccessException;
}
