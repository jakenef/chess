package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    private ClientInterface client;
    private final String serverURL;
    private State state;

    public Repl(String serverURL) {
        this.serverURL = serverURL;
        this.state = State.SIGNED_OUT;
        this.client = new SignedOutClient();
    }

    public void setState(State newState){
        this.state = newState;
        switch (newState){
            case SIGNED_OUT -> client = new SignedOutClient();
            case SIGNED_IN -> client = new SignedInClient();
            case GAMEPLAY -> client = new GameplayClient();
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
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt(){
        System.out.print("\n" + RESET_TEXT_COLOR + state + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String getServerURL(){
        return serverURL;
    }
}
