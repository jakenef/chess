package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    private final ClientInterface client;
    private final String serverURL;
    private State state;

    public Repl(String serverURL) {
        this.serverURL = serverURL;
        this.state = State.SIGNEDOUT;
        this.client = new SignedOutClient();
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
}
