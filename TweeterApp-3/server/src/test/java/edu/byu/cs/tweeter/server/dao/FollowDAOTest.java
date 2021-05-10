package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;

public class FollowDAOTest {
    private final User user1 = new User("Christopher1", "Wilson1","@cw26361", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
    private final User user2 = new User("Fred", "Flintstone", "");


    private FollowDAO followDAOSpy;

    @BeforeEach
    void setup() {
        followDAOSpy = new FollowDAO();
    }

    @Test
    void testGetFollows_noFollowsForUser() {
        Status status = new Status(user2,"", null );
        FollowRequest request = new FollowRequest(user2, user1, false);
        FollowResponse response = followDAOSpy.follow(request);

        Assertions.assertNotNull(response.getFollowee());
    }
    //
    @Test
    void testGetFollows_oneFollowForUser_limitGreaterThanUsers() {

        FollowRequest request = new FollowRequest(user1, user2, false);
        FollowResponse response = followDAOSpy.follow(request);

        Assertions.assertNotNull(response.getFollowee());
    }
}
