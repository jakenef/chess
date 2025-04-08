package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import websocketfacade.WebSocketFacade;

import java.util.Arrays;
import java.util.List;


/**
 * The `GameplayClient` class implements the `ClientInterface` and handles the gameplay state of the REPL.
 * It processes user commands related to gameplay, such as making moves, resigning, and leaving the game.
 */
public class GameplayClient implements ClientInterface{
    private final Repl repl;

    public GameplayClient(Repl repl) {
        this.repl = repl;
    }

    /**
         * Evaluates the user input command and executes the corresponding action.
         * Supported commands include:
         * <ul>
         *   <li>leave - Leave the current game and return to SIGNED_IN mode</li>
         *   <li>print - Print the current state of the chess board</li>
         *   <li>resign - Resign from the current game</li>
         *   <li>move - Make a move in the current game</li>
         *   <li>highlight - Highlight legal moves for a chosen piece</li>
         *   <li>quit - Quit the REPL</li>
         *   <li>help - Display the help message</li>
         * </ul>
         *
         * @param input the user input command
         * @return the result of the command execution
         */
    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd) {
                case "leave" -> leave();
                case "print" -> PrintBoardHelper.getBoardString(repl.getJoinedGameData(), repl);
                case "resign" -> resign();
                case "move" -> move(params);
                case "highlight" -> highlight(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

    public String highlight(String... params) {
        return "";
    }


    /**
     * Executes a move command in the current game.
     * The command expects two or three parameters:
     * <ul>
     *   <li>from - the starting position of the piece (e.g., a3)</li>
     *   <li>to - the ending position of the piece (e.g., a4)</li>
     *   <li>promotionPiece - optional, the piece to promote to if applicable (e.g., QUEEN)</li>
     * </ul>
     *
     * @param params the parameters for the move command
     * @return an empty string if the move is successful, or an error message if the move fails
     * @throws ResponseException if the move command is invalid or fails
     */
    public String move(String... params) throws ResponseException {
        if(params.length != 2 && params.length != 3) {
            throw new ResponseException(400, "Expected: move <a-h, 1-8> <a-h, 1-8> <promotionPiece> (ex. move a3 a4)");
        }
        ChessPosition from, to;
        String promotionPiece = null;
        try {
            from = MoveInputParser.parsePosition(params[0]);
            to = MoveInputParser.parsePosition(params[1]);
            if (params.length == 3) {
                promotionPiece = params[2].toUpperCase();

                List<String> validPromotions = List.of("QUEEN", "ROOK", "BISHOP", "KNIGHT");
                if (!validPromotions.contains(promotionPiece)) {
                    return "Invalid promotion piece. Choose from QUEEN, ROOK, BISHOP, or KNIGHT.";
                }
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseException(400, "Expected: move <a-h, 1-8> <a-h, 1-8> (ex. move a3 a4)");
        }
        ChessMove move = new ChessMove(from, to, ChessPiece.fromString(promotionPiece));

        WebSocketFacade ws = repl.getWs();
        ws.makeMove(repl.getAuthToken(), repl.getJoinedGameData().gameID(), move);

        return "";
    }

    public String resign() throws ResponseException {
        WebSocketFacade ws = repl.getWs();
        ws.resign(repl.getAuthToken(), repl.getJoinedGameData().gameID());
        return "";
    }

    public String leave() throws ResponseException {
        repl.setState(State.SIGNED_IN);

        WebSocketFacade ws = repl.getWs();
        ws.leave(repl.getAuthToken(), repl.getJoinedGameData().gameID());

        repl.setJoinedGameData(null);
        repl.setJoinedAsTeamColor(null);
        return "Successfully left gameplay.";
    }

    @Override
    public String help() {
        return """
        print - redraw board
        move <a-h, 1-8> <a-h, 1-8> (ex. move a3 a4) - enter start position and end position for a chess move
        highlight <a-h, 1-8> (ex. highlight a2) - highlights legal moves for chosen piece
        resign - as a player, you may resign and end the game
        leave - return to SIGNED_IN mode
        help - display this message""";
    }
}
