package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.LogoutDAO;

public class LogoutServiceImpl implements LogoutService {
    @Override
    public LogoutResponse logout(LogoutRequest request) {
        destroyAuthToken(new AuthToken(request.getToken()));
        return getLogoutDAO().logout(request);
    }

    @Override
    public void destroyAuthToken(AuthToken authToken) {
        System.out.println("Sucessfully destroy authToken");
    }

    public LogoutDAO getLogoutDAO(){return new LogoutDAO();
    }
}
