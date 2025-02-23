package server;

import dataaccess.DataAccessException;
import model.ClearRequest;
import model.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler extends BaseHandler {

    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    @Override
    public String handle(Request request, Response res) throws DataAccessException {
        ClearRequest req = new ClearRequest();
        ClearResult result = clearService.clearAll(req);
        return "";
    }
}
