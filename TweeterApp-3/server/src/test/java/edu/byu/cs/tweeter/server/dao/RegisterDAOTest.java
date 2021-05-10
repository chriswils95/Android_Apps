package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterDAOTest {



    private RegisterDAO registerDAOSpy;

    @BeforeEach
    void setup() {
        registerDAOSpy = new RegisterDAO();
    }

    @Test
    void testGetFeeds_inValidLogin() throws Exception {

        RegisterResponse response = null;
        try {
            RegisterRequest request = new RegisterRequest();

            request.setImageString("jaoan");
             response = registerDAOSpy.getRegister(request);
        }catch (Exception e){
            Assertions.assertNull(response);
        }


    }

}
