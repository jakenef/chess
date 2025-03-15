package ui;

public class Repl {
    private final SignedOutClient signedOutClient;
    private final SignedInClient signedInClient;
    private final GameplayClient gameplayClient;


    public Repl(String serverURL) {
        this.signedOutClient = new SignedOutClient();
        this.signedInClient = new SignedInClient();
        this.gameplayClient = new GameplayClient();
    }


}
