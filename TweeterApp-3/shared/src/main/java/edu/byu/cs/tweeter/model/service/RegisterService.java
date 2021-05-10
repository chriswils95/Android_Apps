package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;


/**
 * Contains the business logic to support the login operation.
 */
public interface RegisterService {

    public RegisterResponse register(RegisterRequest request) throws Exception;
}
