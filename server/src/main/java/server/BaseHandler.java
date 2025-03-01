package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.utils.ErrorMessage;
import model.utils.RecordUtils;
import spark.Request;
import spark.Response;

import java.util.Objects;

public abstract class BaseHandler<T, R> {
    private final Gson gson = new Gson();

    protected abstract R processRequest(T request) throws DataAccessException;

    protected abstract T parseRequest(Request req);

    public Object handle(Request req, Response res){
        T parsedReq = parseRequest(req);

        if(parsedReq == null || RecordUtils.isNull(parsedReq)){
            return errorResponse(res, 400, "bad request");
        }

        try {
            R result = processRequest(parsedReq);
            res.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e){
            if (Objects.equals(e.getMessage(), "unauthorized")) {
                return errorResponse(res, 401, e.getMessage());
            } else if (Objects.equals(e.getMessage(), "username already taken")) {
                return errorResponse(res, 403, e.getMessage());
            }else{
                return errorResponse(res, 500, e.getMessage());
            }
        }
    }

    private String errorResponse(Response res, int status, String message){
        res.status(status);
        return new ErrorMessage(message).toJson();
    }
}
