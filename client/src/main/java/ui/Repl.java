package ui;

import chess.ChessGame;
import model.GameData;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import static ui.EscapeSequences.*;

public class Repl {
    private ClientInterface client;
    private final ServerFacade server;
    private State state;
    private String authToken = null;
    private GameData joinedGameData;
    private ChessGame.TeamColor joinedAsTeamColor;

    public Repl(String serverURL) {
        this.server = new ServerFacade(serverURL);
        this.state = State.SIGNED_OUT;
        this.client = new SignedOutClient(this);
    }

    public void setState(State newState){
        this.state = newState;
        switch (newState){
            case SIGNED_OUT -> {
                client = new SignedOutClient(this);
                System.out.println(RESET_TEXT_COLOR + client.help());
            }
            case SIGNED_IN -> {
                client = new SignedInClient(this);
                System.out.println(RESET_TEXT_COLOR + client.help());
            }
            case GAMEPLAY -> {
                client = new GameplayClient(this);
                System.out.println(RESET_TEXT_COLOR + client.help());
            }
        }
    }

    public void run(){
        System.out.println("♕ Welcome to my CS240 Chess Client: ");
        System.out.println(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try{
                result = client.eval(line);
                String formatResult = Arrays.stream(result.split("\n"))
                        .map(mapLine -> "\t" + mapLine)
                        .collect(Collectors.joining("\n"));
                if (state == State.GAMEPLAY){
                    System.out.print(RESET_TEXT_COLOR + formatResult);
                } else {
                    System.out.print(SET_TEXT_COLOR_BLUE + formatResult);
                }
            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public ServerFacade getServer(){
        return this.server;
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
}
