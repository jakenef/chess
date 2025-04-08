package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public ChessPiece clone(){
        return new ChessPiece(pieceColor, type);
    }

    public ChessPiece(ChessPiece other) {
        this.pieceColor = other.pieceColor;
        this.type = other.type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    public static PieceType fromString(String pieceString) {
        if (pieceString == null) {
            return null;
        }
        try {
            return PieceType.valueOf(pieceString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * @return which type of chess piece this piece is in short string format
     */
    public String getPieceTypeString() {
        String pieceStr = "";
        if(pieceColor == ChessGame.TeamColor.WHITE){
            pieceStr += "W";
        }else{
            pieceStr += "B";
        }
        if(type == PieceType.QUEEN){
            pieceStr += "Q";
        }
        if(type == PieceType.PAWN){
            pieceStr += "P";
        }
        if(type == PieceType.KING){
            pieceStr += "K";
        }
        if(type == PieceType.BISHOP){
            pieceStr += "B";
        }
        if(type == PieceType.KNIGHT){
            pieceStr += "N";
        }
        if(type == PieceType.ROOK){
            pieceStr += "R";
        }
        return pieceStr;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (this.type) {
            case null -> null;
            case KING -> KingMovesCalculator.calculateKingMoves(board, myPosition);
            case QUEEN -> RecursiveMovesCalculator.calculateQueenMoves(board, myPosition);
            case BISHOP -> RecursiveMovesCalculator.calculateBishopMoves(board, myPosition);
            case KNIGHT -> KnightMovesCalculator.calculateKnightMoves(board, myPosition);
            case ROOK -> RecursiveMovesCalculator.calculateRookMoves(board, myPosition);
            case PAWN -> PawnMovesCalculator.calculatePawnMoves(board, myPosition,
                    board.getPiece(myPosition).getTeamColor());
        };
    }
}
