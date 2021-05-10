package edu.byu.cs.tweeter.client.model.service.request;

import edu.byu.cs.tweeter.client.model.domain.Status;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class FeedRequest {

    private final Status feed;
    private final int limit;
    private final Status lastFeed;

    /**
     * Creates an instance.
     *
     * @param feed the {@link Status} whose followees are to be returned.
     * @param limit the maximum number of followees to return.
     * @param lastFeed the last followee that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public FeedRequest(Status feed, int limit, Status lastFeed) {
        this.feed = feed;
        this.limit = limit;
        this.lastFeed = lastFeed;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public Status getFeed() {
        return feed;
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
     * Returns the last followee that was returned in the previous request or null if there was no
     * previous request or if no followees were returned in the previous request.
     *
     * @return the last followee.
     */
    public Status getLastFeed() {
        return lastFeed;
    }
}

