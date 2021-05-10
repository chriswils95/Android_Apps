package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;

import java.time.LocalTime;

import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.StaticClass.Timeout;

public class FollowDAO {

    private final String TABLE_NAME = "Followee";
    private final String P_KEY_ATTR = "follower_handle";
    private final String S_KEY_ATTR = "followee_handle";
    Database db;

    public FollowDAO(){
        db = new Database(TABLE_NAME, P_KEY_ATTR, S_KEY_ATTR);
    }

    public FollowResponse follow(FollowRequest request){

        LocalTime currTime = LocalTime.now();
        Timeout.setCurrTime(currTime);
        if(Timeout.sessionTimeout()){
            throw new RuntimeException("400");
        }
        System.out.println(request.getFollowee().getAlias());
        if(request.isUnFollow()){
            DeleteItemSpec deleteItemSpec = new DeleteItemSpec().
                    withPrimaryKey(P_KEY_ATTR, request.getFollower().getAlias(), S_KEY_ATTR, request.getFollowee().getAlias());
            db.deleteItems(deleteItemSpec);
        }
        else {

            if((request.getFollowee() != null) || (request.getFollower() != null)){
                if((request.getFollower().getAlias() != null) || (request.getFollowee().getAlias() != null)) {
                    db.addItems(request.getFollower().getAlias(), request.getFollowee().getAlias());
                }
            }
        }
        return new FollowResponse(request.getFollower(), request.getFollowee());
    }


}
