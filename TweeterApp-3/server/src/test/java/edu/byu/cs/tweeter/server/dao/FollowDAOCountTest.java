package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowDAOCountTest {
    private final User user1 = new User("Christopher1", "Wilson1","@cw26361", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
    private final User user2 = new User("Fred", "Flintstone", "");


    private GetFollowCountDA0 followDAOSpy;

    @BeforeEach
    void setup() {
        followDAOSpy = new GetFollowCountDA0();
    }

    @Test
    void testGetFollowCounts_noFollower() {
        LoginRequest request = new LoginRequest("@cw2636", "");
        FollowCountResponse response = followDAOSpy.getTableCount(request);

        Assertions.assertEquals(0, response.getFolloweeCount());
    }
    //
//    @Test
//    void testGetFollowCounts_moreThanOneFollower() {
//
//        LoginRequest request = new LoginRequest("@cw26361", "");
//        FollowCountResponse response = followDAOSpy.getTableCount(request);
//
//        Assertions.assertTrue(response.getFolloweeCount() > 0);
//    }
}
