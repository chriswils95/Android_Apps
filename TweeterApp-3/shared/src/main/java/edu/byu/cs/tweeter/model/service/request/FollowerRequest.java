package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowerRequest {
    private  User followee;
    private  int limit;
    private  User lastFollower;


    /**
     * Creates an instance.
     *
     * @param followee the {@link User} whose followees are to be returned.
     * @param limit the maximum number of followees to return.
     * @param lastFollower the last followee that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public FollowerRequest(User followee, int limit, User lastFollower) {
        this.followee = followee;
        this.limit = limit;
        this.lastFollower = lastFollower;
    }

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    public FollowerRequest() {}


    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public User getFollowee() {
        return followee;
    }

    /**
     * Sets the follower.
     *
     * @param followee the follower.
     */
    public void setFollowee(User followee) {
        this.followee = followee;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the last followee that was returned in the previous request or null if there was no
     * previous request or if no followees were returned in the previous request.
     *
     * @return the last followee.
     */
    public User getLastFollower() {
        return lastFollower;
    }

    /**
     * Sets the last followee.
     *
     * @param lastFollower the last followee.
     */
    public void setLastFollower(User lastFollower) {
        this.lastFollower = lastFollower;
    }
}
