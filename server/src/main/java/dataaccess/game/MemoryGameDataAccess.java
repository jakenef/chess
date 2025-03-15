package dataaccess.game;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.GameData;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;

public class MemoryGameDataAccess extends MemoryDataAccess<Integer, GameData> implements GameDataAccess {
    private static final AtomicInteger GAME_ID_COUNTER = new AtomicInteger(1);

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        if (gameName == null || gameName.isEmpty()){
            throw new DataAccessException("bad request");
        }
        GameData newGame = new GameData(GAME_ID_COUNTER.getAndIncrement(),
                null, null, gameName, new ChessGame());
        dataMap.put(newGame.gameID(), newGame);
        return newGame;
    }

    @Override
    public ArrayList<GameData> getAllGames() {
        return new ArrayList<>(dataMap.values());
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if (gameID > 0 && dataMap.containsKey(gameID)){
            return dataMap.get(gameID);
        }
        throw new DataAccessException("bad request");
    }

    @Override
    public void joinGame(int gameID, String teamColor, String username) throws DataAccessException {
        if (teamColor == null || teamColor.isEmpty() || username == null || username.isEmpty()
                || !dataMap.containsKey(gameID) || (!teamColor.equals("WHITE") && !teamColor.equals("BLACK"))){
            throw new DataAccessException("bad request");
        }
        GameData game = dataMap.get(gameID);
        GameData joinedGame;
        if(isTeamAvailable(teamColor, game)){
            if(teamColor.equals("WHITE")){
                joinedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            } else {
                joinedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            }
            dataMap.remove(gameID);
            dataMap.put(gameID, joinedGame);
        } else {
            throw new DataAccessException("already taken");
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        return;
    }

    public static boolean isTeamAvailable(String teamColor, GameData game){
        if(teamColor.equals("WHITE")){
            return game.whiteUsername() == null;
        } else {
            return game.blackUsername() == null;
        }
    }
}
