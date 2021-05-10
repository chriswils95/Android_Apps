package edu.byu.cs.tweeter.model.service.response;


public class FollowCountResponse extends PagedResponse {
    private int followeeCount;
    private int followerCount;

    public FollowCountResponse(String message) {
        super(false, message, false);
    }
    public FollowCountResponse(int followeeCount, int followerCount){
        super(true, false);
        this.followeeCount = followeeCount;
        this.followerCount = followerCount;
    }

    public int getFolloweeCount() {
        return followeeCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }
}
