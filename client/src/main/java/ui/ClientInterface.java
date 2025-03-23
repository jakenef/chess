package ui;

import server.ServerFacade;

public interface ClientInterface {
    String eval(String input);
    String help();
}
