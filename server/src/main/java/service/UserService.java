package service;

import dataaccess.auth.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.game.GameDataAccess;
import dataaccess.user.UserDataAccess;
import model.AuthData;
import model.UserData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.LogoutResult;
import model.result.RegisterResult;

public class UserService extends BaseService {

    public UserService(GameDataAccess gameDataAccess, UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        super(gameDataAccess, userDataAccess, authDataAccess);
    }

    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        UserData newUser = new UserData(req.username(), req.password(), req.email());
        userDataAccess.createUser(newUser);
        AuthData auth = authDataAccess.createAuth(req.username());
        return new RegisterResult(newUser.username(), auth.authToken());
    }

    public LoginResult login(LoginRequest req) throws DataAccessException {
        userDataAccess.getUser(req.username(), req.password());
        AuthData auth = authDataAccess.createAuth(req.username());
        return new LoginResult(req.username(), auth.authToken());
    }

    public LogoutResult logout(LogoutRequest req) throws DataAccessException {
        authDataAccess.deleteAuth(req.authToken());
        return new LogoutResult();
    }
}
