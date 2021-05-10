package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.GetUserService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class GetUserImpl implements GetUserService {
    @Override
    public GetUserResponse getUser(LoginRequest request) throws IOException, TweeterRemoteException {
        return getUserDAO().getUser(request);
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }

}
