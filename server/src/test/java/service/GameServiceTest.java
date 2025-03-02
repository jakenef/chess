package service;

import dataaccess.DataAccessException;
import dataaccess.DataAccessFactory;
import dataaccess.auth.AuthDataAccess;
import dataaccess.game.GameDataAccess;
import dataaccess.user.UserDataAccess;
import model.request.JoinGameRequest;
import model.request.CreateGameRequest;
import model.result.CreateGameResult;
import model.GameData;
import model.request.ListGameRequest;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.ListGameResult;
import model.result.LoginResult;
import model.result.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    static GameDataAccess gameDA = DataAccessFactory.createGameDataAccess();
    static UserDataAccess userDA = DataAccessFactory.createUserDataAccess();
    static AuthDataAccess authDA = DataAccessFactory.createAuthDataAccess();
    static GameService gameService = new GameService(gameDA, userDA, authDA);
    static UserService userService = new UserService(gameDA, userDA, authDA);

    @BeforeEach
    void setUp() {
        gameDA = DataAccessFactory.createGameDataAccess();
        userDA = DataAccessFactory.createUserDataAccess();
        authDA = DataAccessFactory.createAuthDataAccess();
        gameService = new GameService(gameDA, userDA, authDA);
        userService = new UserService(gameDA, userDA, authDA);

        try {
            RegisterRequest regReq = new RegisterRequest("testName", "testPassword", "test@t.com");
            RegisterResult res = userService.register(regReq);
            userService.logout(new LogoutRequest(res.authToken()));
        } catch (DataAccessException e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    void listGamesPositive() {
        try{
            LoginRequest logReq = new LoginRequest("testName", "testPassword");
            LoginResult logRes = userService.login(logReq);
            ListGameRequest listReq = new ListGameRequest(logRes.authToken());

            gameDA.createGame("testGame");
            ListGameResult listRes = gameService.listGames(listReq);
            ArrayList<GameData> expected = new ArrayList<>(gameDA.getAllGames());

            assertTrue(expected.containsAll(listRes.games()) && listRes.games().containsAll(expected));
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    void listGamesNegative(){
        assertThrows(DataAccessException.class, () -> gameService.listGames(new ListGameRequest("bad")));
    }

    @Test
    void createGamePositive(){
        try{
            LoginRequest logReq = new LoginRequest("testName", "testPassword");
            LoginResult logRes = userService.login(logReq);
            CreateGameRequest creReq = new CreateGameRequest(logRes.authToken(), "testGame");
            CreateGameResult creRes = gameService.createGame(creReq);

            assertNotNull(gameDA.getGame(creRes.gameID()));
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    void createGameNegative(){
        assertThrows(DataAccessException.class, () -> gameService.createGame
                (new CreateGameRequest("bad", "name")));
    }

    @Test
    void joinGamePositive(){
        try{
            LoginRequest logReq = new LoginRequest("testName", "testPassword");
            LoginResult logRes = userService.login(logReq);
            CreateGameRequest creReq = new CreateGameRequest(logRes.authToken(), "testGame");
            CreateGameResult creRes = gameService.createGame(creReq);

            JoinGameRequest joinReq = new JoinGameRequest(logRes.authToken(), "WHITE", creRes.gameID());
            gameService.joinGame(joinReq);

            assertFalse(gameDA.getGame(creRes.gameID()).whiteUsername().isEmpty());
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    void joinGameNegative(){
        try{
            LoginRequest logReq = new LoginRequest("testName", "testPassword");
            LoginResult logRes = userService.login(logReq);
            CreateGameRequest creReq = new CreateGameRequest(logRes.authToken(), "testGame");
            CreateGameResult creRes = gameService.createGame(creReq);

            RegisterRequest regReq2 = new RegisterRequest("testName2", "testPassword", "test@t.com");
            RegisterResult res2 = userService.register(regReq2);

            JoinGameRequest joinReq = new JoinGameRequest(logRes.authToken(), "WHITE", creRes.gameID());
            gameService.joinGame(joinReq);

            JoinGameRequest joinReq2 = new JoinGameRequest(res2.authToken(), "WHITE", creRes.gameID());
            assertThrows(DataAccessException.class, () -> gameService.joinGame(joinReq2));
        } catch (DataAccessException e){
            fail(e.getMessage());
        }
    }
}