package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

public class UserService extends BaseService {

    public UserService(GameDataAccess gameDataAccess, UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        super(gameDataAccess, userDataAccess, authDataAccess);
    }

    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        UserData newUser = new UserData(req.username(), req.password(), req.email());
        userDataAccess.addUser(newUser);
        AuthData auth = authDataAccess.createAuth(req.username());
        return new RegisterResult(newUser.username(), auth.authToken());
    }
}
