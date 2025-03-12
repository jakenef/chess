package dataaccess.user;

import dataaccess.DataAccessException;
import dataaccess.DataAccessFactory;
import dataaccess.DatabaseManager;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDataAccessTest {
    private UserDataAccess userDA = DataAccessFactory.createUserDataAccess();

    @BeforeEach
    void setUp() {
        userDA = DataAccessFactory.createUserDataAccess();
        try {
            DatabaseManager.configureDatabase();
            DatabaseManager.clearAllTables();
        } catch (DataAccessException e){
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    void deleteAll() {
        UserData testUser = null;
        try {
            testUser = new UserData("testUser", "password", "test@t.com");
            userDA.createUser(testUser);
        } catch (DataAccessException e) {
            fail("setup failed: " + e.getMessage());
        }
        try {
            assertEquals("testUser", userDA.getUser(testUser.username()).username());
            userDA.deleteAll();
            UserData finalTestUser = testUser;
            assertThrows(DataAccessException.class, () -> userDA.getUser(finalTestUser.username()));
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    void createUserPos() {
        try{
            UserData testUser = new UserData("testUser", "password", "test@t.com");
            userDA.createUser(testUser);
            UserData dbRes = userDA.getUser(testUser.username());
            assertEquals(testUser.username(), dbRes.username());
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void createUserNeg(){
        assertThrows(DataAccessException.class, () -> userDA.createUser(null));
    }

    @Test
    void getUserPos() {
        UserData testUser = new UserData("testUser", "password", "test@t.com");
        try {
            userDA.createUser(testUser);
        } catch (DataAccessException e) {
            fail("setup failed: " + e.getMessage());
        }
        try {
            assertEquals(testUser.username(), userDA.getUser(testUser.username()).username());
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getUserNeg(){
        assertThrows(DataAccessException.class, () -> userDA.getUser("badUsername"));
    }

    @Test
    void getUserWithPasswordPos() {
        UserData testUser = new UserData("testUser", "password", "test@t.com");
        try {
            userDA.createUser(testUser);
        } catch (DataAccessException e) {
            fail("setup failed: " + e.getMessage());
        }
        try {
            assertEquals(testUser.username(), userDA.getUser(testUser.username(), testUser.password()).username());
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getUserWithPasswordNeg() {
        UserData testUser = new UserData("testUser", "password", "test@t.com");
        try {
            userDA.createUser(testUser);
        } catch (DataAccessException e) {
            fail("setup failed: " + e.getMessage());
        }
        assertThrows(DataAccessException.class, () -> userDA.getUser(testUser.username(), "wrongPassword"));
    }

    @Test
    void getUserWithPasswordNegDNE(){
        assertThrows(DataAccessException.class, () -> userDA.getUser("badUsername", "badPassword"));
    }

    @Test
    void isEmptyPos() {
        assertTrue(userDA.isEmpty());
    }

    @Test
    void isEmptyNeg(){
        try{
            UserData testUser = new UserData("testUser", "password", "test@t.com");
            userDA.createUser(testUser);
            assertEquals(testUser.username(), userDA.getUser(testUser.username()).username());
        } catch (DataAccessException e){
            fail("setup failed: " + e.getMessage());
        }
        assertFalse(userDA.isEmpty());
    }
}