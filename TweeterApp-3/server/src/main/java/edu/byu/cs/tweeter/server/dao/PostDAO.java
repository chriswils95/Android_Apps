package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.endpointdiscovery.DaemonThreadFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.Dynamodb.ResultsPage;
import edu.byu.cs.tweeter.server.StaticClass.Timeout;
import edu.byu.cs.tweeter.server.sqs.UpdateFeeds;

public class PostDAO implements Serializable {

    User current_user = new User("Test", "User",
            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

    UpdateFeeds updateFeeds;
    private final String TABLE_NAME = "Follower";
    private final String P_KEY_ATTR = "followee_handle";
    private final String S_KEY_ATTR = "follower_handle";
    Database db;

    public PostDAO(){
        updateFeeds = new UpdateFeeds();
        db = new Database(TABLE_NAME, P_KEY_ATTR, S_KEY_ATTR);
    }
    public PostResponse post(PostRequest request) throws Exception {

        postToUser(request.getPost());
        String entityBody = new Gson().toJson(request.getPost());

        updateFeeds.sendMessage(entityBody);
        Status status = new Status(current_user, request.getPost().getDate(), request.getPost().getMessage());
        return new PostResponse(status);
    }

    public Status generatePost(User user){
        return new Status(user, "Thu, Oct 15 2020 11:10:20",  "This is a test post");
    }

    public void postToUser(Status status) throws Exception {
        String table = "Status";
        String key = "user_alias";
        String s_key = "date";
        Database db = new Database(table, key, s_key);

        final Map<String, Object> infoMap = new HashMap<String, Object>();

        infoMap.put("message", status.getMessage());

        db.addItems(infoMap, status.getUser().getAlias(), status.getDate());
    }

}
