package edu.byu.cs.tweeter.client.model.ServiceProxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.LogoutService;
import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.client.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.client.model.service.response.LogoutResponse;


public class LogoutServiceProxyTest {

    private LogoutRequest validRequest;
    private LogoutRequest invalidRequest;

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



        // Setup request objects to use in the tests
        validRequest = new LogoutRequest(username, password);
        invalidRequest = new LogoutRequest(null,  null);

        // Setup a mock ServerFacade that will return known responses

        failureResponse = new LogoutResponse("An exception occured");


        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        logoutServiceSpy = new LogoutServiceProxy();
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
        Assertions.assertNotNull(response);
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


}
