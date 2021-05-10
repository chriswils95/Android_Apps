package edu.byu.cs.tweeter.client.model.service.request;

public class RegisterRequest {
    private final String firstName;
    private final String lastName;
    private final String userName;
    private final String password;
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

}
