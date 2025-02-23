package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDataAccess implements GameDataAccess{
    private final HashMap<String, GameData> gameDataMap = new HashMap<>();

    public void deleteAllGameData(){
        gameDataMap.clear();
    }
}
