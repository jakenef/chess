package dataaccess.auth;

import dataaccess.DataAccessException;
import dataaccess.DataAccessFactory;
import dataaccess.DatabaseManager;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static dataaccess.auth.SQLAuthDataAccess.readAuth;
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
        try{
            AuthData result = authDA.createAuth("testUser");
            AuthData dbRes = null;
            var conn = DatabaseManager.getConnection();
            var statement = "SELECT authToken, username FROM auth WHERE username = ?";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, "testUser");
            var rs = ps.executeQuery();
            if(rs.next()){
                dbRes = readAuth(rs);
            }
            assertEquals(result, dbRes);
        } catch (DataAccessException e){
            fail(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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