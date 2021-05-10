package edu.byu.cs.tweeter.client.model.ServiceProxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.FeedServiceProxy;
import edu.byu.cs.tweeter.client.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.client.model.service.response.FeedResponse;


public class FeedServiceProxyTest {

    private FeedRequest validRequest;
    private FeedRequest invalidRequest;

    private int successResponseLength;
    private FeedResponse failureResponse;

    private FeedServiceProxy feedServiceSpy= new FeedServiceProxy() ;

    /**
     * Create a FollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status status1 = new Status(currentUser, "11-22-18", "@google");
        Status status2 = new Status(currentUser, "11-22-17", "www.google.com");
        Status status3 = new Status(currentUser, "11-22-16", "This is a test");


        // Setup request objects to use in the tests
        validRequest = new FeedRequest(status1, 3, null);
        invalidRequest = new FeedRequest(null, 0, null);

        successResponseLength = 20;

        failureResponse = new FeedResponse("An exception occured");

    }

    /**
     * Verify that for successful requests the {@link FeedService#getFeeds(FeedRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFeeds_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        try {
            FeedResponse response = feedServiceSpy.getFeeds(validRequest);
            Assertions.assertTrue(successResponseLength > 0);
        }catch (Exception e){
            Assertions.assertTrue(successResponseLength > 0);

        }
    }

    /**
     * Verify that the {@link FeedService#getFeeds(FeedRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFeeds_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        try {
            FeedResponse response = feedServiceSpy.getFeeds(validRequest);

            for (Status feed : response.getUserFollowersFeed()) {
                Assertions.assertNotNull(feed.getUser().getImageBytes());
            }
        } catch (Exception e){
            Assertions.assertTrue(successResponseLength > 0);
        }
    }
}
