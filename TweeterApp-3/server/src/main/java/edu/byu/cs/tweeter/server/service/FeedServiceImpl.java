package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;

public class FeedServiceImpl implements FeedService {


    /**
     * Returns an instance of {@link FeedDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */

    FeedDAO getFeedsDA0(){return new FeedDAO();}

    @Override
    public FeedResponse getFeeds(FeedRequest request) {
        return getFeedsDA0().getFeeds(request);
    }
}
