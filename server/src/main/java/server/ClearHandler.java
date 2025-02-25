package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.ClearRequest;
import model.ClearResult;
import model.ErrorMessage;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler extends BaseHandler {

    private final ClearService clearService;
    private final Gson gson;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
        this.gson = new Gson();
    }

    @Override
    public Object handle(Request request, Response res) throws DataAccessException {
        ClearRequest req = new ClearRequest();
        ClearResult result = clearService.clearAll(req);
        if(result.success()){
            res.status(200);
            return "{}";
        } else {
            res.status(500);
            ErrorMessage errMsg = new ErrorMessage("(description of error)");
            return gson.toJson(errMsg);
        }
    }
}
