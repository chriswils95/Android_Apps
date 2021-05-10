package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

/**
 * Contains the business logic to support the follow operation.
 */
public interface FollowService {

    public FollowResponse follow(FollowRequest request);

}
