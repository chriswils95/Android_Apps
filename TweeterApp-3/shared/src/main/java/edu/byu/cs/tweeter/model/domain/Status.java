package edu.byu.cs.tweeter.model.domain;

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

    private  Status(){};

    public void setUser(User user) {
        this.user = user;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status(User user, String date, String message){
        this.user = user;
        this.message = message;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Status{" +
//                "User='" + user.toString() + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
