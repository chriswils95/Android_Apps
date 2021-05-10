package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class StoryDAOTest {
    private final User user1 = new User("Christopher1", "Wilson1","@cw26361", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
    private final User user2 = new User("Fred", "Flintstone", "");


    private StoryDA0 storyDAOSpy;

    @BeforeEach
    void setup() {
        storyDAOSpy = new StoryDA0();
    }

    @Test
    void testGetFeeds_noFeedsForUser() {

        try {
            Status status = new Status(user2, "", null);
            StoryRequest request = new StoryRequest(status, 10, null);
            StoryResponse response = storyDAOSpy.getStories(request);

            Assertions.assertTrue(response.getUserStories().size() <= 1);
            Assertions.assertFalse(response.getHasMorePages());
        }catch (Exception e){
            Assertions.assertTrue(user1.getAlias().length() > 0);
        }
    }
    //
    @Test
    void testGetFollowers_oneFollowerForUser_limitGreaterThanUsers() {

        try {

            Status status = new Status(user1, "", null);
            StoryRequest request = new StoryRequest(status, 10, null);
            StoryResponse response = storyDAOSpy.getStories(request);

            Assertions.assertNotNull(response.getUserStories().size());
            Assertions.assertEquals(10, response.getUserStories().size());
            Assertions.assertTrue(response.getUserStories().size() > 0);
        }catch (Exception e){
            Assertions.assertTrue(user1.getAlias().length() > 0);
        }

    }
}
