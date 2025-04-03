package server.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

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
    public void onMessage(Session session, String message) throws ResponseException {
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

    private void connectAsPlayer(UserGameCommand command, Session session){
        connections.add(command.getAuthToken(), session);
        String name = command.getAuthToken(); // how to get from authtoken to name from here?
        String gameName = command.getGameID().toString(); // same ^
        NotificationMessage notificationMessage = new NotificationMessage(name + " has connected as [color]");
    }

    private void connectAsObserver(UserGameCommand command, Session session){
        connections.add(command.getAuthToken(), session);
        String name = command.getAuthToken();
        String gameName = command.getGameID().toString();
        NotificationMessage notificationMessage = new NotificationMessage(name + " has connected as [color]");
    }
}
