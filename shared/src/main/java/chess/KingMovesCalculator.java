package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {
    private static final int[] POSSIBLE_MOVE_INT = new int[]{-1, 0, 1};

    /**
     * Calculates all possible moves for a King from a given position on the chessboard.
     * The King can move one square in any direction (horizontally, vertically, or diagonally),
     * provided the move is within the bounds of the board and the destination square is either empty
     * or occupied by an opponent's piece.
     *
     * @param board The chessboard on which the King is moving.
     * @param position The current position of the King.
     * @return A collection of all valid moves for the King from the given position.
     */
    public static Collection<ChessMove> calculateKingMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possibleKingMoves = new ArrayList<>();
        for (int i : POSSIBLE_MOVE_INT) {
            for (int j : POSSIBLE_MOVE_INT) {
                // if in bounds of the board
                if (ChessPosition.isInBounds(position.getRow() + j,
                        position.getColumn() + i)) {
                    ChessPosition possiblePosition = new ChessPosition(position.getRow() + j,
                            position.getColumn() + i);
                    ChessMove possibleMove = new ChessMove(position, possiblePosition, null);
                    if (board.getPiece(possiblePosition) == null ||
                            ChessBoard.isDifferentTeam(board, position, possiblePosition)) {
                        possibleKingMoves.add(possibleMove);
                    }
                }
            }
        }
        return possibleKingMoves;
    }
}
