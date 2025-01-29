package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for(int row = 8; row >= 1; row--){
            boardString.append("\n").append(row).append(" ");
            for(int col = 1; col <= 8; col++){
                ChessPosition position = new ChessPosition(row, col);
                if (getPiece(position) == null) {
                    boardString.append("- ");
                } else {
                    boardString.append(getPiece(position).getPieceTypeString()).append(" ");
                }
            }
        }
        boardString.append("\n  ");
        for(int i = 1; i < 9; i++){
            boardString.append(i).append(" ");
        }
        return boardString + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][8 - position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][8 - position.getColumn()];
    }

    /**
     * Checks if two pieces on the chessboard belong to the same team.
     *
     * @param board The chessboard containing the pieces.
     * @param pos1 The position of the first piece.
     * @param pos2 The position of the second piece.
     * @return True if both pieces belong to the same team, false otherwise.
     */
    public static boolean isDifferentTeam(ChessBoard board, ChessPosition pos1, ChessPosition pos2){
        return !board.getPiece(pos1).getTeamColor().equals(board.getPiece(pos2).getTeamColor());
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessBoard rBoard = new ChessBoard();
        setOneSide(rBoard, 2, 1, ChessGame.TeamColor.WHITE);
        setOneSide(rBoard, 7, 8, ChessGame.TeamColor.BLACK);
        this.board = rBoard.board;
    }

    /**
     * Sets up one side of the chessboard with pawns and royal pieces.
     *
     * @param rBoard The chessboard to set up.
     * @param pawnRow The row to place the pawns.
     * @param royalRow The row to place the royal pieces (Rook, Knight, Bishop, Queen, King).
     * @param team The color of the pieces to place.
     */
    private static void setOneSide(ChessBoard rBoard, int pawnRow, int royalRow, ChessGame.TeamColor team){
        for (int i = 1; i < 9; i++){
            ChessPosition nPos = new ChessPosition(pawnRow, i);
            ChessPiece nPiece = new ChessPiece(team, ChessPiece.PieceType.PAWN);
            rBoard.addPiece(nPos, nPiece);
        }
        for (int i = 1; i < 4; i++){
            ChessPosition nPos = new ChessPosition(royalRow, i);
            ChessPosition nPos2 = new ChessPosition(royalRow, 9-i);
            if(i == 1){
                ChessPiece nPiece = new ChessPiece(team, ChessPiece.PieceType.ROOK);
                rBoard.addPiece(nPos, nPiece);
                rBoard.addPiece(nPos2, nPiece);
            } else if(i == 2){
                ChessPiece nPiece = new ChessPiece(team, ChessPiece.PieceType.KNIGHT);
                rBoard.addPiece(nPos, nPiece);
                rBoard.addPiece(nPos2, nPiece);
            } else {
                ChessPiece nPiece = new ChessPiece(team, ChessPiece.PieceType.BISHOP);
                rBoard.addPiece(nPos, nPiece);
                rBoard.addPiece(nPos2, nPiece);
            }
        }
        ChessPosition qPos = new ChessPosition(royalRow, 4);
        ChessPosition kPos = new ChessPosition(royalRow, 5);
        ChessPiece kPiece = new ChessPiece(team, ChessPiece.PieceType.KING);
        ChessPiece qPiece = new ChessPiece(team, ChessPiece.PieceType.QUEEN);
        rBoard.addPiece(qPos, qPiece);
        rBoard.addPiece(kPos, kPiece);
    }
}