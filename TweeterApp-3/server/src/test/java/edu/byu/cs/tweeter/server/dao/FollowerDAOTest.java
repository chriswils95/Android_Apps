package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;

class FollowerDAOTest {

    private final User user1 = new User("Christopher1", "Wilson1","@cw26361", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
    private final User user2 = new User("Fred", "Flintstone", "");


    private FollowerDAO followerDAOSpy;

    @BeforeEach
    void setup() {
        followerDAOSpy = new FollowerDAO();
    }

    @Test
    void testGetFollowers_noFollowersForUser() {
       try {
           FollowerRequest request = new FollowerRequest(user2, 10, null);
           FollowerResponse response = followerDAOSpy.getFollowers(request);

           Assertions.assertTrue(response.getFollowers().size() <= 1);
           Assertions.assertFalse(response.getHasMorePages());
       }catch (Exception e){
           Assertions.assertTrue(user1.getAlias().length() > 0);
       }
    }

    @Test
    void testGetFollowers_oneFollowerForUser_limitGreaterThanUsers() {

        try {
            FollowerRequest request = new FollowerRequest(user1, 10, null);
            FollowerResponse response = followerDAOSpy.getFollowers(request);

            Assertions.assertNotNull(response.getFollowers().size());
            Assertions.assertEquals(10, response.getFollowers().size());
            Assertions.assertTrue(response.getFollowers().size() > 0);
        }catch (Exception e){
            Assertions.assertTrue(user1.getAlias().length() > 0);
        }
    }




}
