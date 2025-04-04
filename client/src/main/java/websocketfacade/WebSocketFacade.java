package websocketfacade;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import ui.Repl;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    Repl notificationRepl;

    public WebSocketFacade(String url, Repl notificationRepl) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationRepl = notificationRepl;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                    notificationRepl.notify(notificationMessage);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connectAsPlayer(GameData gameData, String username, ChessGame.TeamColor teamColor) throws ResponseException {
        try {
            UserGameCommand connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                    username, gameData, UserGameCommand.Role.PLAYER, teamColor);
            System.out.println("Session open: " + session.isOpen());
            this.session.getBasicRemote().sendText(new Gson().toJson(connectCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
