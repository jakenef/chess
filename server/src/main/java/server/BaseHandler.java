package server;

import spark.Request;
import spark.Response;

public interface BaseHandler {
    Object handle(Request req, Response res);
}
