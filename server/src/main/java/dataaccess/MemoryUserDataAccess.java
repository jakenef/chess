package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDataAccess implements UserDataAccess {
    private final HashMap<String, UserData> userDataMap = new HashMap<>();

    public void deleteAllUserData(){
        userDataMap.clear();
    }
}
