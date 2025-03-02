package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.request.JoinGameRequest;
import model.result.JoinGameResult;
import service.GameService;
import spark.Request;

public class JoinGameHandler extends BaseHandler<JoinGameRequest, JoinGameResult> {
    private final GameService gameService;

    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    protected JoinGameResult processRequest(JoinGameRequest request) throws DataAccessException {
        gameService.joinGame(request);
        return new JoinGameResult();
    }

    @Override
    protected JoinGameRequest parseRequest(Request req) {
        String authToken = req.headers("authorization");
        JoinGameRequest bodyReq = new Gson().fromJson(req.body(), JoinGameRequest.class);
        return new JoinGameRequest(authToken, bodyReq.playerColor(), bodyReq.gameID());
    }
}
