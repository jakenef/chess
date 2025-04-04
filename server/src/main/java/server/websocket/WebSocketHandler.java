package server.websocket;

import com.google.gson.Gson;
import dataaccess.auth.AuthDataAccess;
import dataaccess.game.GameDataAccess;
import dataaccess.user.UserDataAccess;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
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

    public WebSocketHandler(UserDataAccess userDA, GameDataAccess gameDA, AuthDataAccess authDA) {
        this.userDA = userDA;
        this.gameDA = gameDA;
        this.authDA = authDA;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> {
                if (isPlayer(command)) {
                    connectAsPlayer(command, session);
                } else {
                    connectAsObserver(command, session);
                }
            }
        }
    }

    private void connectAsPlayer(UserGameCommand command, Session session) throws IOException {
        connections.add(command.getUsername(), session);
        LoadGameMessage loadGameMessage = new LoadGameMessage(command.getGameData());
        Connection clientConn = connections.get(command.getUsername());
        clientConn.send(loadGameMessage.toString());
        NotificationMessage notificationMessage = new NotificationMessage(command.getUsername() +
                " has connected as " + command.getPlayerJoinColor().toString());
        connections.broadcast(command.getUsername(), notificationMessage);
    }

    private void connectAsObserver(UserGameCommand command, Session session){
        connections.add(command.getAuthToken(), session);
        String name = command.getAuthToken();
        String gameName = command.getGameID().toString();
        NotificationMessage notificationMessage = new NotificationMessage(name + " has connected as an observer");
    }

    private String getUsername(UserGameCommand command){
        return authDA.getAuth(command.getAuthToken()).username();
    }

    private boolean isPlayer(UserGameCommand command) {
        GameData gameData = gameDA.getGame(command.getGameID());
        if(gameData.)
    }
}
