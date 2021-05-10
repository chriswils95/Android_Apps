package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.FollowServiceProxy;
import edu.byu.cs.tweeter.client.model.service.LogoutService;
import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.client.model.service.PostService;
import edu.byu.cs.tweeter.client.model.service.PostServiceProxy;
import edu.byu.cs.tweeter.client.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.client.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.client.model.service.request.PostRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.client.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.model.service.response.PostResponse;

/**
 * The presenter for the login functionality of the application.
 */
public class MainPresenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public MainPresenter(View view) {
        this.view = view;
    }

    /**
     * Makes a login request.
     *
     * @param logoutRequest the request.
     */
    public LogoutResponse logout(LogoutRequest logoutRequest) throws IOException, TweeterRemoteException {
        LogoutService logoutService = new LogoutServiceProxy();
        return logoutService.logout(logoutRequest);
    }

    public FollowResponse follow(FollowRequest followRequest) throws IOException, TweeterRemoteException {
        FollowService followService = new FollowServiceProxy();
        return followService.follow(followRequest);
    }

    public PostResponse post(PostRequest postRequest) throws IOException, TweeterRemoteException {
        PostService postService = new PostServiceProxy();
        return postService.post(postRequest);
    }


    /**
     * Returns an instance of {@link FollowService}. Allows mocking of the FollowService class
     * for testing purposes. All usages of FollowService should get their FollowService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowService getFollowService() {
        return new FollowServiceProxy();
    }




    /**
     * Returns an instance of {@link PostService}. Allows mocking of the PostService class
     * for testing purposes. All usages of PostService should get their PostService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    PostService getPostService() {
        return new PostServiceProxy();
    }


    /**
     * Returns an instance of {@link LogoutService}. Allows mocking of the LogoutService class
     * for testing purposes. All usages of PostService should get their LogoutService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    LogoutService getLogoutService() {
        return new LogoutServiceProxy();
    }
}
