package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.service.PostServiceImpl;

public class GetPostHandler implements RequestHandler<PostRequest, PostResponse>{
    @Override
    public PostResponse handleRequest(PostRequest input, Context context) {
        PostServiceImpl postService = new PostServiceImpl();
        try {
            return postService.post(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
