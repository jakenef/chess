package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    @Override
    public String toString() {
        return "Game Name: " + gameName + ", White Username: " + (whiteUsername == null ? "empty" : whiteUsername)
                + ", Black Username: " + (blackUsername == null ? "empty" : blackUsername) + ", Game ID: " + gameID +"\n";
    }
}
