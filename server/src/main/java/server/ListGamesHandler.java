package server;

import dataaccess.DataAccessException;
import model.request.ListGameRequest;
import model.result.ListGameResult;
import service.GameService;
import spark.Request;

public class ListGamesHandler extends BaseHandler<ListGameRequest, ListGameResult>{
    private final GameService gameService;

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    protected ListGameResult processRequest(ListGameRequest request) throws DataAccessException {
        return gameService.listGames(request);
    }

    @Override
    protected ListGameRequest parseRequest(Request req) {
        String authToken = req.headers("authorization");
        return new ListGameRequest(authToken);
    }
}
