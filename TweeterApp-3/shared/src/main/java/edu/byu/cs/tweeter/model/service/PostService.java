package edu.byu.cs.tweeter.model.service;


import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

/**
 * Contains the business logic to support the login operation.
 */
public interface PostService {

    public PostResponse post(PostRequest request) throws Exception;

}
