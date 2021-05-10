package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.client.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.client.model.service.response.LoginResponse;

public class GetUserProxy implements GetUserService {

    static final String URL_PATH = "/getuser";

    @Override
    public GetUserResponse getUser(LoginRequest request) throws IOException, TweeterRemoteException {
        GetUserResponse response = getServerFacade().getUser(request, URL_PATH);

        if(response != null) {
            if (response.isSuccess()) {
            }
        }

        return response;
    }

    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }

}
