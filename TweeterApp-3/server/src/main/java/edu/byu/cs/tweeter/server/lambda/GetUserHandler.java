package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.service.GetUserImpl;
import edu.byu.cs.tweeter.server.service.LoginServiceImpl;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class GetUserHandler implements RequestHandler<LoginRequest, GetUserResponse> {
    @Override
    public GetUserResponse handleRequest(LoginRequest request, Context context) {
        GetUserImpl userService = new GetUserImpl();
        try {
            return userService.getUser(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TweeterRemoteException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
