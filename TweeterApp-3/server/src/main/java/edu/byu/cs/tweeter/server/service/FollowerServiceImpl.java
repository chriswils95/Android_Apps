package edu.byu.cs.tweeter.server.service;


import edu.byu.cs.tweeter.model.service.FollowerService;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.dao.FollowerDAO;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowerServiceImpl implements FollowerService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowingDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    @Override
    public FollowerResponse getFollowers(FollowerRequest request) {
        return getFollowerDAO().getFollowers(request);
    }

    /**
     * Returns an instance of {@link FollowingDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowerDAO getFollowerDAO() {
        return new FollowerDAO();
    }
}
