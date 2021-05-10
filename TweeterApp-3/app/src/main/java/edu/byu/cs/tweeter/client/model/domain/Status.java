package edu.byu.cs.tweeter.client.model.domain;

import java.io.Serializable;

public class Status implements Serializable {
    User user;
    String date;

    public String getDate() {
        return date;
    }

    String message;


    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }


    public Status(User user,String date, String message){
        this.user = user;
        this.message = message;
        this.date = date;
    }

}
