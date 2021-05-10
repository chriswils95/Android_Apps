package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.PostRequest;
import edu.byu.cs.tweeter.client.model.service.response.PostResponse;

public class PostServiceProxy implements PostService {

    static final String URL_PATH = "/post";

    public PostResponse post(PostRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        PostResponse postResponse = serverFacade.post(request, URL_PATH);

        if(postResponse.isSuccess()) {
        }

        return postResponse;
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
