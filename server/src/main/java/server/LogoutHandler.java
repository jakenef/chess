package server;

import dataaccess.DataAccessException;
import model.request.LogoutRequest;
import model.result.LogoutResult;
import service.UserService;
import spark.Request;

public class LogoutHandler extends BaseHandler<LogoutRequest, LogoutResult>{
    private final UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected LogoutResult processRequest(LogoutRequest request) throws DataAccessException {
        return userService.logout(request);
    }

    @Override
    protected LogoutRequest parseRequest(Request req) {
        String authToken = req.headers("authorization");
        return new LogoutRequest(authToken);
    }
}
