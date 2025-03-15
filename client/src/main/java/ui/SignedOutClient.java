package ui;

public class SignedOutClient implements ClientInterface{
    @Override
    public String eval(String input) {
        return input;
    }

    @Override
    public String help() {
        return "help message: ";
    }
}
