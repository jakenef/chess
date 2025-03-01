package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.request.CreateGameRequest;
import model.result.CreateGameResult;
import service.GameService;
import spark.Request;

public class CreateGameHandler extends BaseHandler<CreateGameRequest, CreateGameResult>{
    private final GameService gameService;

    public CreateGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    protected CreateGameResult processRequest(CreateGameRequest request) throws DataAccessException {
        return gameService.createGame(request);
    }

    @Override
    protected CreateGameRequest parseRequest(Request req) {
        String authToken = req.headers("authorization");
        CreateGameRequest bodyReq = new Gson().fromJson(req.body(), CreateGameRequest.class);
        return new CreateGameRequest(authToken, bodyReq.gameName());
    }
}
