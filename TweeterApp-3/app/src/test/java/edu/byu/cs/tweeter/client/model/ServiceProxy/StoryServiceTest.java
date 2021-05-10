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
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.client.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.client.model.service.response.StoryResponse;


public class StoryServiceTest {

    private StoryRequest validRequest;
    private StoryRequest invalidRequest;

    private int successResponseLength;
    private StoryResponse failureResponse;

    private StoryService storyServiceSpy;

    /**
     * Create a FollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        Status status1 = new Status(currentUser, "11-22-18", "@google");

        // Setup request objects to use in the tests
        validRequest = new StoryRequest(status1, 3, null);
        invalidRequest = new StoryRequest(null, 0, null);

        successResponseLength = 10;
        failureResponse = new StoryResponse("An exception occured");

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        storyServiceSpy = new StoryServiceProxy();
    }

    /**
     * Verify that for successful requests the {@link StoryService#getStories(StoryRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetStories_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceSpy.getStories(validRequest);
        Assertions.assertTrue(successResponseLength > 0);
    }

    /**
     * Verify that the {@link StoryService#getStories(StoryRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetStoriess_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceSpy.getStories(validRequest);

        for(Status feed : response.getUserStories()) {
            Assertions.assertNotNull(feed.getUser().getImageBytes());
        }
    }
}
