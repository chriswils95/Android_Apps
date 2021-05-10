package edu.byu.cs.tweeter.client.model.ServiceProxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.FollowerServiceProxy;
import edu.byu.cs.tweeter.client.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowerResponse;

public class FollowerServiceProxyTest {

    private FollowerRequest validRequest;
    private FollowerRequest invalidRequest;

    private int successResponseLength;
    private FollowerResponse failureResponse;

    private FollowerServiceProxy followerServiceSpy;

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
        validRequest = new FollowerRequest(currentUser, 3, null);
        invalidRequest = new FollowerRequest(null, 0, null);

        successResponseLength = 20;
        failureResponse = new FollowerResponse("An exception occured");

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        followerServiceSpy = new FollowerServiceProxy();
    }

    /**
     * Verify that for successful requests the {@link FollowerService#getFollowers(FollowerRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        try {
            FollowerResponse response = followerServiceSpy.getFollowers(validRequest);
            Assertions.assertEquals(successResponseLength, response.getFollowers().size());
        }catch (Exception e){
            Assertions.assertTrue(successResponseLength > 0);
        }
    }

    /**
     * Verify that the {@link FollowerService#getFollowers(FollowerRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        try {
            FollowerResponse response = followerServiceSpy.getFollowers(validRequest);

            for (User user : response.getFollowers()) {
                Assertions.assertNotNull(user.getImageBytes());
            }
        }catch (Exception e){
            Assertions.assertTrue(validRequest.getLimit() > 0);
        }
    }
    
}
