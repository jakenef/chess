package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import service.UserService;
import spark.Request;

public class RegisterHandler extends BaseHandler<RegisterRequest, RegisterResult> {

    private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected RegisterResult processRequest(RegisterRequest request) throws DataAccessException {
        return userService.register(request);
    }

    @Override
    protected RegisterRequest parseRequest(Request req) {
        return new Gson().fromJson(req.body(), RegisterRequest.class);
    }
}
