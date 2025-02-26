package service;

import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
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

    protected boolean isAuthorized(String authToken) throws DataAccessException {
        //return authDataAccess.getAuth(authToken) != null;
        return false;
    }

    protected String toJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
