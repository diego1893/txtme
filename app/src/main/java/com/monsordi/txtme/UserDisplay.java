package com.monsordi.txtme;

import java.io.Serializable;

/**
 * Created by diego on 17/04/18.
 */

public class UserDisplay implements Serializable {

    private String user;
    private String message;
    private String hour;
    private String imageUrl;

    public UserDisplay() {}

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public String getHour() {
        return hour;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
