package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
    }

    public ChessGame clone(){
        ChessGame other = new ChessGame();
        other.board = this.board.clone();
        other.teamTurn = this.teamTurn;
        return other;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece startPiece = board.getPiece(startPosition);
        if (startPiece == null){
            return null;
        }
        ArrayList<ChessMove> pieceMoveList = new ArrayList<>(startPiece.pieceMoves(board, startPosition));
        ArrayList<ChessMove> validMoveList = new ArrayList<>();
        for(ChessMove move : pieceMoveList){
            if(isMoveOutOfCheck(move)){
                validMoveList.add(move);
            }
        }
        return validMoveList;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece movePiece = board.getPiece(move.getStartPosition());
        if(movePiece == null){
            throw new InvalidMoveException();
        }
        ArrayList<ChessMove> validMoves = getAllValidMoves(movePiece.getTeamColor());
        assert validMoves != null;
        if(validMoves.contains(move) && teamTurn == movePiece.getTeamColor()){
            board.addPiece(move.getEndPosition(), movePiece);
            board.addPiece(move.getStartPosition(), null);
        }else{
            throw new InvalidMoveException();
        }
    }

    public void noValMakeMove(ChessMove move){
        ChessPiece movePiece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), movePiece);
        board.addPiece(move.getStartPosition(), null);
    }

    private ArrayList<ChessMove> getAllValidMoves(TeamColor teamColor) {
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemyTeam;
        if (teamColor == TeamColor.BLACK){
            enemyTeam = TeamColor.WHITE;
        } else {
            enemyTeam = TeamColor.BLACK;
        }
        ArrayList<ChessMove> allTeamMoves = new ArrayList<>(getAllTeamPieceMoves(enemyTeam));
        for(ChessMove move : allTeamMoves){
            if(board.getPiece(move.getEndPosition()) != null
                    && board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //for (every position: team){
        // addall(validmoves(pos))
        // if (validMovesList == empty){
        //return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public boolean isMoveOutOfCheck(ChessMove move){
        //create new chessGame, copy of this game
        ChessGame testGame = this.clone();
        //make move in the new game
        testGame.noValMakeMove(move);
        return !testGame.isInCheck(testGame.board.getPiece(move.getEndPosition()).getTeamColor());
    }

    /**
     * Gets all possible moves for all pieces of the specified team.
     *
     * @param team the team whose moves are to be retrieved
     * @return a collection of all possible moves for the specified team
     */
    public Collection<ChessMove> getAllTeamPieceMoves(TeamColor team){
        ArrayList<ChessMove> allTeamMoveList = new ArrayList<>();
        ArrayList<ChessPosition> allTeamPositionList = board.getAllTeamPositions(team);
        for (ChessPosition pos : allTeamPositionList){
            ChessPiece iteratorPiece = board.getPiece(pos);
            allTeamMoveList.addAll(iteratorPiece.pieceMoves(board, pos));
        }
        return allTeamMoveList;
    }
}
