package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class LoginHandler implements BaseHandler {

    private final UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        LoginResult logResult;
        LoginRequest logReq = new Gson().fromJson(req.body(), LoginRequest.class);

        if (RecordUtils.isNull(logReq)){
            res.status(400);
            ErrorMessage errMsg = new ErrorMessage("bad request");
            return errMsg.toJson();
        }

        try {
            logResult = userService.login(logReq);
        } catch (DataAccessException e) {
            if(Objects.equals(e.getMessage(), "unauthorized")){
                res.status(401);
                ErrorMessage errMsg = new ErrorMessage("unauthorized");
                return errMsg.toJson();
            }else{
                res.status(500);
                ErrorMessage errMsg = new ErrorMessage(e.getMessage());
                return errMsg.toJson();
            }
        }

        res.status(200);
        return new Gson().toJson(logResult);
    }
}
