package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class PostRequest {

    private Status  post;

    public void setPost(Status post) {
        this.post = post;
    }

    private PostRequest(){};
    /**
     * Creates an instance.
     *
     * @param post the last followee that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public PostRequest(Status post) {
      this.post = post;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public User getUser() {
        return post.getUser();
    }



    /**
     * Returns the last followee that was returned in the previous request or null if there was no
     * previous request or if no followees were returned in the previous request.
     *
     * @return the last followee.
     */
    public Status getPost() {
        return post;
    }
}
