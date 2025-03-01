package server;

import dataaccess.DataAccessException;
import model.request.ClearRequest;
import model.result.ClearResult;
import service.ClearService;
import spark.Request;

public class ClearHandler extends BaseHandler<ClearRequest, ClearResult> {

    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    @Override
    protected ClearResult processRequest(ClearRequest request) throws DataAccessException {
        return clearService.clearAll(request);
    }

    @Override
    protected ClearRequest parseRequest(Request req) {
        return new ClearRequest();
    }
}