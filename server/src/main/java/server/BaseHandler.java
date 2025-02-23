package server;

import org.eclipse.jetty.client.api.Request;

public abstract class BaseHandler {
    public abstract String handleRequest(Request request);


}
