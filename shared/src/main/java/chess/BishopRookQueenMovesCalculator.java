package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopRookQueenMovesCalculator {
    ArrayList<ChessMove> pieceMoves = new ArrayList<>();
    ChessBoard board;
    final ChessPosition startPos;

    public enum Direction{
        N(0,1),
        E(-1,0),
        W(1,0),
        S(0,-1),
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

    BishopRookQueenMovesCalculator(ChessBoard board, ChessPosition startPos) {
        this.board = board;
        this.startPos = startPos;
    }

    public ArrayList<ChessMove> getMoves() {
        return pieceMoves;
    }

    public static Collection<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition position){
        //recursively iterate over four paths until blocked
        BishopRookQueenMovesCalculator bmc = new BishopRookQueenMovesCalculator(board, position);
        bmc.checkPath(position, Direction.NW);
        bmc.checkPath(position, Direction.NE);
        bmc.checkPath(position, Direction.SW);
        bmc.checkPath(position, Direction.SE);
        return bmc.getMoves();
    }

    public static Collection<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition position) {
        //recursively iterate over four paths until blocked
        BishopRookQueenMovesCalculator rmc = new BishopRookQueenMovesCalculator(board, position);
        rmc.checkPath(position, BishopRookQueenMovesCalculator.Direction.N);
        rmc.checkPath(position, BishopRookQueenMovesCalculator.Direction.W);
        rmc.checkPath(position, BishopRookQueenMovesCalculator.Direction.S);
        rmc.checkPath(position, BishopRookQueenMovesCalculator.Direction.E);
        return rmc.getMoves();
    }

    public static Collection<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition position){
        BishopRookQueenMovesCalculator qmc = new BishopRookQueenMovesCalculator(board, position);
        qmc.checkPath(position, Direction.NW);
        qmc.checkPath(position, Direction.NE);
        qmc.checkPath(position, Direction.SW);
        qmc.checkPath(position, Direction.SE);
        qmc.checkPath(position, BishopRookQueenMovesCalculator.Direction.N);
        qmc.checkPath(position, BishopRookQueenMovesCalculator.Direction.W);
        qmc.checkPath(position, BishopRookQueenMovesCalculator.Direction.S);
        qmc.checkPath(position, BishopRookQueenMovesCalculator.Direction.E);
        return qmc.getMoves();
    }

    /**
     * Recursively checks the path in a given direction from a starting position on the chessboard.
     * Adds valid moves for a piece to the pieceMoves list.
     *
     * @param pathPosition The path position of the piece. (for recursion)
     * @param direction The direction to check for valid moves.
     */
    public void checkPath(ChessPosition pathPosition, Direction direction){
        if(pathPosition.getColumn() + direction.x > 8 || pathPosition.getColumn() + direction.x < 1
                || pathPosition.getRow() + direction.y > 8 || pathPosition.getRow() + direction.y < 1){
            return; // if next step out of bounds
        }
        ChessPosition nextStep = new ChessPosition(pathPosition.getRow()+direction.y,
                pathPosition.getColumn()+direction.x);
        if (board.getPiece(nextStep) != null // if the next step is occupied by teammate
                && board.getPiece(nextStep).getTeamColor().equals(board.getPiece(startPos).getTeamColor())){
            return;
        } else if (board.getPiece(nextStep) != null // if the next step is occupied by enemy
                && !board.getPiece(nextStep).getTeamColor().equals(board.getPiece(startPos).getTeamColor())){
            ChessMove possibleMove = new ChessMove(startPos, nextStep, null);
            pieceMoves.add(possibleMove);
            return;
        } else { //clear, add to possible moves
            ChessMove possibleMove = new ChessMove(startPos, nextStep, null);
            pieceMoves.add(possibleMove);
            checkPath(nextStep, direction);
        }
    }
}