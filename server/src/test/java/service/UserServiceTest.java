package service;

import dataaccess.*;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    static GameDataAccess gameDA = DataAccessFactory.createGameDataAccess();
    static UserDataAccess userDA = DataAccessFactory.createUserDataAccess();
    static AuthDataAccess authDA = DataAccessFactory.createAuthDataAccess();
    static UserService userService = new UserService(gameDA, userDA, authDA);

    @BeforeEach
    void setUp() {
        gameDA = DataAccessFactory.createGameDataAccess();
        userDA = DataAccessFactory.createUserDataAccess();
        authDA = DataAccessFactory.createAuthDataAccess();
        userService = new UserService(gameDA, userDA, authDA);
    }

    @Test
    void registerPositive() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("testName", "testPassword", "test@t.com");
        RegisterResult res = userService.register(req);
        assertEquals("testName", res.username());
        assertFalse(res.authToken().isEmpty());
    }

    @Test
    void registerNegative() throws DataAccessException{
        RegisterRequest req = new RegisterRequest("testName", "testPassword", "test@t.com");
        userService.register(req);
        RegisterRequest req2 = new RegisterRequest("testName", "testPassword", "test@t.com");
        assertThrows(DataAccessException.class, ()-> userService.register(req2));
    }
}