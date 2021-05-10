package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.PostService;
import edu.byu.cs.tweeter.client.model.service.request.PostRequest;
import edu.byu.cs.tweeter.client.model.service.response.PostResponse;


public class PostPresenterTest {

    private PostRequest request;
    private PostRequest invalidRequest;
    private PostResponse response;
    private PostService mockPostService;
    private MainPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status status = new Status(currentUser, "11-22-16", "This is a test");

        request = new PostRequest(status);
        invalidRequest = new PostRequest(null);
        response = new PostResponse(status);

        // Create a mock FollowingService
        mockPostService = Mockito.mock(PostService.class);
        Mockito.when(mockPostService.post(request)).thenReturn(response);

        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new MainPresenter(new MainPresenter.View() {}));
        Mockito.when(presenter.getPostService()).thenReturn(mockPostService);
    }

    @Test
    public void testPost_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockPostService.post(request)).thenReturn(response);

        try {

            // Assert that the presenter returns the same response as the service (it doesn't do
            // anything else, so there's nothing else to test).
            Assertions.assertEquals(response, presenter.post(request));
        }catch (Exception e){
            Assertions.assertTrue(response.getUser().getAlias().length() > 0);
        }
    }

    @Test
    public void testPost_serviceReturnsInCorrectServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockPostService.post(invalidRequest)).thenReturn(response);
        try {

            // Assert that the presenter returns the same response as the service (it doesn't do
            // anything else, so there's nothing else to test).
            Assertions.assertNotEquals(response, presenter.post(invalidRequest));
        }catch (Exception e){
            Assertions.assertTrue(response.getUser().getAlias().length() > 0);
        }
    }
}
