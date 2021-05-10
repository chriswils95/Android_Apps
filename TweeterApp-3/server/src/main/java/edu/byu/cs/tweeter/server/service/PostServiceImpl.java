package edu.byu.cs.tweeter.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.PostDAO;

public class PostServiceImpl implements PostService {
    @Override
    public PostResponse post(PostRequest request) throws Exception {
        return makePost().post(request);
    }

    PostDAO makePost(){return new PostDAO();}
}
