package ui;

import exception.ResponseException;
import model.GameData;
import model.request.ListGameRequest;
import model.request.LogoutRequest;
import model.result.ListGameResult;

import java.util.Arrays;
import java.util.List;

public class SignedInClient implements ClientInterface{
    private final Repl repl;

    public SignedInClient(Repl repl) {
        this.repl = repl;
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd) {
                case "create" -> create(params);
                case "logout" -> logout();
                case "list" -> list();
                case "play" -> play(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

    public String create(String... params){
        return null;
    }

    public String logout() throws ResponseException {
        repl.getServer().logout(new LogoutRequest(repl.getAuthToken()));
        repl.setAuthToken(null);
        repl.setState(State.SIGNED_OUT);
        return "Successfully signed out.";
    }

    private String formatGameList(List<GameData> games) {
        StringBuilder formattedList = new StringBuilder();

        for (GameData game : games) {
            String whiteUsername = (game.whiteUsername() == null) ? "empty" : game.whiteUsername();
            String blackUsername = (game.blackUsername() == null) ? "empty" : game.blackUsername();

            formattedList.append("- Game Name: ").append(game.gameName())
                    .append(", White Username: ").append(whiteUsername)
                    .append(", Black Username: ").append(blackUsername)
                    .append(", Game ID: ").append(game.gameID())
                    .append("\n");
        }

        return formattedList.toString();
    }

    public String list() throws ResponseException {
        ListGameResult result = repl.getServer().listGame(new ListGameRequest(repl.getAuthToken()));
        return "All Games: \n" + formatGameList(result.games());
    }

    public String play(String... params){
        return null;
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
