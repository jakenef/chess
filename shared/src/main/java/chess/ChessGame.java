package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
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
        if(validMoves.contains(move) && teamTurn == movePiece.getTeamColor()){
            board.addPiece(move.getEndPosition(), movePiece);
            board.addPiece(move.getStartPosition(), null);
            if(move.getPromotionPiece() != null){
                movePiece.setType(move.getPromotionPiece());
            }
            teamTurn = movePiece.getTeamColor().opposite();
        }else{
            throw new InvalidMoveException("invalid move");
        }
    }

    /**
     * Makes a move on the chessboard without validating it.
     * This method directly updates the board with the given move,
     * bypassing any checks for move validity or game rules.
     *
     * @param move the chess move to perform
     */
    public void noValMakeMove(ChessMove move){
        ChessPiece movePiece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), movePiece);
        board.addPiece(move.getStartPosition(), null);
    }

    /**
     * Retrieves all valid moves for all pieces of the specified team.
     *
     * @param teamColor the color of the team whose valid moves are to be retrieved
     * @return a list of all valid moves for the specified team
     */
    private ArrayList<ChessMove> getAllValidMoves(TeamColor teamColor) {
        ArrayList<ChessMove> allValidMovesList = new ArrayList<>();
        ArrayList<ChessPosition> allTeamPos = board.getAllTeamPositions(teamColor);
        for(ChessPosition pos : allTeamPos){
            allValidMovesList.addAll(validMoves(pos));
        }
        return allValidMovesList;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemyTeam = teamColor.opposite();
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
        if(teamTurn != teamColor){
            return false;
        }
        ArrayList<ChessPosition> allTeamPos = board.getAllTeamPositions(teamColor);
        ArrayList<ChessMove> allValidMoves = new ArrayList<>();
        //for (every position: team){
        for(ChessPosition pos : allTeamPos){
            allValidMoves.addAll(validMoves(pos));
        }
        return allValidMoves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return isInCheckmate(teamColor);
        } else {
            return false;
        }
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

    /**
     * Checks if a move will result in the current player's king being in check.
     *
     * @param move the move to test
     * @return true if the move does not result in the current player's king being in check, false otherwise
     */
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

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK,
        NONE;
        public TeamColor opposite() {
            return this == BLACK ? WHITE : BLACK;
        }
    }

    public static TeamColor fromString(String color) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }

        try {
            return TeamColor.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid color: " + color);
        }
    }
}
