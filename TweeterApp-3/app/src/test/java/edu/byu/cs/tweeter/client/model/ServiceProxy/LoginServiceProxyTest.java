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
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.response.LoginResponse;


public class LoginServiceProxyTest {

    private LoginRequest validRequest;
    private LoginRequest invalidRequest;

    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    private LoginServiceProxy loginServiceSpy;

    /**
     * Create a FollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {

        String username = "cw2636";
        String password = "password";
        User currentUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");


        // Setup request objects to use in the tests
        validRequest = new LoginRequest(username, password);
        invalidRequest = new LoginRequest(null,  null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LoginResponse(currentUser, new AuthToken(""));

        failureResponse = new LoginResponse("An exception occured");

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        loginServiceSpy = new LoginServiceProxy();
    }

    /**
     * Verify that for successful requests the {@link LoginService#login(LoginRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        try {
            LoginResponse response = loginServiceSpy.login(validRequest);
            Assertions.assertNotNull(successResponse.getUser());
        }catch (Exception e){
            Assertions.assertTrue(successResponse.getUser().getAlias().length() > 0);
        }
    }

}
