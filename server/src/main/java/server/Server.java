package server;

import dataaccess.*;
import dataaccess.auth.AuthDataAccess;
import dataaccess.game.GameDataAccess;
import dataaccess.user.UserDataAccess;
import server.websocket.WebSocketHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final RegisterHandler registerHandler;
    private final ClearHandler clearHandler;
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;
    private final ListGamesHandler listGamesHandler;
    private final CreateGameHandler createGameHandler;
    private final JoinGameHandler joinGameHandler;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        GameDataAccess gameDA = DataAccessFactory.createGameDataAccess();
        UserDataAccess userDA = DataAccessFactory.createUserDataAccess();
        AuthDataAccess authDA = DataAccessFactory.createAuthDataAccess();

        ClearService clearService = new ClearService(gameDA, userDA, authDA);
        UserService userService = new UserService(gameDA, userDA, authDA);
        GameService gameService = new GameService(gameDA, userDA, authDA);

        this.clearHandler = new ClearHandler(clearService);
        this.registerHandler = new RegisterHandler(userService);
        this.loginHandler = new LoginHandler(userService);
        this.logoutHandler = new LogoutHandler(userService);
        this.listGamesHandler = new ListGamesHandler(gameService);
        this.createGameHandler = new CreateGameHandler(gameService);
        this.joinGameHandler = new JoinGameHandler(gameService);
        this.webSocketHandler = new WebSocketHandler(userDA, gameDA, authDA);

        try {
            DatabaseManager.configureDatabase();
            System.out.println("Database configured successfully!");
        } catch (DataAccessException e) {
            System.out.println("Failed to configure the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register endpoints and handle exceptions here.
        Spark.webSocket("/ws", webSocketHandler);

        Spark.delete("/db", clearHandler::handle);
        Spark.post("/user", registerHandler::handle);
        Spark.post("/session", loginHandler::handle);
        Spark.delete("/session", logoutHandler::handle);
        Spark.get("/game", listGamesHandler::handle);
        Spark.post("/game", createGameHandler::handle);
        Spark.put("/game", joinGameHandler::handle);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
