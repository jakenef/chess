package server;

import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

public interface BaseHandler {
    public Object handle(Request req, Response res) throws DataAccessException;
}
