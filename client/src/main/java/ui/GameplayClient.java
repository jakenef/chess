package ui;

import exception.ResponseException;

import java.util.Arrays;

public class GameplayClient implements ClientInterface{
    private final Repl repl;

    public GameplayClient(Repl repl) {
        this.repl = repl;
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (false){
                throw new ResponseException(400, "Stubbed out for next phase");
            }
            return switch(cmd) {
                case "leave" -> leave();
                case "print" -> PrintBoardHelper.getBoardString(repl.getJoinedGameData(), repl);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

    public String leave(){
        repl.setState(State.SIGNED_IN);
        return "Successfully left gameplay.";
    }

    @Override
    public String help() {
        return """
        print - redraw board
        leave - return to SIGNED_IN mode
        help - display this message""";
    }
}
