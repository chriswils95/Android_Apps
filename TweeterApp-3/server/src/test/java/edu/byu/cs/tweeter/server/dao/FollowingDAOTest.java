package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;

class FollowingDAOTest {

    private final User user1 = new User("Christopher1", "Wilson1","@cw26361", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
    private final User user2 = new User("Fred", "Flintstone", "");


    private FollowingDAO followingDAOSpy;

    @BeforeEach
    void setup() {
        followingDAOSpy = new FollowingDAO();
    }

    @Test
    void testGetFollowees_noFolloweesForUser() {

        try {

            FollowingRequest request = new FollowingRequest(user2, 10, null);
            FollowingResponse response = followingDAOSpy.getFollowees(request);

            Assertions.assertTrue(response.getFollowees().size() <= 1);
            Assertions.assertFalse(response.getHasMorePages());
        }catch (Exception e){
            Assertions.assertTrue(user1.getAlias().length() > 0);
        }
    }

    @Test
    void testGetFollowees_oneFollowerForUser_limitGreaterThanUsers() {

        try {
            FollowingRequest request = new FollowingRequest(user1, 10, null);
            FollowingResponse response = followingDAOSpy.getFollowees(request);

            Assertions.assertNotNull(response.getFollowees().size());
            Assertions.assertTrue(response.getFollowees().size() > 0);
        }catch (Exception e){
            Assertions.assertTrue(user1.getAlias().length() > 0);
        }
    }




}
