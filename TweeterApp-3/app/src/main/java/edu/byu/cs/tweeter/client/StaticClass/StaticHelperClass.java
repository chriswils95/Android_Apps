package edu.byu.cs.tweeter.client.StaticClass;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.client.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.client.model.service.response.LoginResponse;


public final class StaticHelperClass {

   public  static  List<User> listOfUsers;

   public static List<Status> feeds = new ArrayList<Status>();
    public static List<Status> stories = new ArrayList<Status>();

    public static boolean isPost;
    public static AuthToken authToken;
    public static int userClicked;

    public static int getUserClicked() {
        return userClicked;
    }

    public static void setUserClicked(int userClicked) {
        StaticHelperClass.userClicked = userClicked;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        StaticHelperClass.currentUser = currentUser;
    }

    public static User currentUser;

    public static User getMainUser() {
        return mainUser;
    }

    public static void setMainUser(User mainUser) {
        StaticHelperClass.mainUser = mainUser;
    }

    public static User mainUser;


    public static boolean isIsPost() {
        return isPost;
    }

    public static void setIsPost(boolean isPost) {

        StaticHelperClass.isPost = isPost;
    }

    public static Status post;

    public static boolean isIs_follow() {
        return is_follow;
    }

    public static void setIs_follow(boolean is_follow) {

        StaticHelperClass.is_follow = is_follow;
    }

    public static boolean is_follow;


    public static AuthToken getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(AuthToken authToken) {
        StaticHelperClass.authToken = authToken;
    }

    public static Status getPost() {
        return post;
    }

    public static void setPost(Status post) {
        StaticHelperClass.post = post;
    }

    public static void appendFeeds(Status status){
        stories.add(status);
    }


    public static List<Status> getStories() {
        return stories;
    }

    public static void setStories(List<Status> stories) {
        StaticHelperClass.stories = stories;
    }

    public static Status getUserFeed(String alias){
       for (int i = 0; i < feeds.size();i++){
           if(feeds.get(i).getUser().getAlias().equals(alias)){
               return feeds.get(i);
           }
       }

       return  null;
   }

    public static List<Status> getFeeds() {
        return feeds;
    }

    public static void setFeeds(List<Status> feeds) {
        feeds = feeds;
    }

    public static boolean isUser;

   public static boolean isRegister;

   public static int numOfFollowers;

   public static int numOfFollowees;

    public static int getNumOfFollowers() {
        return numOfFollowers;
    }

    public static void setNumOfFollowers(int numOfFollowers) {
        StaticHelperClass.numOfFollowers = numOfFollowers;
    }

    public static int getNumOfFollowees() {
        return numOfFollowees;
    }

    public static void setNumOfFollowees(int numOfFollowees) {
        StaticHelperClass.numOfFollowees = numOfFollowees;
    }

    public static boolean isIsRegister() {
        return isRegister;
    }

    public static void setIsRegister(boolean isRegister) {
        StaticHelperClass.isRegister = isRegister;
    }

    public static boolean isIsUser() {
        return isUser;
    }

    public static void setIsUser(boolean isUser) {
        StaticHelperClass.isUser = isUser;
    }

    private StaticHelperClass(){
        listOfUsers = new ArrayList<User>();
    }

    public static User getUser(String alias) throws IOException, TweeterRemoteException {

        LoginRequest request = new LoginRequest(alias, "Isatu1997");
        ServerFacade facade = new ServerFacade();
        GetUserResponse response = facade.getUser(request, "/login");
//        for (int i = 0; i < listOfUsers.size(); i++){
//          if(listOfUsers.get(i).getAlias().equals(alias)){
//              return listOfUsers.get(i);
//          }
//      }

        if (response != null){
            return response.getUser();
        }



      return null;
    }

    public static void setListOfUser(List<User> listOfUsers){
        StaticHelperClass.listOfUsers = listOfUsers;
    }
}
