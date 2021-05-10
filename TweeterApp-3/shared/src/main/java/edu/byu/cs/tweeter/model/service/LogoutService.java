package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

/**
 * Contains the business logic to support the login operation.
 */
public interface LogoutService {

    public LogoutResponse logout(LogoutRequest request);

    public void destroyAuthToken(AuthToken authToken);

}
