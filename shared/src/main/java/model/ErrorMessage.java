package model;

import com.google.gson.Gson;

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

    public String toJson() {
        return new Gson().toJson(this);
    }
}
