package ui;

public class SignedInClient implements ClientInterface{
    @Override
    public String eval(String input) {
        return input;
    }

    @Override
    public String help() {
        return "";
    }
}
