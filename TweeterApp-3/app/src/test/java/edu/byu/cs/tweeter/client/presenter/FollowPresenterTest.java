package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowResponse;


public class FollowPresenterTest {

    private FollowRequest request;
    private FollowRequest invalidRequest;
    private FollowResponse response;
    private FollowService mockFollowService;
    private MainPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        invalidRequest = new FollowRequest(null, null, false);
        request = new FollowRequest(currentUser, resultUser1, true);
        response = new FollowResponse(currentUser, resultUser1);

        // Create a mock FollowingService
        mockFollowService = Mockito.mock(FollowService.class);
        Mockito.when(mockFollowService.follow(request)).thenReturn(response);


        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new MainPresenter(new MainPresenter.View() {}));
        Mockito.when(presenter.getFollowService()).thenReturn(mockFollowService);

    }

    @Test
    public void testGetFollow_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowService.follow(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.follow(request));
    }

    @Test
    public void testGetFollow_serviceReturnsInCorrectServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowService.follow(invalidRequest)).thenReturn(response);

        Assertions.assertEquals(response, presenter.follow(request));



    }
}
