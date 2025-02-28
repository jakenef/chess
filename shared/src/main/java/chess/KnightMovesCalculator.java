package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {
    private static final int[] X_MOVES = {-2, -2, -1, -1, 1, 1, 2, 2};
    private static final int[] Y_MOVES = {-1, 1, -2, 2, -2, 2, -1, 1};

    /**
     * Calculates all possible moves for a knight from a given position on the board.
     * Considers all eight possible L-shaped moves and checks if they are within bounds and not occupied by a teammate.
     *
     * @param board The chessboard.
     * @param position The starting position of the knight.
     * @return A collection of possible moves for the knight.
     */
    public static Collection<ChessMove> calculateKnightMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possibleMoveList = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            if(ChessPosition.isInBounds(position.getRow() + Y_MOVES[i], position.getColumn() + X_MOVES[i])){
                ChessPosition possiblePosition = new ChessPosition(position.getRow() + Y_MOVES[i],
                        position.getColumn() + X_MOVES[i]);
                if(board.getPiece(possiblePosition) == null
                        || ChessBoard.isDifferentTeam(board, position, possiblePosition)){
                    ChessMove possibleMove = new ChessMove(position, possiblePosition, null);
                    possibleMoveList.add(possibleMove);
                }
            }
        }
        return possibleMoveList;
    }
}
