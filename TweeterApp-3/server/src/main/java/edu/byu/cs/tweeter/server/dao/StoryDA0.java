package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.gson.Gson;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.Dynamodb.ResultsPage;
import edu.byu.cs.tweeter.server.StaticClass.Timeout;

public class StoryDA0 {

    private final String TABLE_NAME = "Status";
    private final String P_KEY_ATTR = "user_alias";
    private final String S_KEY_ATTR = "date";
    Database db;

    User current_user = new User("Test", "User",
                            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

    public StoryDA0(){
        db = new Database(TABLE_NAME, P_KEY_ATTR, S_KEY_ATTR);
    }

    public StoryResponse getStories(StoryRequest storyRequest){


        LocalTime currTime = LocalTime.now();

        LocalTime init = getTime(storyRequest.getStory().getUser().getAlias());

        long elapsedMinutes = Duration.between(init, currTime).toMinutes();


        if(elapsedMinutes > 1000){
            throw new RuntimeException("400");
        }

        boolean hasMorePages = false;




        List<Map<String, AttributeValue>>values = new ArrayList<>();

        String lastStoryAlias = null;
        String pKey = storyRequest.getStory().getUser().getAlias();
        if(storyRequest.getLastStory() != null){
            storyRequest.getLastStory().getUser().getAlias();
        }
        ResultsPage resultsPage = db.getItems(pKey, storyRequest.getLimit(),
                lastStoryAlias);

        if(resultsPage.hasLastKey()){
            hasMorePages = true;
        }
        values = resultsPage.getItem();
        List<Status> responseStories = new ArrayList<>(storyRequest.getLimit());
        Iterator<Map<String, AttributeValue>> it = values.iterator();
        while (it.hasNext()){
            Map<String, AttributeValue> val = it.next();
            String date = val.get(S_KEY_ATTR).getS();
            Map<String, AttributeValue> messageMap = val.get("info").getM();
            String message = messageMap.get("message").getS();     System.out.println(message);
            Status status = new Status(storyRequest.getStory().getUser(), date, message);
            responseStories.add(status);
        }

        return new StoryResponse(responseStories, hasMorePages);
    }

    public Status generatePost(User user, String message){
        return new Status(user, "Thu, Oct 15 2020 11:10:20", message);
    }

    public Status generatePost(User user){
        return new Status(user, "Thu, Oct 15 2020 11:10:20",  "This is a test post");
    }

    public LocalTime getTime(String alias){
        List<Map<String, AttributeValue>>values = new ArrayList<>();
        Database db = new Database("AuthToken", "user_handle", "authToken");
        ResultsPage resultsPage = db.getItems(alias, 1, null);
        values = resultsPage.getItem();
        Iterator<Map<String, AttributeValue>> it = values.iterator();
        String date = null;
        while (it.hasNext()) {
            Status status = null;
            Map<String, AttributeValue> val = it.next();
            Map<String, AttributeValue> messageMap = val.get("info").getM();
            date = messageMap.get("date").getS();
        }


        System.out.println(date);


        LocalTime time = new Gson().fromJson(date, LocalTime.class);

        return time;

    }
}
