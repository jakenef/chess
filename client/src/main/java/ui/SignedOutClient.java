package ui;

public class SignedOutClient implements ClientInterface{
    @Override
    public String eval(String input) {
        return input;
    }

    @Override
    public String help() {
        return """
        \tregister <USERNAME> <PASSWORD> <EMAIL> - to create an account
        \tlogin <USERNAME> <PASSWORD> - to play chess
        \tquit - playing chess
        \thelp - display possible commands""";
    }
}
