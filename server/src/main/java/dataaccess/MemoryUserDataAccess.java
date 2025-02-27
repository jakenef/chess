package dataaccess;

import model.UserData;

public class MemoryUserDataAccess extends MemoryDataAccess<String, UserData> implements UserDataAccess {

    public void addUser(UserData user) throws DataAccessException {
        if (dataMap.containsKey(user.username())) {
            throw new DataAccessException("username already taken");
        }
        dataMap.put(user.username(), user);
    }
}