package edu.byu.cs.tweeter.model.service.request;

import java.io.Serializable;

public class RegisterRequest implements Serializable {
    private  String firstName;
    private  String lastName;
    private  String userName;
    private  String password;
    private String imageString;


    public void setImageString(String imageString) {
        this.imageString = imageString;
    }




    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageString() {
        return imageString;
    }

    public RegisterRequest(String firstName, String lastName, String userName, String password, String imageString){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.imageString = imageString;
    }

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    public RegisterRequest() {}


}
