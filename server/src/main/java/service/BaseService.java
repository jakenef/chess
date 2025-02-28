package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;

public abstract class BaseService {
    protected final GameDataAccess gameDataAccess;
    protected final UserDataAccess userDataAccess;
    protected final AuthDataAccess authDataAccess;

    public BaseService(GameDataAccess gameDataAccess, UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }
}
