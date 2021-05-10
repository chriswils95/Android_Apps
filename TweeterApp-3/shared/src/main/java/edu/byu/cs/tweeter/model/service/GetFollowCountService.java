package edu.byu.cs.tweeter.model.service;

import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public interface GetFollowCountService {

    public FollowCountResponse getTableCount(LoginRequest loginRequest);
}
