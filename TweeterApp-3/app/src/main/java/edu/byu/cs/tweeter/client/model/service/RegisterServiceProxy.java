package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.client.model.service.response.RegisterResponse;

public class RegisterServiceProxy implements RegisterService  {
    static final String URL_PATH = "/register";
    @Override
    public RegisterResponse register(RegisterRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        RegisterResponse registerResponse = serverFacade.register(request, URL_PATH);

        if(registerResponse.isSuccess()) {
        }

        return registerResponse;    }

    @Override
    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
