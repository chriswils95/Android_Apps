package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.StoryService;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.RegisterDAO;
import edu.byu.cs.tweeter.server.dao.StoryDA0;

public class StoryServiceImpl implements StoryService {

    @Override
    public StoryResponse getStories(StoryRequest request) {
        return getStoryDAO().getStories(request);
    }

    /**
     * Returns an instance of {@link RegisterDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StoryDA0 getStoryDAO(){ return new StoryDA0();};
}
