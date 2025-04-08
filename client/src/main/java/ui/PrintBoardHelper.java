package ui;

import chess.*;
import model.GameData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.RESET;

public class PrintBoardHelper {


    public static String getBoardString(GameData gameData, ChessGame.TeamColor perspective,
                                        Set<ChessPosition> possibleMovePositions) {
        ChessBoard board = gameData.game().getBoard();
        StringBuilder boardString = new StringBuilder();

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
                boolean isHighlighted = possibleMovePositions != null && possibleMovePositions.contains(pos);

                String squareColor = isHighlighted ? NEON_GREEN_SQUARE : isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;

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

    public static String getBoardString(GameData gameData, ChessGame.TeamColor perspective) {
        return getBoardString(gameData, perspective, null);
    }

    /**
     * Retrieves the Unicode symbol for the given chess piece.
     *
     * @param piece The chess piece.
     * @return The Unicode character for the piece.
     */
    private static String getUnicodeForPiece(ChessPiece piece) {
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

    public static String getPossibleMovesBoardString(GameData joinedGameData,
                                                     ChessGame.TeamColor perspective, Collection<ChessMove> possibleMoves) {
        Set<ChessPosition> highlights = new HashSet<>();

        for (ChessMove move : possibleMoves) {
            highlights.add(move.getStartPosition());
            highlights.add(move.getEndPosition());
        }

        return getBoardString(joinedGameData, perspective, highlights);
    }
}
