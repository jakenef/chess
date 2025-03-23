package ui;

import server.ServerFacade;

public class SignedInClient implements ClientInterface{
    private final Repl repl;

    public SignedInClient(Repl repl) {
        this.repl = repl;
    }

    @Override
    public String eval(String input) {
        return input;
    }

    @Override
    public String help() {
        return """
        create <GAMENAME> - a game
        list - games
        join <ID> [WHITE/BLACK] - a game
        observe <ID> - a game
        logout - when you are done
        quit - playing chess
        help - display possible commands""";
    }
}
