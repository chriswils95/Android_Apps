package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.client.model.service.response.StoryResponse;


import edu.byu.cs.tweeter.client.presenter.StoryPresenter;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

/**
 * An {@link AsyncTask} for retrieving followees for a user.
 */
public class GetStoryTask extends AsyncTask<StoryRequest, Void, StoryResponse> {

    private final StoryPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void storiesRetrieved(StoryResponse storyResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followees.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetStoryTask(StoryPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followees. This method is
     * invoked indirectly by calling {@link #execute(StoryRequest...)}.
     *
     * @param storyRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected StoryResponse doInBackground(StoryRequest... storyRequests) {

        StoryResponse response = null;

        try {
            response = presenter.getStories(storyRequests[0]);
            if(response.isSuccess()) {
                loadImages(response);
            }
        } catch (IOException | TweeterRemoteException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Loads the profile image data for each followee included in the response.
     *
     * @param response the response from the followee request.
     */
    public void loadImages(StoryResponse response) throws IOException {
        for(edu.byu.cs.tweeter.client.model.domain.Status status : response.getUserStories()) {
            User user = status.getUser();
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        }
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param storyResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(StoryResponse storyResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.storiesRetrieved(storyResponse);
        }
    }



}

