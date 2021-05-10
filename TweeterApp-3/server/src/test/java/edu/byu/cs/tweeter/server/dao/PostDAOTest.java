package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostDAOTest {
    private final User user1 = new User("Christopher1", "Wilson1","@cw26361", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
    private final User user2 = new User("Fred", "Flintstone", "");


    private PostDAO postDAOSpy;

    @BeforeEach
    void setup() {
        postDAOSpy = new PostDAO();
    }

    @Test
    void testGetPosts_unsuccessful() throws Exception {
        Status status = new Status(user2,"", "This is a post" );
        PostRequest request = new PostRequest(status);
        PostResponse response = postDAOSpy.post(request);

        Assertions.assertNotEquals("@cw26361", response.getPost().getUser().getAlias());
        Assertions.assertEquals("", response.getPost().getDate());

    }
    //
    @Test
    void testGetPosts_successful() throws Exception {

        Status status = new Status(user1,"", "This is a post" );
        PostRequest request = new PostRequest(status);
        PostResponse response = postDAOSpy.post(request);

        Assertions.assertNotNull(response.getPost());

    }
}
