package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.gson.Gson;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.Dynamodb.ResultsPage;
import edu.byu.cs.tweeter.server.StaticClass.Timeout;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowingDAO {
    // This is the hard coded followee data returned by the 'getFollowees()' method
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";
    Database db;
    private final String TABLE_NAME = "Followee";
    private final String P_KEY_ATTR = "follower_handle";
    private final String S_KEY_ATTR = "followee_handle";
    private final String USER_P_KEY = "alias";
    private final String USER_S_KEY = "image_url";
    private final String USER_TABLE_NAME = "User";

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


    public FollowingDAO(){
        db = new Database(TABLE_NAME, P_KEY_ATTR, S_KEY_ATTR);
    }

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param follower the User whose count of how many following is desired.
     * @return said count.
     */
    public Integer getFolloweeCount(User follower) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert follower != null;
        return getDummyFollowees().size();
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getFollower() != null;

        LocalTime currTime = LocalTime.now();

        LocalTime init = getTime(request.getFollower().getAlias());

        long elapsedMinutes = Duration.between(init, currTime).toMinutes();


        if(elapsedMinutes > 1000){
            throw new RuntimeException("400");
        }


        ResultsPage results = null;
        List<User> users = null;

        Set<String> followeeAlias = new HashSet<>();
        boolean hasMorePages = false;

        System.out.println(request.getFollower().getAlias());
        String lastFollowerAlias = null;

        if(request.getLastFollowee() != null){
            lastFollowerAlias = request.getLastFollowee().getAlias();
        }
        results = db.getFollowees(request.getFollower().getAlias(), request.getLimit(), lastFollowerAlias);

        followeeAlias.addAll(results.getValues());

        if(results.hasLastKey()){
            hasMorePages = true;
        }




        if(followeeAlias.size() > 0) {
            db = new Database(USER_TABLE_NAME, USER_P_KEY, USER_S_KEY);
            List<Item> items = db.getBatchItems(followeeAlias);
            users = getFollowees(items);
        }else {

            List<User> nullUser = new ArrayList<>();
            return new FollowingResponse(nullUser, hasMorePages);
        }




        return new FollowingResponse(users, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFollowee the last followee that was returned in the previous request or null if
     *                     there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFollowee != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFollowee.equals(allFollowees.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    public List<User> getDummyFollowees() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }

    public List<User> getFollowees(List<Item> items){
        List<User> users = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            Item item = items.get(i);
            User user = null;
            String image_url = (String) item.get("image_url");
            String alias = (String) item.get("alias");
            Map<String, Object> info = (Map<String, Object>) item.get("info");
            String firstName = (String) info.get("FirstName");
            String lastName = (String) info.get("LastName");
            user = new User(firstName, lastName, alias, image_url);
            users.add(user);
        }

        return users;
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

