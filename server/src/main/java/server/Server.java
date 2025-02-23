package server;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import service.ClearService;
import service.GameService;
import spark.*;

public class Server {

//    private final UserService userService;
//    private final AuthService authService;
    private final GameService gameService;
//    private final GameHandler gameHandler;
//    private final UserHandler userHandler;
//    private final AuthHandler authHandler;
    private final ClearHandler clearHandler;

    public Server(){
        MemoryGameDataAccess mGameDA = new MemoryGameDataAccess();
        MemoryUserDataAccess mUserDA = new MemoryUserDataAccess();
        MemoryAuthDataAccess mAuthDA = new MemoryAuthDataAccess();

        ClearService clearService = new ClearService(mGameDA, mUserDA, mAuthDA);
        this.gameService = new GameService();

        this.clearHandler = new ClearHandler(clearService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::handle);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
