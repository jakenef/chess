package ui;

import exception.ResponseException;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.ListGameRequest;
import model.request.LogoutRequest;
import model.result.CreateGameResult;
import model.result.JoinGameResult;
import model.result.ListGameResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignedInClient implements ClientInterface{
    private final Repl repl;
    private ArrayList<GameData> gameList = null;

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
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

    public String create(String... params) throws ResponseException {
        if (params.length != 1){
            throw new ResponseException(400, "Expected: create <GAMENAME>");
        }
        CreateGameResult result = repl.getServer().createGame(new CreateGameRequest(repl.getAuthToken(), params[0]));
        return "Successfully created your game.";
    }

    public String logout() throws ResponseException {
        repl.getServer().logout(new LogoutRequest(repl.getAuthToken()));
        repl.setAuthToken(null);
        repl.setState(State.SIGNED_OUT);
        return "Successfully signed out.";
    }

    private String formatGameList(List<GameData> games) {
        StringBuilder gameListStr = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            GameData game = games.get(i);
            String whiteUsername = (game.whiteUsername() == null) ? "empty" : game.whiteUsername();
            String blackUsername = (game.blackUsername() == null) ? "empty" : game.blackUsername();

            gameListStr.append((i + 1))
                    .append(". Game Name: ")
                    .append(game.gameName())
                    .append(", White: ")
                    .append(whiteUsername)
                    .append(", Black: ")
                    .append(blackUsername)
                    .append("\n");
        }
        return gameListStr.toString();
    }

    public String list() throws ResponseException {
        ListGameResult result = repl.getServer().listGame(new ListGameRequest(repl.getAuthToken()));
        gameList = result.games();
        return "All Games: \n" + formatGameList(result.games());
    }

    public String join(String... params) throws ResponseException {
        if (params.length != 2){
            throw new ResponseException(400, "Expected: join <ID> [WHITE/BLACK]");
        } else if (gameList == null){
            throw new ResponseException(400, "You must list the games out before joining one.");
        }
        int index = -1;
        try {
            index = Integer.parseInt(params[0]) - 1;
        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Expected: join <ID> [WHITE/BLACK], where <ID> is a number.");
        }
        if (!params[1].equalsIgnoreCase("WHITE") && !params[1].equalsIgnoreCase("BLACK")){
            throw new ResponseException(400, "Expected: join <ID> [WHITE/BLACK]");
        }
        if (index > gameList.size() - 1 || index < 0){
            throw new ResponseException(400, "Game ID is incorrect. Use `list` and try again.");
        }
        GameData game = gameList.get(index);
        JoinGameRequest request = new JoinGameRequest(repl.getAuthToken(), params[1].toUpperCase(), game.gameID());
        JoinGameResult result = repl.getServer().joinGame(request);
        repl.setState(State.GAMEPLAY);
        return "Successfully joined game: " + game.gameName();
    }

    public String observe(String... params) throws ResponseException {
        if (params.length != 1){
            throw new ResponseException(400, "Expected: observe <ID>");
        } else if (gameList == null){
            throw new ResponseException(400, "You must list the games out before observing one.");
        }
        int index = -1;
        try {
            index = Integer.parseInt(params[0]) - 1;
        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Expected: observe <ID>, where <ID> is a number.");
        }
        if (index > gameList.size() - 1 || index < 0){
            throw new ResponseException(400, "Game ID is incorrect. Use `list` and try again.");
        }
        return "observing game ID: " + (index + 1);
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
