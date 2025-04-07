package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import websocketfacade.WebSocketFacade;

import java.util.Arrays;
import java.util.List;

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
                case "resign" -> resign();
                case "move" -> move(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

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
