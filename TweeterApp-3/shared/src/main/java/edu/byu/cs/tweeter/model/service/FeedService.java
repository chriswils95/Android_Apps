package edu.byu.cs.tweeter.model.service;



import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

/**
     * Contains the business logic for getting the status of a user followers.
     */
    public interface FeedService {

        /**
         * Returns the users that the user specified in the request is following. Uses information in
         * the request object to limit the number of followees returned and to return the next set of
         * followees after any that were returned in a previous request. Uses the {@link } to
         * get the followees from the server.
         *
         * @param request contains the data required to fulfill the request.
         * @return the followees.
         */
        public FeedResponse getFeeds(FeedRequest request);


}
