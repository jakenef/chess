package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {
    private static final int[] xMoves = {-2, -2, -1, -1, 1, 1, 2, 2};
    private static final int[] yMoves = {-1, 1, -2, 2, -2, 2, -1, 1};

    public static Collection<ChessMove> calculateKnightMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possibleMoveList = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            if(ChessPosition.isInBounds(position.getRow() + yMoves[i], position.getColumn() + xMoves[i])){
                ChessPosition possiblePosition = new ChessPosition(position.getRow() + yMoves[i],
                        position.getColumn() + xMoves[i]);
                if(board.getPiece(possiblePosition) == null
                        || !ChessBoard.isSameTeam(board, position, possiblePosition)){
                    ChessMove possibleMove = new ChessMove(position, possiblePosition, null);
                    possibleMoveList.add(possibleMove);
                }
            }
        }
        return possibleMoveList;
    }
}
