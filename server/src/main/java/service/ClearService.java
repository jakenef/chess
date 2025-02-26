package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.ClearRequest;
import model.ClearResult;

public class ClearService extends BaseService {

    public ClearService(GameDataAccess gameDataAccess, UserDataAccess userDataAccess, AuthDataAccess authDataAccess){
        super(gameDataAccess, userDataAccess, authDataAccess);
    }

    public ClearResult clearAll(ClearRequest req) throws DataAccessException {
        gameDataAccess.deleteAll();
        userDataAccess.deleteAll();
        authDataAccess.deleteAll();
        return new ClearResult(true);
    }
}
