package dataaccess;

import dataaccess.auth.AuthDataAccess;
import dataaccess.auth.MemoryAuthDataAccess;
import dataaccess.auth.SQLAuthDataAccess;
import dataaccess.game.GameDataAccess;
import dataaccess.game.MemoryGameDataAccess;
import dataaccess.game.SQLGameDataAccess;
import dataaccess.user.MemoryUserDataAccess;
import dataaccess.user.SQLUserDataAccess;
import dataaccess.user.UserDataAccess;

// one place to change implementation from memory to SQL
public class DataAccessFactory {

    public static GameDataAccess createGameDataAccess() {
        return new SQLGameDataAccess();
    }

    public static UserDataAccess createUserDataAccess() {
        return new SQLUserDataAccess();
    }

    public static AuthDataAccess createAuthDataAccess() {
        return new SQLAuthDataAccess();
    }
}
