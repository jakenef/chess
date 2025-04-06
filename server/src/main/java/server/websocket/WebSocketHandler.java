package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDataAccess;
import dataaccess.game.GameDataAccess;
import dataaccess.user.UserDataAccess;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    private final ConcurrentHashMap<Integer, GameConnectionManager> gameCMList = new ConcurrentHashMap<>();
    private final UserDataAccess userDA;
    private final GameDataAccess gameDA;
    private final AuthDataAccess authDA;
    private Connection clientConnection;

    public WebSocketHandler(UserDataAccess userDA, GameDataAccess gameDA, AuthDataAccess authDA) {
        this.userDA = userDA;
        this.gameDA = gameDA;
        this.authDA = authDA;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userCommand = new Gson().fromJson(message, UserGameCommand.class);
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        this.clientConnection = new Connection(null, session);
        try {
            this.clientConnection = new Connection(getUsername(userCommand.getAuthToken()), session);
            switch (userCommand.getCommandType()) {
                case CONNECT -> {
                    if (isPlayer(userCommand)) {
                        connectAsPlayer(userCommand, session);
                    } else {
                        connectAsObserver(userCommand, session);
                    }
                }
                case MAKE_MOVE -> makeMove(makeMoveCommand);
                case LEAVE -> leave(userCommand);
                case RESIGN -> resign(userCommand);
            }
        } catch (DataAccessException | IOException | InvalidMoveException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            clientConnection.send(new Gson().toJson(errorMessage));
        }
    }

    private void resign(UserGameCommand command) throws DataAccessException, InvalidMoveException, IOException {
        GameConnectionManager gameConnManager = getGameConnManager(command);
        String rootClientUsername = getUsername(command.getAuthToken());
        if (!isPlayer(command)){
            throw new InvalidMoveException("observer cannot resign");
        }

        GameData gameData = getGameData(command.getGameID());
        ChessGame game = gameData.game();

        if(game.getTeamTurn().equals(ChessGame.TeamColor.NONE)){
            throw new InvalidMoveException("the game is already over.");
        }

        game.setTeamTurn(ChessGame.TeamColor.NONE);
        gameDA.updateGame(gameData);

        NotificationMessage notificationMessage = new NotificationMessage(rootClientUsername + " has resigned.");
        gameConnManager.broadcast(null, notificationMessage);
    }

    private void leave(UserGameCommand command) throws DataAccessException, IOException {
        GameConnectionManager gameConnManager = getGameConnManager(command);
        String rootClientUsername = getUsername(command.getAuthToken());
        gameConnManager.remove(rootClientUsername);

        if (isPlayer(command)) {
            GameData oldGameData = getGameData(command.getGameID());
            if (getRootClientTeamColor(command).equals("WHITE")){
                gameDA.updateGame(new GameData(oldGameData.gameID(), null,
                        oldGameData.blackUsername(), oldGameData.gameName(), oldGameData.game()));
            } else {
                gameDA.updateGame(new GameData(oldGameData.gameID(), oldGameData.whiteUsername(),
                        null, oldGameData.gameName(), oldGameData.game()));
            }
        }

        NotificationMessage notificationMessage = new NotificationMessage(rootClientUsername + " has left the game.");
        gameConnManager.broadcast(rootClientUsername, notificationMessage);
    }

    private void makeMove(MakeMoveCommand command) throws DataAccessException, InvalidMoveException, IOException {
        if (!isPlayer(command)){
            throw new InvalidMoveException("invalid move");
        }
        GameConnectionManager gameConnManager = getGameConnManager(command);
        String rootClientUsername = getUsername(command.getAuthToken());

        GameData gameData = getGameData(command.getGameID());
        ChessGame game = gameData.game();

        if(game.getTeamTurn().equals(ChessGame.TeamColor.NONE) || !game.getTeamTurn().toString().equals(getRootClientTeamColor(command))){
            throw new InvalidMoveException("invalid move");
        }

        game.makeMove(command.getMove());
        gameDA.updateGame(gameData);

        LoadGameMessage loadGameMessage = new LoadGameMessage(getGameData(command.getGameID()));
        gameConnManager.broadcast(null, loadGameMessage);

        NotificationMessage notificationMessage = new NotificationMessage(rootClientUsername +
                " made move: " + command.getMove().toString());
        gameConnManager.broadcast(rootClientUsername, notificationMessage);

        NotificationMessage statusNotificationMessage = null;
        if (game.isInCheck(ChessGame.TeamColor.WHITE)){
            statusNotificationMessage = new NotificationMessage(gameData.whiteUsername() + " is in check!");
        } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            statusNotificationMessage = new NotificationMessage(gameData.blackUsername() + " is in check!");
        } else if (game.isInCheckmate(ChessGame.TeamColor.WHITE)){
            statusNotificationMessage = new NotificationMessage(gameData.whiteUsername() + " is in checkmate!");
        } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)){
            statusNotificationMessage = new NotificationMessage(gameData.blackUsername() + " is in checkmate!");
        } else if (game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)){
            statusNotificationMessage = new NotificationMessage("Stalemate...");
        }
        if (statusNotificationMessage != null) {
            gameConnManager.broadcast(null, statusNotificationMessage);
        }
    }

    private void connectAsPlayer(UserGameCommand command, Session session) throws IOException, DataAccessException {
        GameConnectionManager gameConnManager = getGameConnManager(command);
        String rootClientUsername = getUsername(command.getAuthToken());
        gameConnManager.add(rootClientUsername, session);

        LoadGameMessage loadGameMessage = new LoadGameMessage(getGameData(command.getGameID()));
        clientConnection.send(new Gson().toJson(loadGameMessage));

        NotificationMessage notificationMessage = new NotificationMessage(rootClientUsername +
                " has connected as " + getRootClientTeamColor(command));
        gameConnManager.broadcast(rootClientUsername, notificationMessage);
    }

    private void connectAsObserver(UserGameCommand command, Session session) throws DataAccessException, IOException {
        GameConnectionManager gameConnManager = getGameConnManager(command);
        String rootClientUsername = getUsername(command.getAuthToken());
        gameConnManager.add(rootClientUsername, session);

        LoadGameMessage loadGameMessage = new LoadGameMessage(getGameData(command.getGameID()));
        clientConnection.send(new Gson().toJson(loadGameMessage));

        NotificationMessage notificationMessage = new NotificationMessage(rootClientUsername +
                " has connected as an observer");
        gameConnManager.broadcast(rootClientUsername, notificationMessage);
    }

    //service mini class

    private String getUsername(String authToken) throws DataAccessException {
        return authDA.getAuth(authToken).username();
    }

    private GameData getGameData(int gameID) throws DataAccessException {
        return gameDA.getGame(gameID);
    }

    private boolean isPlayer(UserGameCommand command) throws DataAccessException {
        GameData gameData = gameDA.getGame(command.getGameID());
        return getUsername(command.getAuthToken()).equals(gameData.whiteUsername())
                || getUsername(command.getAuthToken()).equals(gameData.blackUsername());
    }

    private String getRootClientTeamColor(UserGameCommand command) throws DataAccessException {
        GameData gameData = gameDA.getGame(command.getGameID());
        if (getUsername(command.getAuthToken()).equals(gameData.whiteUsername())){
            return "WHITE";
        } else {
            return "BLACK";
        }
    }

    private GameConnectionManager getGameConnManager(UserGameCommand command){
        GameConnectionManager gameConnectionManager = gameCMList.get(command.getGameID());
        if (gameConnectionManager == null) {
            gameConnectionManager = new GameConnectionManager();
            gameCMList.put(command.getGameID(), gameConnectionManager);
        }
        return gameConnectionManager;
    }
}
