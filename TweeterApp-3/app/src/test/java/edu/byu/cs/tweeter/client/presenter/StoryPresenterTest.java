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
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.client.model.service.response.StoryResponse;


public class StoryPresenterTest {

    private StoryRequest request;
    private StoryResponse response;
    private StoryService mockStoryService;
    private StoryPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status status1 = new Status(currentUser, "11-22-18", "@google");
        Status status2 = new Status(currentUser, "11-22-17", "www.google.com");
        Status status3 = new Status(currentUser, "11-22-16", "This is a test");

        request = new StoryRequest(status1, 3, null);
        response = new StoryResponse(Arrays.asList(status1, status2, status3), false);

        // Create a mock FollowingService
        mockStoryService = Mockito.mock(StoryService.class);
        Mockito.when(mockStoryService.getStories(request)).thenReturn(response);

        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new StoryPresenter(new StoryPresenter.View() {}));
        Mockito.when(presenter.getStoryService()).thenReturn(mockStoryService);
    }

    @Test
    public void testGetStrories_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockStoryService.getStories(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getStories(request));
    }

    @Test
    public void testGetStories_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockStoryService.getStories(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getStories(request);
        });
    }
}
