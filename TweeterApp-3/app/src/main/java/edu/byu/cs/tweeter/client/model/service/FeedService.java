package edu.byu.cs.tweeter.client.model.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.client.model.service.response.FeedResponse;

/**
     * Contains the business logic for getting the status of a user followers.
     */
    public interface FeedService {

        /**
         * Returns the users that the user specified in the request is following. Uses information in
         * the request object to limit the number of followees returned and to return the next set of
         * followees after any that were returned in a previous request. Uses the {@link ServerFacade} to
         * get the followees from the server.
         *
         * @param request contains the data required to fulfill the request.
         * @return the followees.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        public FeedResponse getFeeds(FeedRequest request) throws IOException, TweeterRemoteException;

        /**
         * Loads the profile image data for each followee included in the response.
         *
         * @param response the response from the followee request.
         */
        public void loadImages(FeedResponse response) throws IOException;

        /**
         * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
         * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
         * method to allow for proper mocking.
         *
         * @return the instance.
         */
        ServerFacade getServerFacade();

}
