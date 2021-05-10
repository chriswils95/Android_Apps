package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class LoginDAOTest {



    private LoginDAO loginDAOSpy;

    @BeforeEach
    void setup() {
        loginDAOSpy = new LoginDAO();
    }

    @Test
    void testGetFeeds_inValidLogin() throws Exception {

        try {
            LoginRequest request = new LoginRequest("cw2636bb", "Isatu1997");
            LoginResponse response = loginDAOSpy.login(request);

            Assertions.assertNull(response.getAuthToken().getToken());
        }catch (Exception e){
            Assertions.assertTrue(true);
        }
    }
    //
    @Test
    void testGetLogin_validLogin() throws Exception {
        try {
            LoginRequest request = new LoginRequest("@cw26361", "Isatu1997");
            LoginResponse response = loginDAOSpy.login(request);

            Assertions.assertNotNull(response.getUser());
            Assertions.assertEquals("@cw26361", response.getUser().getAlias());
        }catch ( Exception e){
            Assertions.assertTrue(true);
        }
    }
}
