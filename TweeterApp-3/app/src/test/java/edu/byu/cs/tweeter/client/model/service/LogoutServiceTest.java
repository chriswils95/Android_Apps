package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.client.model.service.response.LogoutResponse;


public class LogoutServiceTest {

    private LogoutRequest validRequest;
    private LogoutRequest invalidRequest;

    private LogoutResponse successResponse;
    private LogoutResponse failureResponse;

    private LogoutService logoutServiceSpy;

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

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");


        // Setup request objects to use in the tests
        validRequest = new LogoutRequest(username, password);
        invalidRequest = new LogoutRequest(null,  null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LogoutResponse(new AuthToken(""));
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.logout(validRequest, "/logout")).thenReturn(successResponse);

        failureResponse = new LogoutResponse("An exception occured");
        Mockito.when(mockServerFacade.logout(invalidRequest, "/logout" +
                "")).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        logoutServiceSpy = Mockito.spy(new LogoutServiceProxy());
        Mockito.when(logoutServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the {@link LogoutService#logout(LogoutRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceSpy.logout(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that the {@link LogoutService#logout(LogoutRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogout_validRequest_validAuthToken() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceSpy.logout(validRequest);
        Assertions.assertNotNull(response.getAuthToken());
    }

    /**
     * Verify that for failed requests the {@link LogoutService#logout(LogoutRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogout_invalidRequest_returnsNoUser() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceSpy.logout(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
