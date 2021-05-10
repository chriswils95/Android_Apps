package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.PostDAO;


public class PostServiceImplTest {

    private PostRequest validRequest;
    private PostRequest invalidRequest;

    private PostResponse successResponse;
    private PostResponse failureResponse;

    private PostServiceImpl postServiceSpy;

    /**
     * Create a FollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws Exception {

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
        PostDAO postDAO = Mockito.mock(PostDAO.class);
        Mockito.when(postDAO.post(validRequest)).thenReturn(successResponse);

        failureResponse = new PostResponse("An exception occured");
        Mockito.when(postDAO.post(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        postServiceSpy = Mockito.spy(new PostServiceImpl());
        Mockito.when(postServiceSpy.makePost()).thenReturn(postDAO);
    }

    /**
     * Verify that for successful requests the {@link PostService#post(PostRequest)}
     * method returns the same result as the {@link PostDAO}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testPost_validRequest_correctResponse() throws Exception {
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
    public void testPost_validRequest_loadsProfileImages() throws Exception {
        PostResponse response = postServiceSpy.post(validRequest);
        Assertions.assertNotNull(response.getUser().getImageUrl());
    }

    /**
     * Verify that for failed requests the {@link PostService#post(PostRequest)}
     * method returns the same result as the {@link PostDAO}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testPost_invalidRequest_returnsNoUser() throws Exception {
        PostResponse response = postServiceSpy.post(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
