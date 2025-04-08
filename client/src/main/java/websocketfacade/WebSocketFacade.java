package websocketfacade;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.Repl;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The `WebSocketFacade` class is responsible for managing the WebSocket connection
 * and communication between the client and the server. It handles sending commands
 * and receiving messages related to the chess game.
 */
public class WebSocketFacade extends Endpoint {
    Session session;
    Repl repl;

    /**
         * Constructs a new `WebSocketFacade` instance and establishes a WebSocket connection to the specified URL.
         *
         * @param url  the URL of the WebSocket server
         * @param repl the `Repl` instance to handle received messages
         * @throws ResponseException if an error occurs while establishing the WebSocket connection
         */
    public WebSocketFacade(String url, Repl repl) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.repl = repl;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);

                switch (serverMessage.getServerMessageType()){
                    case NOTIFICATION -> repl.printNotification(notificationMessage);
                    case ERROR -> repl.printError(errorMessage);
                    case LOAD_GAME -> repl.printLoadGameMessage(loadGameMessage);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
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

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        MakeMoveCommand makeMoveCommand = new MakeMoveCommand(authToken, gameID, move);
        try {
            session.getBasicRemote().sendText(new Gson().toJson(makeMoveCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
