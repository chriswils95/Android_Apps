package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.LoginDAO;


public class LoginServiceImplTest {

    private LoginRequest validRequest;
    private LoginRequest invalidRequest;

    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    private LoginServiceImpl loginServiceSpy;

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

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");


        // Setup request objects to use in the tests
        validRequest = new LoginRequest(username, password);
        invalidRequest = new LoginRequest(null,  null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LoginResponse(currentUser, new AuthToken(""));
        LoginDAO loginDAO = Mockito.mock(LoginDAO.class);
        Mockito.when(loginDAO.login(validRequest)).thenReturn(successResponse);

        failureResponse = new LoginResponse("An exception occured");
        Mockito.when(loginDAO.login(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        loginServiceSpy = Mockito.spy(new LoginServiceImpl());
        Mockito.when(loginServiceSpy.getLoginDAO()).thenReturn(loginDAO);
    }

    /**
     * Verify that for successful requests the {@link LoginService#login(LoginRequest)}
     * method returns the same result as the {@link LoginDAO}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogin_validRequest_correctResponse() throws Exception {
        LoginResponse response = loginServiceSpy.login(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that the {@link LoginService#login(LoginRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogin_validRequest_loadsProfileImages() throws Exception {
        LoginResponse response = loginServiceSpy.login(validRequest);
        Assertions.assertNotNull(response.getUser().getImageUrl());
    }

    /**
     * Verify that for failed requests the {@link LoginService#login(LoginRequest)}
     * method returns the same result as the {@link LoginDAO}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogin_invalidRequest_returnsNoUser() throws Exception {
        LoginResponse response = loginServiceSpy.login(invalidRequest);
        Assertions.assertEquals(null, response.getUser());
    }
}
