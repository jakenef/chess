package ui;

import exception.ResponseException;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;

import java.util.Arrays;

/**
 * The `SignedOutClient` class implements the `ClientInterface` and provides methods
 * to handle various client operations such as registering, logging in, and quitting.
 * It is used when the client is in a signed-out state.
 */
public class SignedOutClient implements ClientInterface{
    private final Repl repl;

    public SignedOutClient(Repl repl) {
        this.repl = repl;
    }

    /**
         * Evaluates the given input command and executes the corresponding action.
         *
         * @param input The input command to evaluate.
         * @return The result of the executed command.
         */
    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

    /**
         * Registers a new user with the specified username, password, and email.
         *
         * @param params The parameters for the register command. Expected format: [USERNAME, PASSWORD, EMAIL].
         * @return A message indicating the result of the registration.
         * @throws ResponseException If the parameters are invalid or the registration fails.
         */
    public String register(String... params) throws ResponseException {
        if (params.length != 3){
            throw new ResponseException(400, "Expected: register <USERNAME> <PASSWORD> <EMAIL>");
        }
        RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
        RegisterResult result = repl.getServer().register(request);
        repl.setAuthToken(result.authToken());
        repl.setState(State.SIGNED_IN);
        return "Successfully registered user: " + result.username();
    }

    /**
     * Logs in a user with the specified username and password.
     *
     * @param params The parameters for the login command. Expected format: [USERNAME, PASSWORD].
     * @return A message indicating the result of the login.
     * @throws ResponseException If the parameters are invalid or the login fails.
     */
    public String login(String... params) throws ResponseException {
        if (params.length != 2){
            throw new ResponseException(400, "Expected: login <USERNAME> <PASSWORD>");
        }
        LoginRequest request = new LoginRequest(params[0], params[1]);
        LoginResult result;
        try {
            result = repl.getServer().login(request);
        } catch (ResponseException e) {
            throw new ResponseException(e.statusCode(), "Username or password incorrect. Try again.");
        }
        repl.setAuthToken(result.authToken());
        repl.setState(State.SIGNED_IN);
        return "Successfully signed in user: " + result.username();
    }

    @Override
    public String help() {
        return """
        register <USERNAME> <PASSWORD> <EMAIL> - to create an account
        login <USERNAME> <PASSWORD> - to play chess
        quit - playing chess
        help - display possible commands""";
    }
}
