package edu.byu.cs.tweeter.client.model.ServiceProxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.FollowServiceProxy;
import edu.byu.cs.tweeter.client.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowResponse;


public class FollowServiceProxyTest {

    private FollowRequest validRequest;
    private FollowRequest invalidRequest;

    private FollowResponse successResponse;
    private FollowResponse failureResponse;

    private FollowService followServiceSpy;

    /**
     * Create a FollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup request objects to use in the tests
        validRequest = new FollowRequest(currentUser, resultUser1, true);
        invalidRequest = new FollowRequest(null, null, false);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowResponse(currentUser, resultUser1);

        failureResponse = null;


        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        followServiceSpy = new FollowServiceProxy();
    }

    /**
     * Verify that for successful requests the {@link FollowService#follow(FollowRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testFollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowResponse response = followServiceSpy.follow(validRequest);
        Assertions.assertEquals(successResponse.getFollowee(), response.getFollowee());
        Assertions.assertEquals(successResponse.getFollower(), response.getFollower());

    }

    /**
     * Verify that the {@link FollowService#follow(FollowRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollow_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        FollowResponse response = followServiceSpy.follow(validRequest);
         Assertions.assertNotNull(response.getFollowee().getImageUrl());
    }

    /**
     * Verify that for failed requests the {@link FollowService#follow(FollowRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollow_invalidRequest_returnsNoFollowees() throws IOException, TweeterRemoteException {
        FollowResponse response = null;
        try {
            response = followServiceSpy.follow(invalidRequest);
        }
        catch (Exception e){
            Assertions.assertEquals(failureResponse, response);
        }
    }
}
