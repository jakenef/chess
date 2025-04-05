package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDataAccess;
import dataaccess.game.GameDataAccess;
import dataaccess.user.UserDataAccess;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
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
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        this.clientConnection = new Connection(null, session);
        try {
            this.clientConnection = new Connection(getUsername(command), session);
            switch (command.getCommandType()) {
                case CONNECT -> {
                    if (isPlayer(command)) {
                        connectAsPlayer(command, session);
                    } else {
                        connectAsObserver(command, session);
                    }
                }
            }
        } catch (DataAccessException | IOException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            clientConnection.send(new Gson().toJson(errorMessage));
        }
    }

    private void connectAsPlayer(UserGameCommand command, Session session) throws IOException, DataAccessException {
        GameConnectionManager gameConnManager = getGameConnManager(command);
        String rootClientUsername = getUsername(command);
        gameConnManager.add(rootClientUsername, session);

        LoadGameMessage loadGameMessage = new LoadGameMessage(getGameData(command));
        clientConnection.send(new Gson().toJson(loadGameMessage));

        NotificationMessage notificationMessage = new NotificationMessage(rootClientUsername +
                " has connected as " + getPlayerJoinColor(command));
        gameConnManager.broadcast(rootClientUsername, notificationMessage);
    }

    private void connectAsObserver(UserGameCommand command, Session session) throws DataAccessException, IOException {
        GameConnectionManager gameConnManager = getGameConnManager(command);
        String rootClientUsername = getUsername(command);
        gameConnManager.add(rootClientUsername, session);

        LoadGameMessage loadGameMessage = new LoadGameMessage(getGameData(command));
        clientConnection.send(new Gson().toJson(loadGameMessage));

        NotificationMessage notificationMessage = new NotificationMessage(rootClientUsername +
                " has connected as an observer");
        gameConnManager.broadcast(rootClientUsername, notificationMessage);
    }

    private String getUsername(UserGameCommand command) throws DataAccessException {
        return authDA.getAuth(command.getAuthToken()).username();
    }

    private GameData getGameData(UserGameCommand command) throws DataAccessException {
        return gameDA.getGame(command.getGameID());
    }

    private boolean isPlayer(UserGameCommand command) throws DataAccessException {
        GameData gameData = gameDA.getGame(command.getGameID());
        return getUsername(command).equals(gameData.whiteUsername())
                || getUsername(command).equals(gameData.blackUsername());
    }

    private String getPlayerJoinColor(UserGameCommand command) throws DataAccessException {
        GameData gameData = gameDA.getGame(command.getGameID());
        if (getUsername(command).equals(gameData.whiteUsername())){
            return "WHITE";
        } else {
            return "BLACK";
        }
    }

    private GameConnectionManager getGameConnManager(UserGameCommand command){
        GameConnectionManager gameConnectionManager = gameCMList.get(command.getGameID());
        if (gameConnectionManager != null){
            return gameConnectionManager;
        } else {
            gameConnectionManager = new GameConnectionManager();
            gameCMList.put(command.getGameID(), gameConnectionManager);
            return gameConnectionManager;
        }
    }
}
