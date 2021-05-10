package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class StoryRequest {

    public void setStory(Status story) {
        this.story = story;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLastStory(Status lastStory) {
        this.lastStory = lastStory;
    }

    private  Status story;
    private  int limit;
    private  Status lastStory;

    /**
     * Creates an instance.
     *
     * @param story the {@link Status} whose followees are to be returned.
     * @param limit the maximum number of followees to return.
     * @param lastStory the last followee that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public StoryRequest(Status story, int limit, Status lastStory) {
        this.story = story;
        this.limit = limit;
        this.lastStory = lastStory;
    }

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private StoryRequest() {}

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public Status getStory() {
        return story;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        {
            return "StoryRequest{" +
                    "limit='" + limit + '\'' +
                    ", story='" + story.toString() + '\'' +
//                    ", lastStory='" + lastStory.toString() + '\'' +
                    '}';
        }

    }

    /**
     * Returns the last followee that was returned in the previous request or null if there was no
     * previous request or if no followees were returned in the previous request.
     *
     * @return the last followee.
     */
    public Status getLastStory() {
        return lastStory;
    }
}

