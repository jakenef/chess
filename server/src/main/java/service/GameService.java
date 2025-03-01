package service;

import dataaccess.DataAccessException;
import dataaccess.auth.AuthDataAccess;
import dataaccess.game.GameDataAccess;
import dataaccess.user.UserDataAccess;
import model.request.CreateGameRequest;
import model.result.CreateGameResult;
import model.GameData;
import model.request.ListGameRequest;
import model.result.ListGameResult;

public class GameService extends BaseService {

    public GameService(GameDataAccess gameDataAccess, UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        super(gameDataAccess, userDataAccess, authDataAccess);
    }

    public ListGameResult listGames(ListGameRequest req) throws DataAccessException {
        if(authDataAccess.isAuthorized(req.authToken())){
            return new ListGameResult(gameDataAccess.getAllGames());
        } else {
            throw new DataAccessException("unauthorized");
        }
    }

    public CreateGameResult createGame(CreateGameRequest req) throws DataAccessException{
        if(authDataAccess.isAuthorized(req.authToken())){
            GameData newGame = gameDataAccess.createGame(req.gameName());
            return new CreateGameResult(newGame.gameID());
        } else {
            throw new DataAccessException("unauthorized");
        }
    }
}
