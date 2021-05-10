package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.FollowerServiceProxy;
import edu.byu.cs.tweeter.client.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowerResponse;

/**
 * The presenter for the "following" functionality of the application.
 */
public class FollowerPresenter {

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
    public FollowerPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowerResponse getFollower(FollowerRequest request) throws IOException, TweeterRemoteException {
        FollowerService followerService = getFollowerService();
        return followerService.getFollowers(request);
    }

    /**
     * Returns an instance of {@link FollowerService}. Allows mocking of the FollowingService class
     * for testing purposes. All usages of FollowingService should get their FollowingService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowerService getFollowerService() {
        return new FollowerServiceProxy();
    }
}
