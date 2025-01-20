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
        for(int x = 1; x <= 8; x++){
            boardString.append("\n").append(9 - x).append(" ");
            for(int y = 1; y <= 8; y++){
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
     * Checks if two pieces on the chessboard belong to the same team.
     *
     * @param board The chessboard containing the pieces.
     * @param pos1 The position of the first piece.
     * @param pos2 The position of the second piece.
     * @return True if both pieces belong to the same team, false otherwise.
     */
    public static boolean isSameTeam(ChessBoard board, ChessPosition pos1, ChessPosition pos2){
        return board.getPiece(pos1).getTeamColor().equals(board.getPiece(pos2).getTeamColor());
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessBoard newBoard = new ChessBoard();
        for (int x = 1; x < 9; x++){ // set up white pawns
            ChessPosition newPosition = new ChessPosition(2, x);
            ChessPiece newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            newBoard.addPiece(newPosition, newPiece);
        }
        for (int x = 1; x < 9; x++){ // set up black pawns
            ChessPosition newPosition = new ChessPosition(7, x);
            ChessPiece newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            newBoard.addPiece(newPosition, newPiece);
        }
        for (int x = 1; x < 4; x++){ // set up white other pieces
            ChessPosition leftPos = new ChessPosition(1, x);
            ChessPosition rightPos = new ChessPosition(1, 9-x);
            ChessPiece newPiece = null;
            if (x == 1) {
                newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            }
            if (x == 2){
                newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            }
            if(x == 3){
                newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            }
            newBoard.addPiece(leftPos, newPiece);
            newBoard.addPiece(rightPos, newPiece);
        }
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPosition queenSpot = new ChessPosition(1, 4);
        ChessPosition kingSpot = new ChessPosition(1, 5);
        newBoard.addPiece(queenSpot, whiteQueen);
        newBoard.addPiece(kingSpot, whiteKing);

        for (int x = 1; x < 4; x++){ // set up black other pieces
            ChessPosition leftPos = new ChessPosition(8, x);
            ChessPosition rightPos = new ChessPosition(8, 9-x);
            ChessPiece newPiece = null;
            if (x == 1) {
                newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            }
            if (x == 2){
                newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            }
            if(x == 3){
                newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            }
            newBoard.addPiece(leftPos, newPiece);
            newBoard.addPiece(rightPos, newPiece);
        }
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPosition bQueenSpot = new ChessPosition(8, 4);
        ChessPosition bKingSpot = new ChessPosition(8, 5);
        newBoard.addPiece(bQueenSpot, blackQueen);
        newBoard.addPiece(bKingSpot, blackKing);

        this.board = newBoard.board;
    }
}
