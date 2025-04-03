package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connectAsPlayer(command, session); // how to differentiate between connect as observer and as player?
        }
    }

    private void connectAsPlayer(UserGameCommand command, Session session){
        connections.add(command.getAuthToken(), session);
        String name = command.getAuthToken(); // how to get from authtoken to name from here?
        String gameName = command.getGameID().toString(); // same ^
        NotificationMessage notificationMessage = new NotificationMessage(name + " has connected as [color]");
    }
}
