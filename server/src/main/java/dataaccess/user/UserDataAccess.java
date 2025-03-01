package dataaccess.user;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDataAccess {
    void deleteAll();
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username, String password) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
}
