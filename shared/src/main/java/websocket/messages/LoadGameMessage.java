package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {

    final GameData gameData;

    public LoadGameMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.gameData = game;
    }

    public GameData getGame() {
        return gameData;
    }
}
