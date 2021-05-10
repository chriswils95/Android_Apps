package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.client.model.service.response.LoginResponse;

public interface GetUserService {
    GetUserResponse getUser(LoginRequest request) throws IOException, TweeterRemoteException;
}
