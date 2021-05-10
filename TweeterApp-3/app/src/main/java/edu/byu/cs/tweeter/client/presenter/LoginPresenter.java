package edu.byu.cs.tweeter.client.presenter;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.response.LoginResponse;

/**
 * The presenter for the login functionality of the application.
 */
public class LoginPresenter {

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
    public LoginPresenter(View view) {
        this.view = view;
    }

    /**
     * Makes a login request.
     *
     * @param loginRequest the request.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LoginResponse login(LoginRequest loginRequest) throws IOException, TweeterRemoteException {
        LoginService loginService = new LoginServiceProxy();
        return loginService.login(loginRequest);
    }


    /**
     * Returns an instance of {@link LoginService}. Allows mocking of the LoginService class
     * for testing purposes. All usages of LoginService should get their LoginService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    LoginService getLoginService() {
        return new LoginServiceProxy();
    }

}
