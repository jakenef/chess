package ui;

import exception.ResponseException;
import websocketfacade.WebSocketFacade;

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

    public String leave() throws ResponseException {
        WebSocketFacade ws = repl.getWs();
        ws.leave(repl.getAuthToken(), repl.getJoinedGameData().gameID());

        repl.setState(State.SIGNED_IN);
        repl.setJoinedGameData(null);
        repl.setJoinedAsTeamColor(null);
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
