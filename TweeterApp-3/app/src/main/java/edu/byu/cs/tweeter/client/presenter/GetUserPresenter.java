package edu.byu.cs.tweeter.client.presenter;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.GetUserProxy;
import edu.byu.cs.tweeter.client.model.service.GetUserService;
import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.client.model.service.response.LoginResponse;

/**
 * The presenter for the login functionality of the application.
 */
public class GetUserPresenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public GetUserPresenter(View view) {
        this.view = view;
    }

    /**
     * Makes a login request.
     *
     * @param loginRequest the request.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public GetUserResponse getUser(LoginRequest loginRequest) throws IOException, TweeterRemoteException {
        GetUserService getUserService = new GetUserProxy();
        return getUserService.getUser(loginRequest);
    }



}
