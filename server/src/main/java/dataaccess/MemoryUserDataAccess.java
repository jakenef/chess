package dataaccess;

import model.UserData;

public class MemoryUserDataAccess extends MemoryDataAccess<String, UserData> implements UserDataAccess {

    public void addUser(UserData user) throws DataAccessException {
        if(user == null || user.username().isEmpty()){
            throw new DataAccessException("username null");
        }
        if (dataMap.containsKey(user.username())) {
            throw new DataAccessException("username already taken");
        }
        dataMap.put(user.username(), user);
    }

    public UserData getUser(String username, String password) throws DataAccessException {
        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new DataAccessException("username or password null");
        }
        if(dataMap.containsKey(username)
                && dataMap.get(username).password().equals(password)){
            return dataMap.get(username);
        } else {
            throw new DataAccessException("unauthorized");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if(username == null || username.isEmpty()) {
            throw new DataAccessException("username null");
        }
        if(dataMap.containsKey(username)){
            return dataMap.get(username);
        } else {
            throw new DataAccessException("dataMap does not contain given username key");
        }
    }


}