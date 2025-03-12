package dataaccess.game;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static dataaccess.game.MemoryGameDataAccess.isTeamAvailable;
import static dataaccess.user.SQLUserDataAccess.isDatabaseEmpty;

public class SQLGameDataAccess implements GameDataAccess{
    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "DELETE FROM game";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        if (gameName == null || gameName.isEmpty()){
            throw new DataAccessException("bad request");
        }
        var conn = DatabaseManager.getConnection();
        var json = new Gson().toJson(new ChessGame());
        var statement = "INSERT INTO game (gameName, json) VALUES (?, ?)";
        try {
            var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, gameName);
            ps.setString(2, json);
            ps.executeUpdate();
            var rs = ps.getGeneratedKeys();
            if (rs.next()){
                int gameID = rs.getInt(1);
                return new GameData(gameID, null, null, gameName, new ChessGame());
            } else {
                throw new DataAccessException("database error: unable to return generated key");
            }
        } catch (SQLException e) {
            throw new DataAccessException("database error: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> getAllGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        var conn = DatabaseManager.getConnection();
        var statement = "SELECT * FROM game";
        try {
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery();
            while(rs.next()){
                games.add(readGameFromRS(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("database error: " + e.getMessage());
        }
        return games;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if (gameID < 0) {
            throw new DataAccessException("bad request");
        }
        var conn = DatabaseManager.getConnection();
        var statement = "SELECT gameID, gameName, whiteUsername, blackUsername, json FROM game WHERE gameID = ? LIMIT 1";
        try {
            var ps = conn.prepareStatement(statement);
            ps.setInt(1, gameID);
            var rs = ps.executeQuery();
            if (rs.next()){
                return readGameFromRS(rs);
            } else {
                throw new DataAccessException("bad request");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void joinGame(int gameID, String teamColor, String username) throws DataAccessException {
        if (teamColor == null || teamColor.isEmpty() || username == null || username.isEmpty()
                || (!teamColor.equals("WHITE") && !teamColor.equals("BLACK"))){
            throw new DataAccessException("bad request");
        }
        GameData existingGame = getGame(gameID);
        if (isTeamAvailable(teamColor, existingGame)){
            String column = teamColor.equals("WHITE") ? "whiteUsername" : "blackUsername";
            var conn = DatabaseManager.getConnection();
            var statement = "UPDATE game SET " + column + " = ? WHERE gameID = ?";
            try {
                var ps = conn.prepareStatement(statement);
                ps.setString(1, username);
                ps.setInt(2, gameID);
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0){
                    throw new DataAccessException("bad request");
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } else {
            throw new DataAccessException("already taken");
        }
    }

    @Override
    public boolean isEmpty() {
        String statement = "SELECT COUNT(*) FROM game";
        return isDatabaseEmpty(statement);
    }

    private static GameData readGameFromRS(ResultSet rs) throws SQLException {
        String gameName = rs.getString("gameName");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        int gameID = rs.getInt("gameID");
        ChessGame game = new Gson().fromJson(rs.getString("json"), ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
