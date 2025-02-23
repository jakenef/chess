package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.ClearRequest;
import model.ClearResult;

public class ClearService {
    private final GameDataAccess gameDataAccess;
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public ClearService(GameDataAccess gameDataAccess, UserDataAccess userDataAccess, AuthDataAccess authDataAccess){
        this.gameDataAccess = gameDataAccess;
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public ClearResult clearAll(ClearRequest req) throws DataAccessException {
        gameDataAccess.deleteAllGameData();
        userDataAccess.deleteAllUserData();
        authDataAccess.deleteAllAuthData();
        return new ClearResult(true);
    }
}
