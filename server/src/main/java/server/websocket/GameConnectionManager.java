package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GameConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String clientName, Session session) {
        Connection connection = new Connection(clientName, session);
        connections.put(clientName, connection);
    }

    public void remove(String clientName) {
        connections.remove(clientName);
    }

    public Connection get(String clientName) {
        return connections.get(clientName);
    }

    public void broadcast(String excludeClientName, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (Connection iterConnection : connections.values()) {
            if (iterConnection.session.isOpen()) {
                if (!iterConnection.clientName.equals(excludeClientName)) {
                    iterConnection.send(serverMessage.toString());
                }
            } else {
                removeList.add(iterConnection);
            }
        }
        for (Connection iterConnection : removeList) {
            connections.remove(iterConnection.clientName);
        }
    }
}
