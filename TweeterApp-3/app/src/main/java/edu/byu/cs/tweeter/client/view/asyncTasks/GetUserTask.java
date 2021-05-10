package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import edu.byu.cs.tweeter.client.StaticClass.StaticHelperClass;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.GetUserProxy;
import edu.byu.cs.tweeter.client.model.service.GetUserService;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.client.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.client.presenter.FeedPresenter;
import edu.byu.cs.tweeter.client.presenter.GetUserPresenter;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;

public class GetUserTask extends AsyncTask<LoginRequest, Void, GetUserResponse> {


    private Exception exception;
    private Activity activity;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {

        void handleException(Exception ex);
    }

    /**
     * Creates an instance.
     *
     */
    public GetUserTask(Activity activity) {


        this.activity = activity;


    }

    /**
     * The method that is invoked on a background thread to log the user in. This method is
     * invoked indirectly by calling {@link #execute(LoginRequest...)}.
     *
     * @param loginRequests the request object (there will only be one).
     * @return the response.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected GetUserResponse doInBackground(LoginRequest... loginRequests) {
        GetUserResponse loginResponse = null;

        try {
            loginResponse = getUser(loginRequests[0]);

            if(loginResponse != null) {
                if (loginResponse.isSuccess()) {
                    loadImage(loginResponse.getUser());
                }
            }
        } catch (IOException | TweeterRemoteException ex) {
            exception = ex;
        }

        return loginResponse;
    }

    /**
     * Loads the profile image for the user.
     *
     * @param user the user whose profile image is to be loaded.
     */
    private void loadImage(User user) {
        try {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.toString(), e);
        }
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

    /**
     * Notifies the observer (on the thread of the invoker of the
     * {@link #execute(LoginRequest...)} method) when the task completes.
     *
     * @param loginResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(GetUserResponse loginResponse) {

        if(loginResponse != null) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra(MainActivity.CURRENT_USER_KEY, loginResponse.getUser());
            intent.putExtra(MainActivity.AUTH_TOKEN_KEY, loginResponse.getAuthToken());
            intent.putExtra(MainActivity.IS_FOLLOW, loginResponse.isFollowing());
            activity.startActivity(intent);
        }
        else  {
            Toast.makeText(activity.getApplicationContext(), "User not in Database.", Toast.LENGTH_SHORT).show();
        }

    }
}
