package dataaccess.auth;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;

import java.util.UUID;

public class MemoryAuthDataAccess extends MemoryDataAccess<String, AuthData> implements AuthDataAccess {

    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null || username.isEmpty()) {
            throw new DataAccessException("null username");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        dataMap.put(authToken, newAuth);
        return newAuth;
    }

    public boolean isAuthorized(String authToken){
        return dataMap.containsKey(authToken);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        if(authToken == null || authToken.isEmpty()){
            throw new DataAccessException("authToken null");
        }
        if(isAuthorized(authToken)){
            return dataMap.get(authToken);
        } else {
            throw new DataAccessException("unauthorized");
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        if(authToken == null || authToken.isEmpty()){
            throw new DataAccessException("authToken null");
        }
        if(isAuthorized(authToken)){
            dataMap.remove(authToken);
        } else {
            throw new DataAccessException("unauthorized");
        }
    }
}
