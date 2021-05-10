package edu.byu.cs.tweeter.server.dao;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.Dynamodb.ResultsPage;
import edu.byu.cs.tweeter.server.StaticClass.Timeout;

public class GetFollowCountDA0 {


    Database db;
    private final String TABLE_NAME = "Follower";
    private final String P_KEY_ATTR = "followee_handle";
    private final String S_KEY_ATTR = "follower_handle";

    public GetFollowCountDA0(){
        db = new Database(TABLE_NAME, P_KEY_ATTR, S_KEY_ATTR);
    }

    public FollowCountResponse getTableCount(LoginRequest loginRequest){

        LocalTime currTime = LocalTime.now();
        Timeout.setCurrTime(currTime);
        if(Timeout.sessionTimeout()){
            throw new RuntimeException("400");
        }

        int followingCount = getFollowingCount(loginRequest.getUsername());
        int followerCount = getFollowerCount(loginRequest.getUsername());

        System.out.println(followerCount);
        System.out.println(followingCount);

        return new FollowCountResponse(followingCount, followerCount);
    }


    public int getFollowerCount(String alias){
        ResultsPage results = null;
        List<User> users = null;

        Set<String> followerAlias = new HashSet<>();
        boolean hasMorePages = false;

        String lastFollowerAlias = null;

        while (results == null || results.hasLastKey()) {
            lastFollowerAlias = ((results != null) ? results.getLastKey() : null);
            results = db.getFollowers(alias, 10, lastFollowerAlias);
            followerAlias.addAll(results.getValues());
        }


        System.out.println(followerAlias.size());

        return followerAlias.size();
    }

    public int getFollowingCount(String alias){
        ResultsPage results = null;

        Database dao = new Database("Followee", "follower_handle", "followee_handle");

        Set<String> followeeAlias = new HashSet<>();
        boolean hasMorePages = false;

        System.out.println();
        String lastFollowerAlias = null;

        lastFollowerAlias = null;
        while (results == null || results.hasLastKey()) {
            lastFollowerAlias = ((results != null) ? results.getLastKey() : null);
            results = dao.getFollowees(alias, 1, lastFollowerAlias);
            followeeAlias.addAll(results.getValues());
        }

        return followeeAlias.size();
    }
}
