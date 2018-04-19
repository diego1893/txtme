package com.monsordi.txtme;

/**
 * Created by diego on 16/04/18.
 */

public class Message {

    private String message;
    private String userId;
    private String date;

    public Message() {}

    public Message(String message, String userId, String date) {
        this.message = message;
        this.userId = userId;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }
}
