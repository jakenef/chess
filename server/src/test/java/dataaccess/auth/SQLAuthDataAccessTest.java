package dataaccess.auth;

import dataaccess.DataAccessException;
import dataaccess.DataAccessFactory;
import dataaccess.DatabaseManager;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SQLAuthDataAccessTest {
    static AuthDataAccess authDA = DataAccessFactory.createAuthDataAccess();

    @BeforeEach
    void setUp() {
        authDA = DataAccessFactory.createAuthDataAccess();
        try {
            DatabaseManager.configureDatabase();
            DatabaseManager.clearAllTables();
        } catch (DataAccessException e){
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    void deleteAll() {
        AuthData authRes = null;
        try {
            authRes = authDA.createAuth("testUser");
        } catch (DataAccessException e) {
            fail("setup failed: " + e.getMessage());
        }
        try {
            assertEquals("testUser", authDA.getAuth(authRes.authToken()).username());
            authDA.deleteAll();
            assertFalse(authDA.isAuthorized(authRes.authToken()));
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
        // get created, clear, get empty, assert empty
    }

    @Test
    void createAuthPos() {
        try{
            AuthData result = authDA.createAuth("testUser");
            AuthData dbRes = authDA.getAuth(result.authToken());
            assertEquals(result, dbRes);
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void createAuthNeg(){
        assertThrows(DataAccessException.class, () -> authDA.createAuth(null));
    }

    @Test
    void deleteAuth() {
    }

    @Test
    void getAuthPos() {
        AuthData authRes = null;
        try {
            authRes = authDA.createAuth("testUser");
        } catch (DataAccessException e) {
            fail("setup failed: " + e.getMessage());
        }
        try {
            assertEquals(authRes, authDA.getAuth(authRes.authToken()));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getAuthNeg() {
        assertThrows(DataAccessException.class, () -> authDA.getAuth("badToken"));

    }
}