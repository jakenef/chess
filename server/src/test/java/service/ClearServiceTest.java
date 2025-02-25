package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import model.ClearRequest;
import org.junit.jupiter.api.Test;
import spark.Request;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    static MemoryGameDataAccess mGameDA = new MemoryGameDataAccess();
    static MemoryUserDataAccess mUserDA = new MemoryUserDataAccess();
    static MemoryAuthDataAccess mAuthDA = new MemoryAuthDataAccess();

    static final ClearService clearService = new ClearService(mGameDA, mUserDA, mAuthDA);

    @Test
    void clearAll() throws DataAccessException {
        //set up some data once we have framework
        ClearRequest req = new ClearRequest();
        clearService.clearAll(req);
        //assert true size of data = 0, once we have a framework put together.
        assertTrue(false);
    }
}