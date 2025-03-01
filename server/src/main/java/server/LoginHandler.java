package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.request.LoginRequest;
import model.result.LoginResult;
import service.UserService;
import spark.Request;

public class LoginHandler extends BaseHandler<LoginRequest, LoginResult> {

    private final UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected LoginResult processRequest(LoginRequest request) throws DataAccessException {
        return userService.login(request);
    }

    @Override
    protected LoginRequest parseRequest(Request req) {
        return new Gson().fromJson(req.body(), LoginRequest.class);
    }
}