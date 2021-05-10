package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowResponse;

public class FollowServiceProxy implements FollowService {

    static final String URL_PATH = "/follow";

    public FollowResponse follow(FollowRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        FollowResponse followResponse = serverFacade.follow(request, URL_PATH);

        if(followResponse.isSuccess()) {
        }

        return followResponse;
    }



    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
     * method to allow for proper mocking.
     *
     * @return the instance.
     */
    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
