package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RecursiveMovesCalculator {
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

    RecursiveMovesCalculator(ChessBoard board, ChessPosition startPos) {
        this.board = board;
        this.startPos = startPos;
    }

    public ArrayList<ChessMove> getMoves() {
        return pieceMoves;
    }

    /**
     * Calculates all possible moves for a bishop from a given position on the board.
     * Recursively iterates over four diagonal paths until blocked.
     *
     * @param board The chessboard.
     * @param position The starting position of the bishop.
     * @return A collection of possible moves for the bishop.
     */
    public static Collection<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition position){
        //recursively iterate over four paths until blocked
        RecursiveMovesCalculator bmc = new RecursiveMovesCalculator(board, position);
        bmc.checkPath(position, Direction.NW);
        bmc.checkPath(position, Direction.NE);
        bmc.checkPath(position, Direction.SW);
        bmc.checkPath(position, Direction.SE);
        return bmc.getMoves();
    }

    /**
     * Calculates all possible moves for a rook from a given position on the board.
     * Recursively iterates over four straight paths until blocked.
     *
     * @param board The chessboard.
     * @param position The starting position of the rook.
     * @return A collection of possible moves for the rook.
     */
    public static Collection<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition position) {
        //recursively iterate over four paths until blocked
        RecursiveMovesCalculator rmc = new RecursiveMovesCalculator(board, position);
        rmc.checkPath(position, RecursiveMovesCalculator.Direction.N);
        rmc.checkPath(position, RecursiveMovesCalculator.Direction.W);
        rmc.checkPath(position, RecursiveMovesCalculator.Direction.S);
        rmc.checkPath(position, RecursiveMovesCalculator.Direction.E);
        return rmc.getMoves();
    }

    /**
     * Calculates all possible moves for a queen from a given position on the board.
     * Recursively iterates over all eight possible paths until blocked.
     *
     * @param board The chessboard.
     * @param position The starting position of the queen.
     * @return A collection of possible moves for the queen.
     */
    public static Collection<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition position){
        RecursiveMovesCalculator qmc = new RecursiveMovesCalculator(board, position);
        for(Direction d : Direction.values()) qmc.checkPath(position,d);
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
        if(!ChessPosition.isInBounds(pathPosition.getRow() + direction.y,
                pathPosition.getColumn() + direction.x)){
            return; // if next step out of bounds
        }
        ChessPosition nextStep = new ChessPosition(pathPosition.getRow()+direction.y,
                pathPosition.getColumn()+direction.x);
        if (board.getPiece(nextStep) != null // if the next step is occupied by teammate
                && ChessBoard.isSameTeam(board, startPos, nextStep)){
            return;
        } else if (board.getPiece(nextStep) != null && !ChessBoard.isSameTeam(board, startPos, nextStep)){
            // if the next step is occupied by enemy
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