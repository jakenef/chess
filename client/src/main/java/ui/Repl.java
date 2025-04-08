package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import serverfacade.ServerFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocketfacade.WebSocketFacade;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import static ui.EscapeSequences.*;

/**
 * The `Repl` class represents a Read-Eval-Print Loop (REPL) for a chess client.
 * It handles user input, evaluates commands, and prints the results.
 */
public class Repl {
    private ClientInterface client;
    private final ServerFacade server;
    private final String serverURL;
    private WebSocketFacade ws;
    private State state;
    private String authToken = null;
    private String username = null;
    private GameData joinedGameData;
    private ChessGame.TeamColor joinedAsTeamColor;

    public Repl(String serverURL) {
        this.server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
        this.state = State.SIGNED_OUT;
        this.client = new SignedOutClient(this);
    }

    public void setState(State newState){
        this.state = newState;
        switch (newState){
            case SIGNED_OUT -> client = new SignedOutClient(this);
            case SIGNED_IN -> client = new SignedInClient(this);
            case GAMEPLAY -> client = new GameplayClient(this);
        }
    }

    /**
         * Starts the Read-Eval-Print Loop (REPL) for the chess client.
         * This method displays a welcome message, prints the help message,
         * and continuously reads user input, evaluates commands, and prints the results.
         * The loop terminates when the user inputs the "quit" command.
         */
    public void run(){
        System.out.println("♕ Welcome to my CS240 Chess Client: ");
        System.out.println(client.help());

        Scanner scanner = new Scanner(System.in);
        State previousState = state;
        var result = "";
        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try{
                result = client.eval(line);
                String formatResult = formatResult(result);
                System.out.print(SET_TEXT_COLOR_BLUE + formatResult);
                if (state != previousState && state != State.GAMEPLAY) {
                    System.out.print("\n" + SET_TEXT_COLOR_BLUE + formatResult(client.help()));
                    previousState = state;
                }
                System.out.println();
            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String formatResult(String result){
        return Arrays.stream(result.split("\n"))
                .map(mapLine -> "\t" + mapLine)
                .collect(Collectors.joining("\n"));
    }

    public ServerFacade getServer(){
        return this.server;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public WebSocketFacade getWs() {
        return ws;
    }

    public void createWebSocket() throws ResponseException {
        this.ws = new WebSocketFacade(serverURL, this);
    }

    public String getAuthToken(){
        return this.authToken;
    }

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public ChessGame.TeamColor getJoinedAsTeamColor(){
        return this.joinedAsTeamColor;
    }

    public void setJoinedAsTeamColor(ChessGame.TeamColor teamColor){
        this.joinedAsTeamColor = teamColor;
    }

    public GameData getJoinedGameData() {
        return joinedGameData;
    }

    public void setJoinedGameData(GameData joinedGameData) {
        this.joinedGameData = joinedGameData;
    }

    private void printPrompt(){
        System.out.print("\n" + SET_TEXT_COLOR_YELLOW + state + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public void printNotification(NotificationMessage notification) {
        System.out.println(SET_TEXT_COLOR_MAGENTA + notification.getMessage());
        printPrompt();
    }

    public void printError(ErrorMessage error) {
        System.out.println(SET_TEXT_COLOR_RED + error.getErrorMessage());
        printPrompt();
    }

    public void printLoadGameMessage(LoadGameMessage loadGameMessage) {
        this.joinedGameData = loadGameMessage.getGame();
        System.out.println(RESET_TEXT_COLOR + "\n" + PrintBoardHelper.getBoardString(loadGameMessage.getGame(), this));
        printPrompt();
    }
}
