package edu.byu.cs.tweeter.client.model.service.response;

import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.User;

/**
 * A response for a {@link edu.byu.cs.tweeter.client.model.service.request.LoginRequest}.
 */
public class LogoutResponse extends Response {

    private AuthToken authToken;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public LogoutResponse(String message) {
        super(false, message);
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

    /**
     * Returns the auth token.
     *
     * @return the auth token.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }
}
