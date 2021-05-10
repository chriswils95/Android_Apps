package edu.byu.cs.tweeter.client.model.service.request;

/**
 * Contains all the information needed to make a login request.
 */
public class LogoutRequest {

    private  String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private LogoutRequest(){};

    private  String token;

    /**
     * Creates an instance.
     *
     * @param username the username of the user to be logged in.
     * @param token the password of the user to be logged in.
     */
    public LogoutRequest(String username, String token) {
        this.username = username;
        this.token = token;
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
    public String getToken() {
        return token;
    }
}
