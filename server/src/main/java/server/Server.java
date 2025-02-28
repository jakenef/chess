package server;

import dataaccess.*;
import service.ClearService;
import service.UserService;
import spark.*;

public class Server {

    private final RegisterHandler registerHandler;
    private final ClearHandler clearHandler;
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;

    public Server(){
        GameDataAccess gameDA = DataAccessFactory.createGameDataAccess();
        UserDataAccess userDA = DataAccessFactory.createUserDataAccess();
        AuthDataAccess authDA = DataAccessFactory.createAuthDataAccess();

        ClearService clearService = new ClearService(gameDA, userDA, authDA);
        UserService userService = new UserService(gameDA, userDA, authDA);
        this.clearHandler = new ClearHandler(clearService);
        this.registerHandler = new RegisterHandler(userService);
        this.loginHandler = new LoginHandler(userService);
        this.logoutHandler = new LogoutHandler(userService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::handle);
        Spark.post("/user", registerHandler::handle);
        Spark.post("/session", loginHandler::handle);
        Spark.delete("/session", logoutHandler::handle);

        //This line initializes the server and can be removed once you have a functioning endpoint 

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
