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
        for(int y = 1; y <= 8; y++){
            boardString.append("\n").append(9 - y).append(" ");
            for(int x = 1; x <= 8; x++){
                ChessPosition position = new ChessPosition(x, y);
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
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessBoard newBoard = new ChessBoard();
        for (int x = 1; x < 9; x++){ // set up white pawns
            ChessPosition newPosition = new ChessPosition(x, 2);
            ChessPiece newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            newBoard.addPiece(newPosition, newPiece);
        }
        for (int x = 1; x < 9; x++){ // set up black pawns
            ChessPosition newPosition = new ChessPosition(x, 7);
            ChessPiece newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            newBoard.addPiece(newPosition, newPiece);
        }
        this.board = newBoard.board;
    }
}
