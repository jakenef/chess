package client;

import exception.ResponseException;
import model.request.ClearRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
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
        var authData = sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com"));
        assertThrows(ResponseException.class, () -> sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com")));
    }

    @Test
    public void clearPos() throws ResponseException {
        var authData = sFacade.register(new RegisterRequest("testUser",
                "password", "t@test.com"));
        sFacade.clear(new ClearRequest());
        assertThrows(ResponseException.class, () -> sFacade.login(new LoginRequest(
                "testUser", "password")));
    }
}
