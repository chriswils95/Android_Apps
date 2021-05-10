package edu.byu.cs.tweeter.model.service.response;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.model.service.request.FollowRequest}.
 */
public class FollowResponse extends Response {



    public void setFollower(User follower) {
        this.follower = follower;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    private User follower;
    private User followee;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowResponse(String message) {
        super(false, message);
    }

    public FollowResponse(User follower, User followee) {
        super(true, null);
        this.follower = follower;
        this.followee = followee;
    }

    /**
     * Returns the followees for the corresponding request.
     *
     * @return the followees.
     */
    public User getFollower() {
        return follower;
    }
    public User getFollowee() {
        return followee;
    }


    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        FollowResponse that = (FollowResponse) param;

        return (Objects.equals(follower, that.follower) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower);
    }
}

