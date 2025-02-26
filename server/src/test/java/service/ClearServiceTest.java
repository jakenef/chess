package service;

import dataaccess.*;
import model.ClearRequest;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataaccess.DataAccessFactory;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    static GameDataAccess gameDA = DataAccessFactory.createGameDataAccess();
    static UserDataAccess userDA = DataAccessFactory.createUserDataAccess();
    static AuthDataAccess authDA = DataAccessFactory.createAuthDataAccess();
    static ClearService clearService = new ClearService(gameDA, userDA, authDA);

    @BeforeEach
    void setUp() {
        gameDA = DataAccessFactory.createGameDataAccess();
        userDA = DataAccessFactory.createUserDataAccess();
        authDA = DataAccessFactory.createAuthDataAccess();
        clearService = new ClearService(gameDA, userDA, authDA);

        try{
            userDA.addUser(new UserData("testUser1", "password1", "test@test.com"));
            userDA.addUser(new UserData("testUser2", "password456", "test2@example.com"));
            authDA.createAuth("testUser1");
        } catch (DataAccessException e){
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    void clearAll() throws DataAccessException {
        assertFalse(userDA.toString().isEmpty());
        //assertFalse(gameDA.toString().isEmpty());
        assertFalse(authDA.toString().isEmpty());

        clearService.clearAll(new ClearRequest());

        assertTrue(userDA.toString().isEmpty());
        assertTrue(gameDA.toString().isEmpty());
        assertTrue(authDA.toString().isEmpty());
    }
}