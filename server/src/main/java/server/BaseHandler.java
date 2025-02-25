package server;

import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

public abstract class BaseHandler {
    public abstract Object handle(Request request, Response res) throws DataAccessException;


}
