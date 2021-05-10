package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StoryService;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.StoryDA0;


public class StoryServiceImplTest {

    private StoryRequest validRequest;
    private StoryRequest invalidRequest;

    private StoryResponse successResponse;
    private StoryResponse failureResponse;

    private StoryServiceImpl storyServiceSpy;

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
        validRequest = new StoryRequest(status1, 3, null);
        invalidRequest = new StoryRequest(null, 0, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new StoryResponse(Arrays.asList(status1, status2, status3), false);
        StoryDA0 storyDA0 = Mockito.mock(StoryDA0.class);
        Mockito.when(storyDA0.getStories(validRequest)).thenReturn(successResponse);

        failureResponse = new StoryResponse("An exception occured");
        Mockito.when(storyDA0.getStories(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        storyServiceSpy = Mockito.spy(new StoryServiceImpl());
        Mockito.when(storyServiceSpy.getStoryDAO()).thenReturn(storyDA0);
    }

    /**
     * Verify that for successful requests the
     * method returns the same result as the {@link StoryDA0}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetStories_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceSpy.getStories(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that the  method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetStoriess_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceSpy.getStories(validRequest);

        for(Status feed : response.getUserStories()) {
            Assertions.assertNotNull(feed.getUser().getImageUrl());
        }
    }

    /**
     * Verify that for failed requests the
     * method returns the same result as the {@link StoryDA0}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetStories_invalidRequest_returnsNoFeeds() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceSpy.getStories(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
