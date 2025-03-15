package ui;

public class GameplayClient implements ClientInterface{
    @Override
    public String eval(String input) {
        return input;
    }

    @Override
    public String help() {
        return "";
    }
}
