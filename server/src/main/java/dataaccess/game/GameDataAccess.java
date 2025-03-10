package dataaccess.game;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.ArrayList;

public interface GameDataAccess {
    void deleteAll();
    GameData createGame(String gameName) throws DataAccessException;
    ArrayList<GameData> getAllGames() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void joinGame(int gameID, String teamColor, String username) throws DataAccessException;
    boolean isEmpty();
}
