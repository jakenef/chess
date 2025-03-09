package dataaccess.auth;

import dataaccess.DataAccessException;
import dataaccess.DataAccessFactory;
import dataaccess.DatabaseManager;
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
    }

    @Test
    void createAuth() {
    }

    @Test
    void deleteAuth() {
    }

    @Test
    void getAuth() {
    }

    @Test
    void isAuthorized() {
    }
}