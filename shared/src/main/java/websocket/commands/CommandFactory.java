package websocket.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class CommandFactory {

    public static Object fromJson(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        if (!jsonObject.has("commandType")) {
            throw new JsonParseException("Missing commandType in JSON.");
        }

        String commandType = jsonObject.get("commandType").getAsString();

        if (commandType.equals("MAKE_MOVE")) {
            return new Gson().fromJson(jsonObject, MakeMoveCommand.class);
        } else {
            return new Gson().fromJson(jsonObject, UserGameCommand.class);
        }
    }
}
