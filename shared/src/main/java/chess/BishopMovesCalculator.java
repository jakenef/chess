package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {

    public static Collection<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> bishopMoves = new ArrayList<ChessMove>();
        //traverse the board and find all possible end positions
        for(int y = 1; y <= 8; y++){
            for(int x = 1; x <= 8; x++){
                ChessPosition indexPosition = new ChessPosition(x,y);
                if(indexPosition.equals(position)){ // if the index is at the piece's current position, skip.
                    break;
                }
                if (isBishopPathClearAndDiagonal(position, indexPosition, board)){
                    ChessMove newMove = new ChessMove(position, indexPosition, null);
                    bishopMoves.add(newMove);
                }
            }
        }
        return bishopMoves;
    }

    public static boolean isBishopPathClearAndDiagonal(ChessPosition startPosition, ChessPosition endPosition, ChessBoard board){
        int absXOffset = Math.abs(endPosition.getColumn() - startPosition.getColumn());
        int signedXOffset = endPosition.getColumn() - startPosition.getColumn();
        int signedYOffset = endPosition.getRow() - startPosition.getRow();
        boolean pathClear = true;
        if (absXOffset == Math.abs(signedYOffset)) { // if the move is diagonal:
            if (signedXOffset >= 0 && signedYOffset >= 0){
                for (int i = 1; i < absXOffset; i++) { // this only checks the going up and to the right diagonal.
                    int pathX = startPosition.getColumn() + i;
                    int pathY = startPosition.getRow() + i;
                    ChessPosition pathwayPosition = new ChessPosition(pathX, pathY);
                    if (board.getPiece(pathwayPosition) != null) {
                        pathClear = false;
                    }
                }
            } else if (signedXOffset < 0 && signedYOffset >= 0){
                for (int i = 1; i < absXOffset; i++) { // this only checks the going up and to the left diagonal.
                    int pathX = startPosition.getColumn() - i;
                    int pathY = startPosition.getRow() + i;
                    ChessPosition pathwayPosition = new ChessPosition(pathX, pathY);
                    if (board.getPiece(pathwayPosition) != null) {
                        pathClear = false;
                    }
                }
            } else if (signedXOffset >= 0 && signedYOffset < 0){
                for (int i = 1; i < absXOffset; i++) { // this only checks the going down and to the right diagonal.
                    int pathX = startPosition.getColumn() + i;
                    int pathY = startPosition.getRow() - i;
                    ChessPosition pathwayPosition = new ChessPosition(pathX, pathY);
                    if (board.getPiece(pathwayPosition) != null) {
                        pathClear = false;
                    }
                }
            } else if (signedXOffset < 0 && signedYOffset < 0){
                for (int i = 1; i < absXOffset; i++) { // this only checks the going down and to the left diagonal.
                    int pathX = startPosition.getColumn() - i;
                    int pathY = startPosition.getRow() - i;
                    ChessPosition pathwayPosition = new ChessPosition(pathX, pathY);
                    if (board.getPiece(pathwayPosition) != null) {
                        pathClear = false;
                    }
                }
            }
            if(board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor()
                    .equals(board.getPiece(startPosition).getTeamColor())){
                pathClear = false;
            }
            return pathClear;
        } else {
            return false;
        }
    }

}
