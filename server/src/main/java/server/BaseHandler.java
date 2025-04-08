package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.utils.ErrorMessage;
import model.utils.RecordUtils;
import spark.Request;
import spark.Response;

/**
 * Abstract base handler class for processing HTTP requests and responses.
 *
 * @param <T> the type of the request object
 * @param <R> the type of the response object
 */
public abstract class BaseHandler<T, R> {
    private final Gson gson = new Gson();

    protected abstract R processRequest(T request) throws DataAccessException;

    protected abstract T parseRequest(Request req);

    /**
     * Handles the HTTP request and response.
     *
     * @param req the HTTP request
     * @param res the HTTP response
     * @return the response object in JSON format
     */
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
            String errorMsg = e.getMessage();
            int statusCode = switch(errorMsg) {
                case "bad request" -> 400;
                case "unauthorized" -> 401;
                case "already taken" -> 403;
                default -> 500;
            };

            return errorResponse(res, statusCode, errorMsg);
        }
    }

    private String errorResponse(Response res, int status, String message){
        res.status(status);
        return new ErrorMessage(message).toJson();
    }
}
