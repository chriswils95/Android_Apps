package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.PostRequest;
import edu.byu.cs.tweeter.client.model.service.response.PostResponse;


public class PostServiceTest {

    private PostRequest validRequest;
    private PostRequest invalidRequest;

    private PostResponse successResponse;
    private PostResponse failureResponse;

    private PostService postServiceSpy;

    /**
     * Create a FollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {

        String username = "cw2636";
        String password = "password";
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status status = new Status(currentUser, "11-22-16", "This is a test");



        // Setup request objects to use in the tests
        validRequest = new PostRequest(status);
        invalidRequest = new PostRequest(null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new PostResponse(status);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.post(validRequest, "/post")).thenReturn(successResponse);

        failureResponse = new PostResponse("An exception occured");
        Mockito.when(mockServerFacade.post(invalidRequest, "/post")).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        postServiceSpy = Mockito.spy(new PostServiceProxy());
        Mockito.when(postServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the {@link PostService#post(PostRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testPost_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostResponse response = postServiceSpy.post(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that the {@link PostService#post(PostRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testPost_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        PostResponse response = postServiceSpy.post(validRequest);
        Assertions.assertNotNull(response.getUser().getImageUrl());
    }

    /**
     * Verify that for failed requests the {@link PostService#post(PostRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testPost_invalidRequest_returnsNoUser() throws IOException, TweeterRemoteException {
        PostResponse response = postServiceSpy.post(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
