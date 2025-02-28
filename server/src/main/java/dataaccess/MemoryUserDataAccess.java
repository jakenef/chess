package dataaccess;

import model.UserData;

public class MemoryUserDataAccess extends MemoryDataAccess<String, UserData> implements UserDataAccess {

    public void addUser(UserData user) throws DataAccessException {
        if (dataMap.containsKey(user.username())) {
            throw new DataAccessException("username already taken");
        }
        dataMap.put(user.username(), user);
    }

    public boolean isUser(String username, String password) {
        return dataMap.containsKey(username)
                && dataMap.get(username).password().equals(password);
    }
}