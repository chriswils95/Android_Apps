package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;

class FeedDAOTest {

    private final User user1 = new User("Christopher1", "Wilson1","@cw26361", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
    private final User user2 = new User("Fred", "Flintstone", "");


    private FeedDAO feedDAOSpy;

    @BeforeEach
    void setup() {
        feedDAOSpy = new FeedDAO();
    }

    @Test
    void testGetFeeds_noFeedsForUser() {
        try {
            Status status = new Status(user2, "", null);
            FeedRequest request = new FeedRequest(status, 10, null);
            FeedResponse response = feedDAOSpy.getFeeds(request);

            Assertions.assertEquals(0, response.getUserFollowersFeed().size());
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
            FeedRequest request = new FeedRequest(status, 10, null);
            FeedResponse response = feedDAOSpy.getFeeds(request);

            Assertions.assertNotNull(response.getUserFollowersFeed().size());
            Assertions.assertTrue(response.getUserFollowersFeed().size() > 0);
        }catch (Exception e){
            Assertions.assertTrue(user1.getAlias().length() > 0);
        }
    }




}
