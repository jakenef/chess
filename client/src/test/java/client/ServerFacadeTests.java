package client;

import exception.ResponseException;
import model.request.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade sFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        sFacade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    void setUp() throws ResponseException {
        sFacade.clear(new ClearRequest());
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerPositive() throws ResponseException {
        var authData = sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com"));
        assertFalse(authData.authToken().isEmpty());
    }

    @Test
    public void registerNegative() throws ResponseException {
        sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com"));
        assertThrows(ResponseException.class, () -> sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com")));
    }

    @Test
    public void clearPos() throws ResponseException {
        sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com"));
        sFacade.clear(new ClearRequest());
        assertThrows(ResponseException.class, () -> sFacade.login(new LoginRequest(
                "testUser", "password")));
    }

    @Test
    public void loginPos() throws ResponseException {
        sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com"));
        var loginResult = sFacade.login(new LoginRequest("testUser", "password"));
        assertFalse(loginResult.authToken().isEmpty());
    }

    @Test
    public void loginNeg(){
        assertThrows(ResponseException.class, () -> sFacade.login(new
                LoginRequest("badUser", "bad")));
    }

    @Test
    public void logoutPos() throws ResponseException {
        sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com"));
        var loginResult = sFacade.login(new LoginRequest("testUser", "password"));
        assertDoesNotThrow(() -> sFacade.logout(new LogoutRequest(loginResult.authToken())));
    }

    @Test
    public void logoutNeg(){
        assertThrows(ResponseException.class, () -> sFacade.logout(new
                LogoutRequest("notAuth")));
    }

    @Test
    public void listGamesPos() throws ResponseException {
        sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com"));
        var loginResult = sFacade.login(new LoginRequest("testUser", "password"));
        sFacade.createGame(new CreateGameRequest(loginResult.authToken(), "testGame"));
        var listResult = sFacade.listGame(new ListGameRequest(loginResult.authToken()));
        assertFalse(listResult.games().isEmpty());
    }

    @Test
    public void listGamesNeg(){
        assertThrows(ResponseException.class, ()-> sFacade.listGame(new ListGameRequest("bad")));
    }

    @Test
    public void createGamePos() throws ResponseException {
        sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com"));
        var loginResult = sFacade.login(new LoginRequest("testUser", "password"));
        var createGameResult = sFacade.createGame
                (new CreateGameRequest(loginResult.authToken(), "testGame"));
        assertNotNull(createGameResult.gameID());
    }

    @Test
    public void createGameNeg(){
        assertThrows(ResponseException.class, () -> sFacade.createGame(new
                CreateGameRequest("notAuth", "testGame")));
    }

    @Test
    public void joinGamePos() throws ResponseException {
        sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com"));
        var loginResult = sFacade.login(new LoginRequest("testUser", "password"));
        var createGameResult = sFacade.createGame
                (new CreateGameRequest(loginResult.authToken(), "testGame"));
        assertDoesNotThrow(() -> sFacade.joinGame(new JoinGameRequest(loginResult.authToken(),
                "WHITE", createGameResult.gameID())));

    }

    @Test
    public void joinGameNeg(){
        assertThrows(ResponseException.class, () -> sFacade.joinGame(new
                JoinGameRequest("bad", "WHITE", 22)));
    }
}
