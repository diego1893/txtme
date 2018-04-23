package com.monsordi.txtme;

/**
 * Created by diego on 16/04/18.
 */

public class Message {

    private String message;
    private String userId;
    private String date;
    private String autor;

    public Message() {}

    public Message(String message, String userId, String date, String autor) {
        this.message = message;
        this.userId = userId;
        this.date = date;
        this.autor = autor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

}
