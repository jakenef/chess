package server;

import dataaccess.DataAccessException;
import model.ClearRequest;
import model.ClearResult;
import model.ErrorMessage;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public Object handle(Request request, Response res) throws DataAccessException {
        ClearRequest req = new ClearRequest();
        ClearResult result = clearService.clearAll(req);
        if(result.success()){
            res.status(200);
            return "{}";
        } else {
            res.status(500);
            ErrorMessage errMsg = new ErrorMessage("(description of error)");
            return errMsg.toJson();
        }
    }
}
