package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDataAccess implements AuthDataAccess{
    private final HashMap<String, AuthData> authDataMap = new HashMap<>();

    public void deleteAllAuthData(){
        authDataMap.clear();
    }
}
