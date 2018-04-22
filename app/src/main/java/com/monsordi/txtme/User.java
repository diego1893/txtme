package com.monsordi.txtme;

import java.io.Serializable;

/**
 * Created by diego on 17/04/18.
 */

public class User implements Serializable {

    private String uId;
    private String name;
    private String userName;
    private String email;
    private String phone;
    private String imageString;

    public User() {}

    public User(String uId,String name, String userName, String email, String phone, String imageString) {
        this.uId = uId;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.imageString = imageString;
    }

    public User(String uId, String name, String userName, String email, String phone) {
        this.uId = uId;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
    }

    public String getuId() {
        return uId;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageString() {
        return imageString;
    }
}
