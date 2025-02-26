package dataaccess;

import model.AuthData;

import java.util.UUID;

public class MemoryAuthDataAccess extends MemoryDataAccess<String, AuthData> implements AuthDataAccess{

    public AuthData createAuth(String username) throws DataAccessException{
        if (username == null){
            throw new DataAccessException("username null");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        dataMap.put(authToken, newAuth);
        return newAuth;
    }
}
