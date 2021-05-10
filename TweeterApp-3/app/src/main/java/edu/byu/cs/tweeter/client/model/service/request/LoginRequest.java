package edu.byu.cs.tweeter.client.model.service.request;

/**
 * Contains all the information needed to make a login request.
 */
public class LoginRequest {

    private final String username;
    private final String password;
    private final String main_username;

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
     * Returns the password of the user to be logged in by this request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }
}
