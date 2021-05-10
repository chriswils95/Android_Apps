package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.service.RegisterServiceImpl;

/**
     * An AWS lambda function that returns the users a user is following.
     */
    public class GetRegisterHandler implements RequestHandler<RegisterRequest, RegisterResponse> {

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
        public RegisterResponse handleRequest(RegisterRequest request, Context context) {
            RegisterServiceImpl service = new RegisterServiceImpl();
            System.out.println(request.getFirstName());
            System.out.println(request.getImageString());

            try {
                return service.register(request);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

}
