package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.client.model.service.response.FeedResponse;


public class FeedPresenterTest {

    private FeedRequest request;
    private FeedResponse response;
    private FeedService mockFeedService;
    private FeedPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status status1 = new Status(currentUser, "11-22-18", "@google");
        Status status2 = new Status(currentUser, "11-22-17", "www.google.com");
        Status status3 = new Status(currentUser, "11-22-16", "This is a test");

        request = new FeedRequest(status1, 3, null);
        response = new FeedResponse(Arrays.asList(status1, status2, status3), false);

        // Create a mock FollowingService
        mockFeedService = Mockito.mock(FeedService.class);
        Mockito.when(mockFeedService.getFeeds(request)).thenReturn(response);

        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new FeedPresenter(new FeedPresenter.View() {}));
        Mockito.when(presenter.getFeedService()).thenReturn(mockFeedService);
    }

    @Test
    public void testGetFollowing_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFeedService.getFeeds(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getFeeds(request));
    }

    @Test
    public void testGetFollowing_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockFeedService.getFeeds(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFeeds(request);
        });
    }
}
