package edu.byu.cs.tweeter.model.service.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * A response for a {@link edu.byu.cs.tweeter.model.service.request.LogoutRequest}.
 */
public class LogoutResponse extends Response {

    private User user;
    private AuthToken authToken;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public LogoutResponse(String message) {
        super(false, message);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param authToken the auth token representing this user's session with the server.
     */
    public LogoutResponse(AuthToken authToken) {
        super(true, null);
        this.authToken = authToken;
    }

    /**
     * Returns the logged in user.
     *
     * @return the user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the auth token.
     *
     * @return the auth token.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }
}
