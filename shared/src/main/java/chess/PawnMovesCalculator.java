package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {
    private static final int[] xMoves = {0, 0, -1, 1};
    private static final int[] yMoves = {1, 2, 1, 1};

    /**
     * Calculates all possible moves for a pawn from a given position on the board.
     * Considers normal moves, initial two-step moves, attack moves, and promotions.
     *
     * @param board The chessboard.
     * @param position The starting position of the pawn.
     * @param pieceColor The color of the pawn.
     * @return A collection of possible moves for the pawn.
     */
    public static Collection<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition position,
                                                           ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> possibleMoveList = new ArrayList<>();
        possibleMoveList.addAll(checkNormalAndInitialMoves(board, position, pieceColor));
        possibleMoveList.addAll(checkAttackMoves(board, position, pieceColor));
        checkPromotion(possibleMoveList, pieceColor);
        return possibleMoveList;
    }

    /**
     * Checks and returns all possible attack moves for a pawn from a given position on the board.
     * Considers the direction of the pawn based on its color.
     *
     * @param board The chessboard.
     * @param position The starting position of the pawn.
     * @param pieceColor The color of the pawn.
     * @return A collection of possible attack moves for the pawn.
     */
    public static Collection<ChessMove> checkAttackMoves(ChessBoard board, ChessPosition position,
                                                         ChessGame.TeamColor pieceColor){
        ArrayList<ChessMove> possibleMoveList = new ArrayList<>();
        int teamDir = 1;
        if(pieceColor == ChessGame.TeamColor.BLACK){
            teamDir = -1;
        }
        // first, check the attack West move
        if (ChessPosition.isInBounds(position.getRow() + (teamDir * yMoves[2]),
                position.getColumn() + xMoves[2])){
            ChessPosition possiblePosition = new ChessPosition(position.getRow() + (teamDir * yMoves[2]),
                    position.getColumn() + xMoves[2]);
            if(board.getPiece(possiblePosition) != null
                    && !board.getPiece(possiblePosition).getTeamColor().equals(pieceColor)){
                ChessMove newMove = new ChessMove(position, possiblePosition, null);
                possibleMoveList.add(newMove);
            }
        }
        // next, check the attack East move
        if (ChessPosition.isInBounds(position.getRow() + (teamDir * yMoves[3]),
                position.getColumn() + xMoves[3])){
            ChessPosition possiblePosition = new ChessPosition(position.getRow() + (teamDir * yMoves[3]),
                    position.getColumn() + xMoves[3]);
            if(board.getPiece(possiblePosition) != null
                    && !board.getPiece(possiblePosition).getTeamColor().equals(pieceColor)){
                ChessMove newMove = new ChessMove(position, possiblePosition, null);
                possibleMoveList.add(newMove);
            }
        }
        return possibleMoveList;
    }

    /**
     * Checks and returns all possible normal and initial two-step moves for a pawn from a given position on the board.
     * Considers the direction of the pawn based on its color.
     *
     * @param board The chessboard.
     * @param position The starting position of the pawn.
     * @param pieceColor The color of the pawn.
     * @return A collection of possible normal and initial two-step moves for the pawn.
     */
    public static Collection<ChessMove> checkNormalAndInitialMoves(ChessBoard board, ChessPosition position,
                                                              ChessGame.TeamColor pieceColor){
        ArrayList<ChessMove> possibleMoveList = new ArrayList<>();
        int teamDir = 1;
        if(pieceColor == ChessGame.TeamColor.BLACK){
            teamDir = -1;
        }
        boolean normalMove = false;
        //first, check normal move
        if (ChessPosition.isInBounds(position.getRow() + (teamDir * yMoves[0]),
                position.getColumn() + xMoves[0])){
            ChessPosition possiblePosition = new ChessPosition(position.getRow() + (teamDir * yMoves[0]),
                    position.getColumn() + xMoves[0]);
            if(board.getPiece(possiblePosition) == null){
                normalMove = true;
                ChessMove newMove = new ChessMove(position, possiblePosition, null);
                possibleMoveList.add(newMove);
            }
        }
        //next, check initial two-step move
        if (ChessPosition.isInBounds(position.getRow() + (teamDir * yMoves[1]),
                position.getColumn() + xMoves[1])){
            ChessPosition possiblePosition = new ChessPosition(position.getRow() + (teamDir * yMoves[1]),
                    position.getColumn() + xMoves[1]);
            if (pieceColor == ChessGame.TeamColor.WHITE){
                if(board.getPiece(possiblePosition) == null && position.getRow() == 2 && normalMove){
                    ChessMove newMove = new ChessMove(position, possiblePosition, null);
                    possibleMoveList.add(newMove);
                }
            } else {
                if(board.getPiece(possiblePosition) == null && position.getRow() == 7 && normalMove){
                    ChessMove newMove = new ChessMove(position, possiblePosition, null);
                    possibleMoveList.add(newMove);
                }
            }
        }
        return possibleMoveList;
    }

    /**
     * Checks for pawn promotion and adds promotion moves to the list of possible moves.
     * Considers the promotion to Queen, Rook, Knight, and Bishop.
     *
     * @param possibleMoves The collection of possible moves for the pawn.
     * @param pieceColor The color of the pawn.
     */
    public static void checkPromotion(Collection<ChessMove> possibleMoves, ChessGame.TeamColor pieceColor){
        ArrayList<ChessMove> addedPromotionMoves = new ArrayList<>();
        for(ChessMove move : possibleMoves){
            if(pieceColor == ChessGame.TeamColor.WHITE){
                if(move.getEndPosition().getRow() == 8){
                    addPromotionMoves(addedPromotionMoves, move);
                }
            } else {
                if(move.getEndPosition().getRow() == 1){
                    addPromotionMoves(addedPromotionMoves, move);
                }
            }
        }
        possibleMoves.addAll(addedPromotionMoves);
    }

    /**
     * Adds promotion moves to the list of possible moves.
     * Considers the promotion to Queen, Rook, Knight, and Bishop.
     *
     * @param addedPromotionMoves The list to add promotion moves to.
     * @param move The original move that results in a promotion.
     */
    private static void addPromotionMoves(ArrayList<ChessMove> addedPromotionMoves, ChessMove move) {
        move.setPromotionPiece(ChessPiece.PieceType.QUEEN);
        ChessMove move2 = new ChessMove(move);
        move2.setPromotionPiece(ChessPiece.PieceType.ROOK);
        addedPromotionMoves.add(move2);
        ChessMove move3 = new ChessMove(move);
        move3.setPromotionPiece(ChessPiece.PieceType.KNIGHT);
        addedPromotionMoves.add(move3);
        ChessMove move4 = new ChessMove(move);
        move4.setPromotionPiece(ChessPiece.PieceType.BISHOP);
        addedPromotionMoves.add(move4);
    }
}