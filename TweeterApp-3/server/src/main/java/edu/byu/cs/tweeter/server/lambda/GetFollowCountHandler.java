package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.service.FollowerServiceImpl;
import edu.byu.cs.tweeter.server.service.GetFollowCountServiceImpl;

/**
     * An AWS lambda function that returns the users a user is following.
     */
    public class GetFollowCountHandler implements RequestHandler<LoginRequest, FollowCountResponse> {

        /**
         * Returns the users that the user specified in the request is following. Uses information in
         * the request object to limit the number of followees returned and to return the next set of
         * followees after any that were returned in a previous request.
         *
         * @param request contains the data required to fulfill the request.
         * @param context the lambda context.
         * @return the followees.
         */
        @Override
        public FollowCountResponse handleRequest(LoginRequest request, Context context) {
            GetFollowCountServiceImpl service = new GetFollowCountServiceImpl();
            return service.getTableCount(request);    }
    }

