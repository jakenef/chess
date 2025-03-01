package service;

import dataaccess.auth.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.game.GameDataAccess;
import dataaccess.user.UserDataAccess;
import model.request.ClearRequest;
import model.result.ClearResult;

public class ClearService extends BaseService {

    public ClearService(GameDataAccess gameDataAccess, UserDataAccess userDataAccess, AuthDataAccess authDataAccess){
        super(gameDataAccess, userDataAccess, authDataAccess);
    }

    public ClearResult clearAll(ClearRequest req) throws DataAccessException {
        gameDataAccess.deleteAll();
        userDataAccess.deleteAll();
        authDataAccess.deleteAll();
        return new ClearResult();
    }
}
