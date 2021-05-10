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
import edu.byu.cs.tweeter.client.model.service.RegisterService;
import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.client.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.client.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;


public class RegisterServiceProxyTest {

    private RegisterRequest validRequest;
    private RegisterRequest invalidRequest;

    private RegisterResponse successResponse;
    private RegisterResponse failureResponse;

    private RegisterService registerServiceSpy;



    /**
     * Create a FollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {


        String first_name = "chris";
        String last_name = "wils";
        String username = "cw2636";
        String password = "password";
        String imageString = ByteArrayUtils.StrinfFromByteArray("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");


        // Setup request objects to use in the tests
        validRequest = new RegisterRequest(first_name, last_name, username, password, imageString);
        invalidRequest = new RegisterRequest(null,  null, null, null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new RegisterResponse(currentUser, new AuthToken(""));

        failureResponse = new RegisterResponse("An exception occured");

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        registerServiceSpy = new RegisterServiceProxy();
    }

    /**
     * Verify that for successful requests the {@link RegisterService#register(RegisterRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterResponse response = registerServiceSpy.register(validRequest);
        Assertions.assertNotNull(response);
    }

    /**
     * Verify that the {@link RegisterService#register(RegisterRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testRegister_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        RegisterResponse response = registerServiceSpy.register(validRequest);
        Assertions.assertNotNull(response.getUser().getImageUrl());
    }
}
