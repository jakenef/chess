package dataaccess.game;

import dataaccess.DataAccessException;
import dataaccess.DataAccessFactory;
import dataaccess.DatabaseManager;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDataAccessTest {
    private GameDataAccess gameDA = DataAccessFactory.createGameDataAccess();

    @BeforeEach
    void setUp() {
        gameDA = DataAccessFactory.createGameDataAccess();
        try {
            DatabaseManager.configureDatabase();
            DatabaseManager.clearAllTables();
        } catch (DataAccessException e){
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    void deleteAll() {
        GameData newGame = null;
        try {
            newGame = gameDA.createGame("testGame");
        } catch (DataAccessException e) {
            fail("setup failed: " + e.getMessage());
        }
        try {
            assertFalse(gameDA.isEmpty());
            gameDA.deleteAll();
            assertTrue(gameDA.isEmpty());
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    void createGamePos() {
        try {
            GameData newGame = gameDA.createGame("testGame");
            assertEquals(newGame, gameDA.getGame(newGame.gameID()));
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    void createGameNeg(){
        assertThrows(DataAccessException.class, () -> gameDA.createGame(null));
    }

    @Test
    void getAllGamesPos() {
        try {
            GameData newGame = gameDA.createGame("testGame");
            GameData newGame2 = gameDA.createGame("testGame2");

            ArrayList<GameData> expectedList = new ArrayList<>();
            expectedList.add(newGame);
            expectedList.add(newGame2);
            ArrayList<GameData> actualList = gameDA.getAllGames();

            assertTrue(expectedList.containsAll(actualList) && actualList.containsAll(expectedList));
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    void getGamePos() {
        try {
            GameData newGameGet = gameDA.createGame("testGame");
            assertTrue(newGameGet.equals(gameDA.getGame(newGameGet.gameID())));
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    void getGameNeg(){
        assertThrows(DataAccessException.class, () -> gameDA.getGame(22));
    }

    @Test
    void joinGamePos() {
        try {
            GameData newGame = gameDA.createGame("testGame");
            gameDA.joinGame(newGame.gameID(), "WHITE", "testUser");
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    void joinGameNeg(){
        try {
            GameData newGame = gameDA.createGame("testGame");
            gameDA.joinGame(newGame.gameID(), "WHITE", "testUser");

            assertThrows(DataAccessException.class,
                    () -> gameDA.joinGame(newGame.gameID(), "WHITE", "testUser"));
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    void isEmptyPos() {
        assertTrue(gameDA.isEmpty());
    }

    @Test
    void isEmptyNeg(){
        try {
            GameData newGame = gameDA.createGame("testGame");
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
        assertFalse(gameDA.isEmpty());
    }
}