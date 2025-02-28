package dataaccess;

import model.UserData;

public interface UserDataAccess {
    void deleteAll();
    void addUser(UserData user) throws DataAccessException;
    UserData getUser(String username, String password) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
}
