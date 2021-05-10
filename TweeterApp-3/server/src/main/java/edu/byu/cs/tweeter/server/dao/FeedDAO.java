package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.gson.Gson;

import java.awt.SystemTray;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.Dynamodb.ResultsPage;
import edu.byu.cs.tweeter.server.StaticClass.Timeout;

public class FeedDAO {


    private final String TABLE_NAME = "Feed";
    private final String P_KEY_ATTR = "user_alias";
    private final String S_KEY_ATTR = "date";
    private final String F_TABLE_NAME = "Follower";
    private final String F_P_KEY_ATTR = "followee_handle";
    private final String F_S_KEY_ATTR = "follower_handle";

    Database db;

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);
    private final User user6 = new User("Cindy", "Coats", FEMALE_IMAGE_URL);
    private final User user7 = new User("Dan", "Donaldson", MALE_IMAGE_URL);
    private final User user8 = new User("Dee", "Dempsey", FEMALE_IMAGE_URL);
    private final User user9 = new User("Elliott", "Enderson", MALE_IMAGE_URL);
    private final User user10 = new User("Elizabeth", "Engle", FEMALE_IMAGE_URL);
    private final User user11 = new User("Frank", "Frandson", MALE_IMAGE_URL);
    private final User user12 = new User("Fran", "Franklin", FEMALE_IMAGE_URL);
    private final User user13 = new User("Gary", "Gilbert", MALE_IMAGE_URL);
    private final User user14 = new User("Giovanna", "Giles", FEMALE_IMAGE_URL);
    private final User user15 = new User("Henry", "Henderson", MALE_IMAGE_URL);
    private final User user16 = new User("Helen", "Hopwell", FEMALE_IMAGE_URL);
    private final User user17 = new User("Igor", "Isaacson", MALE_IMAGE_URL);
    private final User user18 = new User("Isabel", "Isaacson", FEMALE_IMAGE_URL);
    private final User user19 = new User("Justin", "Jones", MALE_IMAGE_URL);
    private final User user20 = new User("Jill", "Johnson", FEMALE_IMAGE_URL);
    User current_user = new User("Test", "User",
            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

    public FeedDAO(){
        db = new Database(F_TABLE_NAME, F_P_KEY_ATTR, F_S_KEY_ATTR);
    }


    List<Status> getDummyFeeds(){
        List<Status> feeds = new ArrayList<>();
        PostGenerator postGenerator = new PostGenerator();
        Status status = postGenerator.generatePost(user1, "This is a test post");
        feeds.add(status);
        Status status1 = postGenerator.generatePost(user2, "This is a test post");
        feeds.add(status1);
        Status status2 = postGenerator.generatePost(user3, "This is a test post");
        feeds.add(status2);
        Status status3 = postGenerator.generatePost(user4, user1.getAlias());
        feeds.add(status3);
        Status status4 = postGenerator.generatePost(user5, user2.getAlias());
        feeds.add(status4);
        Status status5 = postGenerator.generatePost(user6, user3.getAlias());
        feeds.add(status5);
        Status status6 = postGenerator.generatePost(user7, user4.getAlias());
        feeds.add(status6);
        Status status7 = postGenerator.generatePost(user8, "www.google.com");
        feeds.add(status7);
        Status status8 = postGenerator.generatePost(user9, "www.facebook.com");
        feeds.add(status8);
        Status status9 = postGenerator.generatePost(user10, "This is a test post");
        feeds.add(status9);
        Status status11 = postGenerator.generatePost(user11,  "This is a test post");
        feeds.add(status11);
        Status status12 = postGenerator.generatePost(user12, "This is a test post");
        feeds.add(status12);
        Status status13 = postGenerator.generatePost(user13, "This is a test post");
        feeds.add(status13);
        Status status14 = postGenerator.generatePost(user14, "This is a test post");
        feeds.add(status14);
        Status status15 = postGenerator.generatePost(user15, "This is a test post");
        feeds.add(status15);
        Status status16 = postGenerator.generatePost(user16, "This is a test post");
        feeds.add(status16);
        Status status17 = postGenerator.generatePost(user17, "This is a test post");
        feeds.add(status17);
        Status status18 = postGenerator.generatePost(user18, "This is a test post");
        feeds.add(status18);
        Status status19 = postGenerator.generatePost(user19, "This is a test post");
        feeds.add(status19);
        Status status20 = postGenerator.generatePost(user20, "This is a test post");
        feeds.add(status20);

        return feeds;
    }
    public FeedResponse getFeeds(FeedRequest feedRequest){

        LocalTime currTime = LocalTime.now();

        LocalTime init = getTime(feedRequest.getFeed().getUser().getAlias());

        long elapsedMinutes = Duration.between(init, currTime).toMinutes();

        System.out.println(elapsedMinutes);


        if(elapsedMinutes > 1000){
            throw new RuntimeException("BadRequest");
        }
        boolean hasMorePages = false;

        List<Map<String, AttributeValue>>values = new ArrayList<>();


        ResultsPage results = null;

        List<Status> users = null;


        List<Status> responseFeeds = new ArrayList<>(feedRequest.getLimit());
        List<Status> responseStories = new ArrayList<>(feedRequest.getLimit());



        Set<String> followeeAlias = new HashSet<>();

        String userAlias = feedRequest.getFeed().getUser().getAlias();

        String lastFollowerAlias = null;

        if(feedRequest.getLastFeed() != null){
            lastFollowerAlias = feedRequest.getLastFeed().getUser().getAlias();
        }
        results = db.getFollowers(userAlias, feedRequest.getLimit(), lastFollowerAlias);
//
        followeeAlias.addAll(results.getValues());

        if(results.hasLastKey()){
            hasMorePages = true;
        }
//
        if(followeeAlias.size() > 0) {
            db = new Database(TABLE_NAME, P_KEY_ATTR, S_KEY_ATTR);
            ResultsPage resultsPage = db.getItems(userAlias, feedRequest.getLimit(), lastFollowerAlias);

            values = resultsPage.getItem();
            Iterator<Map<String, AttributeValue>> it = values.iterator();
            while (it.hasNext()) {
                Status status = null;
                Map<String, AttributeValue> val = it.next();
                Map<String, AttributeValue> messageMap = val.get("info").getM();
                String date = val.get("date").getS();
                String message = messageMap.get("Status").getS();

                if (!message.isEmpty()) {
                    status = new Gson().fromJson(message, Status.class);
                }

                responseStories.add(status);
            }
        }

        return new FeedResponse(responseStories, hasMorePages);
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
