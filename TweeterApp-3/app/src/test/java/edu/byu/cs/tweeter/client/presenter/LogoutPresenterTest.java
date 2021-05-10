package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.LogoutService;
import edu.byu.cs.tweeter.client.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.client.model.service.response.LogoutResponse;


public class LogoutPresenterTest {

    private LogoutRequest request;
    private LogoutRequest invalidRequest;
    private LogoutResponse response;
    private LogoutService mockLogoutService;
    private MainPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        String username = "cw2636";
        String password = "password";

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        invalidRequest = new LogoutRequest(null, null);
        request = new LogoutRequest(username, password);
        response = new LogoutResponse(new AuthToken(""));

        // Create a mock FollowingService
        mockLogoutService = Mockito.mock(LogoutService.class);
        Mockito.when(mockLogoutService.logout(request)).thenReturn(response);


        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new MainPresenter(new MainPresenter.View() {}));
        Mockito.when(presenter.getLogoutService()).thenReturn(mockLogoutService);

    }

    @Test
    public void testLogout_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockLogoutService.logout(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertNotNull(presenter.logout(request));
    }


    @Test
    public void testLogout_serviceReturnsInCorrectServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockLogoutService.logout(invalidRequest)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertNotNull(presenter.logout(invalidRequest));
    }

}
