package edu.byu.cs.tweeter.client.model.service;

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
import edu.byu.cs.tweeter.client.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.client.model.service.response.FeedResponse;


public class FeedServiceTest {

    private FeedRequest validRequest;
    private FeedRequest invalidRequest;

    private FeedResponse successResponse;
    private FeedResponse failureResponse;

    private FeedService feedServiceSpy;

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

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FeedResponse(Arrays.asList(status1, status2, status3), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFeeds(validRequest, "/getfeeds")).thenReturn(successResponse);

        failureResponse = new FeedResponse("An exception occured");
        Mockito.when(mockServerFacade.getFeeds(invalidRequest, "/getfeeds")).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        feedServiceSpy = Mockito.spy(new FeedServiceProxy());
        Mockito.when(feedServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
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
        FeedResponse response = feedServiceSpy.getFeeds(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that the {@link FeedService#getFeeds(FeedRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFeeds_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        FeedResponse response = feedServiceSpy.getFeeds(validRequest);

        for(Status feed : response.getUserFollowersFeed()) {
            Assertions.assertNotNull(feed.getUser().getImageBytes());
        }
    }

    /**
     * Verify that for failed requests the {@link FeedService#getFeeds(FeedRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFeeds_invalidRequest_returnsNoFeeds() throws IOException, TweeterRemoteException {
        FeedResponse response = feedServiceSpy.getFeeds(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
