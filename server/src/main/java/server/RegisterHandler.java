package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.ErrorMessage;
import model.RecordUtils;
import model.RegisterRequest;
import model.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class RegisterHandler implements BaseHandler {

    private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) throws DataAccessException{
        RegisterResult regResult;
        RegisterRequest regReq = new Gson().fromJson(req.body(), RegisterRequest.class);

        if (RecordUtils.isNull(regReq)){
            res.status(400);
            ErrorMessage errMsg = new ErrorMessage("bad request");
            return errMsg.toJson();
        }

        try {
            regResult = userService.register(regReq);
        } catch (DataAccessException e) {
            if(Objects.equals(e.getMessage(), "username already taken")){
                res.status(403);
                ErrorMessage errMsg = new ErrorMessage("already taken");
                return errMsg.toJson();
            }else{
                res.status(500);
                ErrorMessage errMsg = new ErrorMessage("other");
                return errMsg.toJson();
            }
        }

        res.status(200);
        return new Gson().toJson(regResult);
    }
}
