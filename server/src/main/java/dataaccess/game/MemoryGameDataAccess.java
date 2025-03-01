package dataaccess.game;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.GameData;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;

public class MemoryGameDataAccess extends MemoryDataAccess<Integer, GameData> implements GameDataAccess {
    private static final AtomicInteger gameIdCounter = new AtomicInteger(1);

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        if (gameName == null || gameName.isEmpty()){
            throw new DataAccessException("gameName is null");
        }
        GameData newGame = new GameData(gameIdCounter.getAndIncrement(),
                "", "", gameName, new ChessGame());
        dataMap.put(newGame.gameID(), newGame);
        return newGame;
    }

    @Override
    public ArrayList<GameData> getAllGames() {
        return new ArrayList<>(dataMap.values());
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        if (gameID != null){
            return dataMap.get(gameID);
        }
        throw new DataAccessException("gameID null");
    }


}
