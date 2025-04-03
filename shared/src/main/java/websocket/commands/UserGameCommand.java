package websocket.commands;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private final CommandType commandType;

    private final String authToken;

    private final Integer gameID;

    private final Role role;

    private final ChessGame.TeamColor playerJoinColor;

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID,
                           Role role, ChessGame.TeamColor playerJoinColor) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.role = role;
        this.playerJoinColor = (role == Role.PLAYER) ? playerJoinColor : null;
    }

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID) {
        this(commandType, authToken, gameID, null, null);
    }

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID, Role role) {
        this(commandType, authToken, gameID, role, null);
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public enum Role { PLAYER, OBSERVER }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGameCommand)) {
            return false;
        }
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }
}
