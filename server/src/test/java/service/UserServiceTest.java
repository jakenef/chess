package service;

import dataaccess.*;
import dataaccess.auth.AuthDataAccess;
import dataaccess.game.GameDataAccess;
import dataaccess.user.UserDataAccess;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;
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
    void registerPositive() {
        try {
            RegisterRequest req = new RegisterRequest("testName", "testPassword", "test@t.com");
            RegisterResult res = userService.register(req);
            assertEquals("testName", res.username());
            assertFalse(res.authToken().isEmpty());
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void registerNegative() {
        try {
            RegisterRequest req = new RegisterRequest("testName", "testPassword", "test@t.com");
            userService.register(req);
            RegisterRequest req2 = new RegisterRequest("testName", "testPassword", "test@t.com");
            assertThrows(DataAccessException.class, ()-> userService.register(req2));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void loginPositive() {
        try {
            RegisterRequest regReq = new RegisterRequest("testName", "testPassword", "test@t.com");
            userService.register(regReq);
            assertNotNull(userDA.getUser("testName"));
            LoginRequest logReq = new LoginRequest("testName", "testPassword");
            LoginResult res = userService.login(logReq);
            assertEquals("testName", res.username());
            assertFalse(res.authToken().isEmpty());
            assertNotNull(authDA.getAuth(res.authToken()));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void loginNegative() {
        LoginRequest logReq = new LoginRequest("testName", "testPassword");
        assertThrows(DataAccessException.class, ()-> userService.login(logReq));
    }

    @Test
    void logoutPositive() {
        try {
            RegisterRequest regReq = new RegisterRequest("testName", "testPassword", "test@t.com");
            userService.register(regReq);
            LoginRequest logReq = new LoginRequest("testName", "testPassword");
            LoginResult res = userService.login(logReq);

            LogoutRequest outReq = new LogoutRequest(res.authToken());
            userService.logout(outReq);

            assertThrows(DataAccessException.class, () -> authDA.getAuth(res.authToken()));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void logoutNegative(){
        LogoutRequest outReq = new LogoutRequest("10-98");
        try {
            RegisterRequest regReq = new RegisterRequest("testName", "testPassword", "test@t.com");
            userService.register(regReq);
            LoginRequest logReq = new LoginRequest("testName", "testPassword");
            userService.login(logReq);
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
        assertThrows(DataAccessException.class, () -> userService.logout(outReq));
    }
}