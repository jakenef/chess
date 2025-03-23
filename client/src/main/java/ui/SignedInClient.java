package ui;

public class SignedInClient implements ClientInterface{
    @Override
    public String eval(String input) {
        return input;
    }

    @Override
    public String help() {
        return """
        \tcreate <GAMENAME> - a game
        \tlist - games
        \tjoin <ID> [WHITE/BLACK] - a game
        \tobserve <ID> - a game
        \tlogout - when you are done
        \tquit - playing chess
        \thelp - display possible commands""";
    }
}
