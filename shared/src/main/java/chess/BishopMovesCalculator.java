package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {
    ArrayList<ChessMove> bishopMoves = new ArrayList<ChessMove>();
    ChessBoard board = null;
    final ChessPosition startPos;

    public enum Direction{
        NW(1,-1),
        NE(1,1),
        SW(-1,-1),
        SE(-1,1);
        public final int x;
        public final int y;
        Direction(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    private BishopMovesCalculator(ChessBoard board, ChessPosition startPos) {
        this.board = board;
        this.startPos = startPos;
    }

    public ArrayList<ChessMove> getBishopMoves() {
        return bishopMoves;
    }

    public static Collection<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition position){
        //recursively iterate over four paths until blocked
        BishopMovesCalculator bmc = new BishopMovesCalculator(board, position);
        bmc.checkPath(position, Direction.NW);
        bmc.checkPath(position, Direction.NE);
        bmc.checkPath(position, Direction.SW);
        bmc.checkPath(position, Direction.SE);
        return bmc.getBishopMoves();
    }

    /**
     * Recursively checks the path in a given direction from a starting position on the chessboard.
     * Adds valid moves for a bishop to the bishopMoves list.
     *
     * @param pathPosition The path position of the bishop. (for recursion)
     * @param direction The direction to check for valid moves.
     */
    private void checkPath(ChessPosition pathPosition, Direction direction){
        if(pathPosition.getColumn() + direction.x > 8 || pathPosition.getColumn() + direction.x < 1
                || pathPosition.getRow() + direction.y > 8 || pathPosition.getRow() + direction.y < 1){
            return; // if next step out of bounds
        }
        ChessPosition nextStep = new ChessPosition(pathPosition.getColumn()+direction.x,
                pathPosition.getRow()+direction.y);
        if (board.getPiece(nextStep) != null // if the next step is occupied by teammate
                && board.getPiece(nextStep).getTeamColor().equals(board.getPiece(startPos).getTeamColor())){
            return;
        } else if (board.getPiece(nextStep) != null // if the next step is occupied by enemy
                && !board.getPiece(nextStep).getTeamColor().equals(board.getPiece(startPos).getTeamColor())){
            ChessMove possibleMove = new ChessMove(startPos, nextStep, null);
            bishopMoves.add(possibleMove);
            return;
        } else { //clear, add to possible moves
            ChessMove possibleMove = new ChessMove(startPos, nextStep, null);
            bishopMoves.add(possibleMove);
            checkPath(nextStep, direction);
        }
    }
}