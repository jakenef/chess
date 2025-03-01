package dataaccess;

import dataaccess.auth.AuthDataAccess;
import dataaccess.auth.MemoryAuthDataAccess;
import dataaccess.game.GameDataAccess;
import dataaccess.game.MemoryGameDataAccess;
import dataaccess.user.MemoryUserDataAccess;
import dataaccess.user.UserDataAccess;

// one place to change implementation from memory to SQL
public class DataAccessFactory {

    public static GameDataAccess createGameDataAccess() {
        return new MemoryGameDataAccess();
    }

    public static UserDataAccess createUserDataAccess() {
        return new MemoryUserDataAccess();
    }

    public static AuthDataAccess createAuthDataAccess() {
        return new MemoryAuthDataAccess();
    }
}
