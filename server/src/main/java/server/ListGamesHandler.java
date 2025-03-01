package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.request.ListGameRequest;
import model.result.ListGameResult;
import model.utils.ErrorMessage;
import model.utils.RecordUtils;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class ListGamesHandler implements BaseHandler{
    private final GameService gameService;

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request req, Response res) {
        ListGameResult listRes;
        String authToken = req.headers("authorization");
        ListGameRequest listReq = new ListGameRequest(authToken);
        if (RecordUtils.isNull(listReq)){
            res.status(400);
            ErrorMessage errMsg = new ErrorMessage("bad request");
            return errMsg.toJson();
        }

        try {
            listRes = gameService.listGames(listReq);
        } catch (DataAccessException e) {
            if(Objects.equals(e.getMessage(), "unauthorized")){
                res.status(401);
                ErrorMessage errMsg = new ErrorMessage("unauthorized");
                return errMsg.toJson();
            }else{
                res.status(500);
                ErrorMessage errMsg = new ErrorMessage(e.getMessage());
                return errMsg.toJson();
            }
        }
        res.status(200);
        return new Gson().toJson(listRes);
    }
}
