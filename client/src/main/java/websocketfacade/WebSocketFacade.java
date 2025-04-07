package websocketfacade;

import com.google.gson.Gson;
import exception.ResponseException;
import ui.Repl;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    Repl repl;

    public WebSocketFacade(String url, Repl repl) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.repl = repl;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                    ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                    LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                    switch (serverMessage.getServerMessageType()){
                        case NOTIFICATION -> repl.printNotification(notificationMessage);
                        case ERROR -> repl.printError(errorMessage);
                        case LOAD_GAME -> repl.printLoadGameMessage(loadGameMessage);
                    }
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("Websocket connected.");
    }

    public void connect(String authToken, int gameID) throws ResponseException {
        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        try {
            session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws ResponseException {
        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        try {
            session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        try {
            session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
