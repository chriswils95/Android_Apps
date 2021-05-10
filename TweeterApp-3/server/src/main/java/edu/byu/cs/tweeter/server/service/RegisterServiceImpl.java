package edu.byu.cs.tweeter.server.service;


import java.io.IOException;

import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.FollowerDAO;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.RegisterDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class RegisterServiceImpl implements RegisterService {

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
    public RegisterResponse register(RegisterRequest request) throws Exception {
        return getRegisterDAO().getRegister(request);
    }

    /**
     * Returns an instance of {@link RegisterDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    RegisterDAO getRegisterDAO() {
        return new RegisterDAO();
    }


}
