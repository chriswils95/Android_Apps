package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class FollowRequest {

    private  User follower;
    private  User followee;
    private boolean unFollow;

    public boolean isUnFollow() {
        return unFollow;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public FollowRequest(){};
    /**
     * Creates an instance.
     *
     * @param follower the {@link User} whose followees are to be returned.
     * @param Followee the last followee that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public FollowRequest(User follower, User Followee, boolean unFollow) {
        this.follower = follower;
        this.followee = Followee;
        this.unFollow = unFollow;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public User getFollower() {
        return follower;
    }



    /**
     * Returns the last followee that was returned in the previous request or null if there was no
     * previous request or if no followees were returned in the previous request.
     *
     * @return the last followee.
     */
    public User getFollowee() {
        return followee;
    }

    public void setUnFollow(boolean unFollow) {
        this.unFollow = unFollow;
    }
}
