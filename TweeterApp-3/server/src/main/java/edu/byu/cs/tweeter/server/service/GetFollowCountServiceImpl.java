package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.GetFollowCountService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.GetFollowCountDA0;

public class GetFollowCountServiceImpl implements GetFollowCountService {
    @Override
    public FollowCountResponse getTableCount(LoginRequest loginRequest) {
        return new GetFollowCountDA0().getTableCount(loginRequest);
    }
}
