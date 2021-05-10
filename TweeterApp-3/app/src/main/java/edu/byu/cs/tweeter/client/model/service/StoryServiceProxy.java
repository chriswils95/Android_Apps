package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.StaticClass.StaticHelperClass;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.client.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

import edu.byu.cs.tweeter.client.model.domain.Status;

/**
 * Contains the business logic for getting the status of a user followers.
 */
public class StoryServiceProxy implements StoryService {
    static final String URL_PATH = "/getstories";

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link ServerFacade} to
     * get the followees from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public StoryResponse getStories(StoryRequest request) throws IOException, TweeterRemoteException {
        StoryResponse response = getServerFacade().getStories(request, URL_PATH);

        if(response.isSuccess()) {
            loadImages(response);
        }
        return response;
    }

    /**
     * Loads the profile image data for each followee included in the response.
     *
     * @param response the response from the followee request.
     */
    public void loadImages(StoryResponse response) throws IOException {
        for(Status story : response.getUserStories()) {
            byte [] bytes = null;
            bytes = ByteArrayUtils.bytesFromUrl(story.getUser().getImageUrl());
            story.getUser().setImageBytes(bytes);
        }
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
