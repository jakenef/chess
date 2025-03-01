package server;

import dataaccess.DataAccessException;
import model.request.ClearRequest;
import model.utils.ErrorMessage;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler implements BaseHandler {

    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public Object handle(Request request, Response res) {
        ClearRequest req = new ClearRequest();
        try {
            clearService.clearAll(req);
        } catch (DataAccessException e) {
            res.status(500);
            ErrorMessage errMsg = new ErrorMessage(e.getMessage());
            return errMsg.toJson();
        }
        res.status(200);
        return "{}";
    }
}
