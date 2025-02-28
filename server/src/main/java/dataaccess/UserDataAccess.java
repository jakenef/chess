package dataaccess;

import model.UserData;

public interface UserDataAccess {
    void deleteAll();
    void addUser(UserData user) throws DataAccessException;
    boolean isUser(String username, String password);
}
