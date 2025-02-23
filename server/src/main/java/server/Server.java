package server;

import dataaccess.DataAccessException;
import service.GameService;
import spark.*;

public class Server {

    private final UserService userService;
    private final AuthService authService;
    private final GameService gameService;
    private final GameHandler gameHandler;
    private final UserHandler userHandler;
    private final AuthHandler authHandler;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    //Handlers
    private Object clear(Request request, Response response) throws DataAccessException {
        //don't i do all the gson json stuff here to go from request -> java object? not necessary here?
        gameService.clear();
        response.status(200);
        return "";
    }
}
