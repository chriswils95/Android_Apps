package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.plaf.PanelUI;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.Dynamodb.ResultsPage;
import edu.byu.cs.tweeter.server.StaticClass.Timeout;

public class UserDAO {

    private final String USER_P_KEY = "alias";
    private final String USER_S_KEY = "image_url";
    private final String USER_TABLE_NAME = "User";
    Database db;
    Database follow_db;

    public UserDAO(){
        db = new Database(USER_TABLE_NAME, USER_P_KEY, USER_S_KEY);
        follow_db = new Database("Followee", "follower_handle", "followee_handle");
    }

    public GetUserResponse getUser(LoginRequest request){

        LocalTime currTime = LocalTime.now();
        Timeout.setCurrTime(currTime);
        if(Timeout.sessionTimeout()){
            throw new RuntimeException("400");
        }
        User user;
        Item userItem = db.getItem(request.getUsername());
        System.out.println(request.getMain_username());
        boolean isFollow = false;
        GetUserResponse response = null;
        isFollow = checkFollow(request.getMain_username(), request.getUsername());
        System.out.println(isFollow);
        String image_url = (String) userItem.get("image_url");
        Map<String, Object> info = (Map<String, Object>) userItem.get("info");
        String firstName = (String) info.get("FirstName");
        String lastName = (String) info.get("LastName");
        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(image_url);
        System.out.println(request.getUsername());
        user = new User(firstName, lastName, request.getUsername(), image_url);
        String token_table = "AuthToken";
        String p_key = "user_handle";
        String s_key = "authToken";
        db = new Database(token_table, p_key, s_key);
        String token = UUID.randomUUID().toString();
        db.addItems(user.getAlias(), token);
        System.out.println(isFollow);
        response = new GetUserResponse(user, new AuthToken(token), isFollow);

        return response;

    }

    public boolean checkFollow(String alias, String user_alias){
        Item item = follow_db.getItemWithHash(alias, user_alias);
        if(item != null){
            return true;
        }

       return false;
    }

}
