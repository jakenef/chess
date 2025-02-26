package dataaccess;

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
