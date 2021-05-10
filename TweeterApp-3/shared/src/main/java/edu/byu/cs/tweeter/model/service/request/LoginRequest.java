package edu.byu.cs.tweeter.model.service.request;

/**
 * Contains all the information needed to make a login request.
 */
public class LoginRequest {

    private String username;
    private String password;

    public String getMain_username() {
        return main_username;
    }

    public void setMain_username(String main_username) {
        this.main_username = main_username;
    }

    private  String main_username;


    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    public LoginRequest() {}

    /**
     * Creates an instance.
     *
     * @param username the username of the user to be logged in.
     * @param password the password of the user to be logged in.
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
        main_username = "";
    }

    /**
     * Creates an instance.
     *
     * @param username the username of the user to be logged in.
     * @param password the password of the user to be logged in.
     */
    public LoginRequest(String username, String password, String main_username) {
        this.username = username;
        this.password = password;
        this.main_username = main_username;
    }

    /**
     * Returns the username of the user to be logged in by this request.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Returns the password of the user to be logged in by this request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
