package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.*;

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

    public LoginResult login(LoginRequest req) throws DataAccessException {
        boolean isUser = userDataAccess.isUser(req.username(), req.password());
        if(isUser){
            AuthData auth = authDataAccess.createAuth(req.username());
            return new LoginResult(req.username(), auth.authToken());
        }
        throw new DataAccessException("unauthorized");
    }
}
