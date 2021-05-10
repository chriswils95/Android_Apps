package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;

import java.time.LocalTime;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.StaticClass.Timeout;

public class LogoutDAO {

    private final String TABLE_NAME = "AuthToken";
    private final String P_KEY_ATTR = "user_handle";
    private final String S_KEY_ATTR = "authToken";
    Database db;

    public LogoutDAO(){
        db = new Database(TABLE_NAME, P_KEY_ATTR, S_KEY_ATTR);
    }


    public LogoutResponse logout(LogoutRequest request){
        LocalTime currTime = LocalTime.now();
        Timeout.setCurrTime(currTime);
        if(Timeout.sessionTimeout()){
            throw new RuntimeException("400");
        }

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec().
                withPrimaryKey(P_KEY_ATTR, request.getUsername(), S_KEY_ATTR, request.getToken());
       boolean deleted =  db.deleteItems(deleteItemSpec);
       if(deleted) {
           return new LogoutResponse(new AuthToken(request.getToken()));
       }
        return new LogoutResponse(new AuthToken(null));
    }
}
