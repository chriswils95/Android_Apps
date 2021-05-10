package edu.byu.cs.tweeter.client.model.net;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.BuildConfig;
import edu.byu.cs.tweeter.client.StaticClass.StaticHelperClass;
import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.client.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.client.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.client.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.client.model.service.request.PostRequest;
import edu.byu.cs.tweeter.client.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.client.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.client.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.client.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.client.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.client.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.client.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.client.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.client.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.client.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.model.service.response.PostResponse;
import edu.byu.cs.tweeter.client.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.client.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {


    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private List<User> currentUsersInDatabase;
    private static List<User> allUserFollowers = new ArrayList<>();
    private static List<User> allUserFollowees;
    private static User current_user = null;
    private static List<Status> stories = new ArrayList<>();;

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



    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://ncv5b0d919.execute-api.us-west-2.amazonaws.com/post";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Loads the profile image data for the user.
     *
     */
    private void loadAllImages() throws IOException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < currentUsersInDatabase.size();i++){
            User user = currentUsersInDatabase.get(i);
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
            users.add(user);
        }

        StaticHelperClass.setListOfUser(users);

    }

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        LoginResponse response = clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
        current_user = response.getUser();
        StaticHelperClass.setCurrentUser(current_user);
        if(response.isSuccess()) {
            StaticHelperClass.setAuthToken(response.getAuthToken());
            currentUsersInDatabase = getDummyUsers();
            getUsersFollowersAndFollowees();
            FollowCountResponse followCountResponse = getTableCount(request);
            StaticHelperClass.setNumOfFollowees(followCountResponse.getFolloweeCount());
            StaticHelperClass.setNumOfFollowers(followCountResponse.getFollowerCount());
            stories = getDummyStories();
            loadAllImages();
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public LogoutResponse logout(LogoutRequest logoutRequest, String urlPath) throws IOException, TweeterRemoteException {
        System.out.println("successfully log user out");
        LogoutResponse response = clientCommunicator.doPost(urlPath, logoutRequest, null, LogoutResponse.class);
        if(response.isSuccess()) {
            StaticHelperClass.setNumOfFollowees(0);
            StaticHelperClass.setNumOfFollowers(0);
            return response;
        }else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public FollowCountResponse getTableCount(LoginRequest loginRequest) throws IOException, TweeterRemoteException {
        String urlPath = "/itemcount";
        FollowCountResponse response = clientCommunicator.doPost(urlPath, loginRequest,
                null, FollowCountResponse.class);
        if(response != null) {
                return response;
        }


        return new FollowCountResponse(0, 0);

    }


    public FollowResponse follow(FollowRequest followRequest, String urlPath) throws IOException, TweeterRemoteException {
        System.out.println("successfully follow user");
        followRequest.getFollowee().setImageBytes(null);
        followRequest.getFollower().setImageBytes(null);

        FollowResponse response = clientCommunicator.doPost(urlPath, followRequest, null, FollowResponse.class);
        if(response.isSuccess()) {
            if (StaticHelperClass.isIsRegister()) {
                if (followRequest.isUnFollow()) {
                    allUserFollowers.remove(followRequest.getFollowee());
                } else {
                    allUserFollowers.add(followRequest.getFollowee());
                }
            }
            return new FollowResponse(followRequest.getFollower(), followRequest.getFollowee());
        }else {
            throw new RuntimeException(response.getMessage());
        }
    }




    public GetUserResponse getUser(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        GetUserResponse response = clientCommunicator.doPost(urlPath, request, null, GetUserResponse.class);
        if (response.isSuccess()) {
            FollowCountResponse followCountResponse = getTableCount(request);
            StaticHelperClass.setNumOfFollowees(followCountResponse.getFolloweeCount());
            StaticHelperClass.setNumOfFollowers(followCountResponse.getFollowerCount());
            StaticHelperClass.setAuthToken(response.getAuthToken());
            return response;
        }

        return null;
    }



    public void getUsersFollowersAndFollowees(){
        allUserFollowees = getDummyFollowees();
        allUserFollowers= getDummyFollowers();
    }



    public RegisterResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        RegisterResponse response = clientCommunicator.doPost(urlPath, request, null, RegisterResponse.class);
        if(response.isSuccess()) {
            stories = new ArrayList<>();
            currentUsersInDatabase = getDummyUsers();
            loadAllImages();
            StaticHelperClass.setAuthToken(response.getAuthToken());
            StaticHelperClass.setCurrentUser(response.getUser());
            return response;
        }else {
            throw new RuntimeException(response.getMessage());
        }
    }



    public  void populateStoryList(List<Status> statuses) {
        List<Status> mylist = StaticHelperClass.getFeeds();

        if(stories != null) {
            for (int i = 0; i < statuses.size(); i++){
                boolean in_here = false;
                for (int j = 0; j < stories.size(); j++)
                if(stories.get(j).getUser().getAlias().equals(statuses.get(i).getUser().getAlias())){
                    in_here = true;
                }

                if(!in_here){
                    stories.add(statuses.get(i));
                }

            }
        }
        else {
            stories = statuses;
        }
    }


   public PostResponse post(PostRequest postRequest, String urlPath) throws IOException, TweeterRemoteException {
       PostResponse response = clientCommunicator.doPost(urlPath, postRequest, null, PostResponse.class);
       if(response.isSuccess()) {
           stories.add(postRequest.getPost());
           return new PostResponse(postRequest.getPost());
       }else {
           return new PostResponse(response.getPost());
       }
   }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public FeedResponse getFeeds(FeedRequest feedRequest, String urlPath) throws IOException, TweeterRemoteException {
        boolean hasMorePages = false;
        FeedResponse response = null;
        List<Status> responseFeeds = new ArrayList<>(feedRequest.getLimit());
        try {
            response = clientCommunicator.doPost(urlPath, feedRequest, null, FeedResponse.class);
            if (response.isSuccess()) {
                if (!StaticHelperClass.isIsRegister()) {
                    responseFeeds = response.getUserFollowersFeed();
                }
                return new FeedResponse(responseFeeds, hasMorePages);
            } else {
                throw new RuntimeException(response.getMessage());
            }
        }catch (Exception e){
            System.out.println("error");
            return new FeedResponse("error");
        }
    }


    public StoryResponse getStories(StoryRequest storyRequest, String urlPath) throws IOException, TweeterRemoteException {
        boolean hasMorePages = false;
        List<Status> responseStories = new ArrayList<>(storyRequest.getLimit());
        try {
            StoryResponse response = clientCommunicator.doPost(urlPath, storyRequest, null, StoryResponse.class);
            if (response.isSuccess()) {
                    responseStories = response.getUserStories();
                return new StoryResponse(responseStories, hasMorePages);
            } else {
                throw new RuntimeException(response.getMessage());
            }
        }catch (Exception e){
            System.out.println("error");
            return new StoryResponse("error");

        }

    }






    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */

    public FollowingResponse getFollowees(FollowingRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        try {
            FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);

            if (response.isSuccess()) {
                if (!StaticHelperClass.isIsRegister()) {
                    return response;
                } else {
                    allUserFollowees = new ArrayList<User>();
                    return new FollowingResponse(allUserFollowees, false);
                }
            } else {
                throw new RuntimeException(response.getMessage());
            }
        }catch (Exception e){
            System.out.println("error");
            return new FollowingResponse("error");
        }
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */

    public FollowerResponse getFollowers(FollowerRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        try {
            FollowerResponse response = clientCommunicator.doPost(urlPath, request, null, FollowerResponse.class);

            if (response.isSuccess()) {

                return response;
            } else {
                throw new RuntimeException(response.getMessage());
            }
        }catch (Exception e){
            return  new FollowerResponse("error");
        }
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



    @RequiresApi(api = Build.VERSION_CODES.O)
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




    @RequiresApi(api = Build.VERSION_CODES.O)
    List<Status> getDummyStories(){
        List<Status> stories = new ArrayList<>();
        PostGenerator postGenerator = new PostGenerator();
        Status status = postGenerator.generatePost(current_user);
        stories.add(status);
        PostGenerator postGenerator1 = new PostGenerator();
        Status status1 = postGenerator1.generatePost(current_user);
        stories.add(status1);
        PostGenerator postGenerator2 = new PostGenerator();
        Status status2 = postGenerator2.generatePost(current_user, "www.google.com");
        stories.add(status2);
        PostGenerator postGenerator3 = new PostGenerator();
        Status status3 = postGenerator3.generatePost(current_user, user1.getAlias());
        stories.add(status3);
        PostGenerator postGenerator4 = new PostGenerator();
        Status status4 = postGenerator4.generatePost(current_user);
        stories.add(status4);
        PostGenerator postGenerator5 = new PostGenerator();
        Status status5 = postGenerator5.generatePost(current_user);
        stories.add(status5);
        PostGenerator postGenerator6 = new PostGenerator();
        Status status6 = postGenerator6.generatePost(current_user);
        stories.add(status6);
        PostGenerator postGenerator7 = new PostGenerator();
        Status status7 = postGenerator7.generatePost(current_user);
        stories.add(status7);
        PostGenerator postGenerator8 = new PostGenerator();
        Status status8 = postGenerator8.generatePost(current_user);
        stories.add(status8);
        PostGenerator postGenerator9 = new PostGenerator();
        Status status9 = postGenerator9.generatePost(current_user);
        stories.add(status9);

        return stories;
    }


    
    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the generator.
     */
    List<User> getDummyFollowers() {
        if(StaticHelperClass.isIsRegister()){
            return new ArrayList<User>();
        }
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }


    /**
     * Returns dummy list of all users
     */
    List<User> getDummyUsers() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }


    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the generator.
     */
    List<User> getDummyFollowees() {
        if(StaticHelperClass.isIsRegister()){
            return new ArrayList<User>();
        }
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }


    FollowGenerator getFollowGenerator() {
        return FollowGenerator.getInstance();}
}
