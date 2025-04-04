package server.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final UserService userService;
    private final GameService gameService;

    public WebSocketHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException, IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> {
                if (command.getRole() == UserGameCommand.Role.PLAYER) {
                    connectAsPlayer(command, session);
                } else if (command.getRole() == UserGameCommand.Role.OBSERVER) {
                    connectAsObserver(command, session);
                } else {
                    throw new ResponseException(500, "Role not set");
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
}
