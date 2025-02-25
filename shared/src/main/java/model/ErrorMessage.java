package model;

public class ErrorMessage {
    private String message;

    public ErrorMessage(String message){
        this.message = "Error: " + message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
