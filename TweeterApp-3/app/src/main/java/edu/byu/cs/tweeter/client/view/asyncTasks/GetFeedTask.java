package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.client.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.client.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.client.presenter.FeedPresenter;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

/**
     * An {@link AsyncTask} for retrieving followees for a user.
     */
    public class GetFeedTask extends AsyncTask<FeedRequest, Void, FeedResponse> {

        private  FeedPresenter presenter;
        private  Observer observer;
        private Exception exception;

        /**
         * An observer interface to be implemented by observers who want to be notified when this task
         * completes.
         */
        public interface Observer {
            void feedsRetrieved(FeedResponse feedResponse);
            void handleException(Exception exception);
        }

        /**
         * Creates an instance.
         *
         * @param presenter the presenter from whom this task should retrieve followees.
         * @param observer the observer who wants to be notified when this task completes.
         */
        public GetFeedTask(FeedPresenter presenter, Observer observer) {
            if(observer == null) {
                throw new NullPointerException();
            }

            this.presenter = presenter;
            this.observer = observer;
        }




        /**
         * The method that is invoked on the background thread to retrieve followees. This method is
         * invoked indirectly by calling {@link #execute(FeedRequest...)}.
         *
         * @param feedRequests the request object (there will only be one).
         * @return the response.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected FeedResponse doInBackground(FeedRequest... feedRequests) {

            FeedResponse response = null;

            try {
                response = presenter.getFeeds(feedRequests[0]);
            } catch (IOException | TweeterRemoteException ex) {
                exception = ex;
            }

            return response;
        }



        /**
         * Notifies the observer (on the UI thread) when the task completes.
         *
         * @param feedResponse the response that was received by the task.
         */
        @Override
        protected void onPostExecute(FeedResponse feedResponse) {
            if(exception != null) {
                observer.handleException(exception);
            } else {
                observer.feedsRetrieved(feedResponse);
            }
        }
    }

