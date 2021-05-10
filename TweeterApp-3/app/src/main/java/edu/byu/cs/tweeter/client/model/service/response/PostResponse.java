package edu.byu.cs.tweeter.client.model.service.response;

import java.util.Objects;

import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.client.model.service.request.FollowingRequest}.
 */
public class PostResponse extends Response {

    private Status post;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public PostResponse(String message) {
        super(false, message);
    }

    public PostResponse(Status post) {
        super(true, null);
        this.post = post;
    }

    /**
     * Returns the followees for the corresponding request.
     *
     * @return the followees.
     */
    public Status getPost() {
        return post;
    }
    public User getUser() {
        return post.getUser();
    }


    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        PostResponse that = (PostResponse) param;

        return (Objects.equals(post, that.post) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(post);
    }
}

