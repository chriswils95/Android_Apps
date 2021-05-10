package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutDAOTest {
    private LogoutDAO logoutDAOSpy;

    @BeforeEach
    void setup() {
        logoutDAOSpy = new LogoutDAO();
    }

    @Test
    void testGetFeeds_inValidLogin() throws Exception {
        LogoutRequest request = new LogoutRequest("cw2636", "Isatu1997");
        LogoutResponse response = logoutDAOSpy.logout(request);

        Assertions.assertNotNull(response.getAuthToken().getToken());
    }
    //
    @Test
    void testGetLogin_validLogin() throws Exception {
        LogoutRequest request = new LogoutRequest("@cw26361", "Isatu1997");
        LogoutResponse response = logoutDAOSpy.logout(request);

        Assertions.assertNull(response.getUser());
        Assertions.assertNotNull(response.getAuthToken().getToken());
    }
}
