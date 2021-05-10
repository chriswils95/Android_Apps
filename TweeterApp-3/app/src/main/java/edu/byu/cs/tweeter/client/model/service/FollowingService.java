package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

/**
 * Contains the business logic for getting the users a user is following.
 */
public interface FollowingService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link ServerFacade} to
     * get the followees from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) throws IOException, TweeterRemoteException;

    /**
     * Loads the profile image data for each followee included in the response.
     *
     * @param response the response from the followee request.
     */
    public void loadImages(FollowingResponse response) throws IOException;

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
     * method to allow for proper mocking.
     *
     * @return the instance.
     */
    public ServerFacade getServerFacade();
}
