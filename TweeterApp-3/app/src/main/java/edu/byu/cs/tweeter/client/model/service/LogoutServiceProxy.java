package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.client.model.service.response.LogoutResponse;

public class LogoutServiceProxy implements LogoutService {

    private static final String URL_PATH = "/logout";

    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        LogoutResponse logoutResponse = serverFacade.logout(request, URL_PATH);

        if(logoutResponse.isSuccess()) {
            destroyAuthToken(logoutResponse.getAuthToken());
        }

        return logoutResponse;
    }

    public void destroyAuthToken(AuthToken authToken) throws IOException {
        System.out.println("Successfully destroy token");
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
     * method to allow for proper mocking.
     *
     * @return the instance.
     */
     public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
