package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;

import java.util.Arrays;

import static ui.EscapeSequences.*;

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
                case "print" -> printBoard();
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

    public String printBoard() {
        ChessBoard board = repl.getJoinedGameData().game().getBoard();
        StringBuilder boardString = new StringBuilder();

        ChessGame.TeamColor perspective = repl.getJoinedAsTeamColor();
        String[] colsWhite = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] colsBlack = {"h", "g", "f", "e", "d", "c", "b", "a"};

        boolean isWhite = (perspective == ChessGame.TeamColor.WHITE);
        String[] colLabels = isWhite ? colsWhite : colsBlack;
        int[] rowOrder;
        if (isWhite) {
            rowOrder = new int[]{8, 7, 6, 5, 4, 3, 2, 1};

        } else {
            rowOrder = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        }

        boolean isFlipped = !isWhite;

        boardString.append("  ");
        for (String col : colLabels) {
            boardString.append(" ").append(col).append(" ");
        }
        boardString.append("\n");

        for (int row : rowOrder) {
            boardString.append(row).append(" ");
            for (int col = 0; col < 8; col++) {
                ChessPosition pos = new ChessPosition(row, isWhite ? col + 1 : 8 - col);
                ChessPiece piece = board.getPiece(pos);

                boolean isLightSquare;
                if (isFlipped) {
                    isLightSquare = (row + col) % 2 != 0;
                } else {
                    isLightSquare = (row + col) % 2 == 0;
                }

                String squareColor = isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;

                String pieceChar = (piece == null) ? EMPTY : getUnicodeForPiece(piece);

                boardString.append(squareColor).append(pieceChar).append(RESET);
            }
            boardString.append(" ").append(row).append("\n");
        }

        boardString.append("  ");
        for (String col : colLabels) {
            boardString.append(" ").append(col).append(" ");
        }
        boardString.append("\n");

        return boardString.toString();
    }

    /**
     * Retrieves the Unicode symbol for the given chess piece.
     *
     * @param piece The chess piece.
     * @return The Unicode character for the piece.
     */
    private String getUnicodeForPiece(ChessPiece piece) {
        if (piece == null) {
            return " ";
        }
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;

        return switch (piece.getPieceType()) {
            case KING -> isWhite ? BLACK_KING : WHITE_KING;
            case QUEEN -> isWhite ? BLACK_QUEEN : WHITE_QUEEN;
            case ROOK -> isWhite ? BLACK_ROOK : WHITE_ROOK;
            case BISHOP -> isWhite ? BLACK_BISHOP : WHITE_BISHOP;
            case KNIGHT -> isWhite ? BLACK_KNIGHT : WHITE_KNIGHT;
            case PAWN -> isWhite ? BLACK_PAWN : WHITE_PAWN;
        };
    }
}
