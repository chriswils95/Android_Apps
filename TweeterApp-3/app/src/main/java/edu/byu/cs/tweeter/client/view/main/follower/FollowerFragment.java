package edu.byu.cs.tweeter.client.view.main.follower;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.StaticClass.StaticHelperClass;
import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.client.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.presenter.FollowerPresenter;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.LoginActivity;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFollowerTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetUserTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowerFragment extends Fragment implements FollowerPresenter.View , MainPresenter.View, LogoutTask.Observer {

    private static final String LOG_TAG = "FollowerFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private User user;
    private AuthToken authToken;
    private FollowerPresenter presenter;
    private MainPresenter mainPresenter;
    private Toast logoutToast;



    private FollowerRecyclerViewAdapter followerRecyclerViewAdapter;
    public FollowerFragment() {
        // Required empty public constructor
    }

    /**
     * 
     * @param user
     * @param authToken
     * @return
     */
    // TODO: Rename and change types and number of parameters
    public static FollowerFragment newInstance(User user, AuthToken authToken) {
        FollowerFragment fragment = new FollowerFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    public void executeAsyncTask(LoginRequest loginRequest){
        GetUserTask getUserTask = new GetUserTask(this.getActivity());
        getUserTask.execute(loginRequest);
    }

    public void executeAsyncTask(LogoutRequest logoutRequest){
        mainPresenter = new MainPresenter(this);
        LogoutTask logoutTask = new LogoutTask(mainPresenter, this);
        logoutTask.execute(logoutRequest);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follower, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new FollowerPresenter(this);

        RecyclerView followerRecyclerView = view.findViewById(R.id.followerRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followerRecyclerView.setLayoutManager(layoutManager);

        followerRecyclerViewAdapter = new FollowerRecyclerViewAdapter();
        followerRecyclerView.setAdapter(followerRecyclerViewAdapter);

        followerRecyclerView.addOnScrollListener(new FollowRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    @Override
    public void logoutSuccessful(LogoutResponse logoutResponse) {
        logoutToast = Toast.makeText(getContext(), "Logging out", Toast.LENGTH_LONG);
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void logoutUnsuccessful(LogoutResponse logoutResponse) {

    }

    @Override
    public void handleException(Exception ex) {

    }


    /**
     * The ViewHolder for the RecyclerView that displays the Following data.
     */
    private class FollowerHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;

        /**
         * Creates an instance and sets an OnClickListener for the user's row.
         *
         * @param itemView the view on which the user will be displayed.
         */
        FollowerHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user = user;
                    StaticHelperClass.setIsUser(false);
                    Toast.makeText(getContext(), "You selected '" + userName.getText() + "'.", Toast.LENGTH_SHORT).show();
                    LoginRequest loginRequest = new LoginRequest((String) userAlias.getText(), "", StaticHelperClass.getMainUser().getAlias());
                    executeAsyncTask(loginRequest);
//                    intent.putExtra(MainActivity.AUTH_TOKEN_KEY, loginResponse.getAuthToken());
                }
            });
        }

        /**
         * Binds the user's data to the view.
         *
         * @param user the user.
         */
        void bindUser(User user) {
            userImage.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));
            userAlias.setText(user.getAlias());
            userName.setText(user.getName());
        }
    }

    /**
     * The adapter for the RecyclerView that displays the Following data.
     */
    private class FollowerRecyclerViewAdapter extends RecyclerView.Adapter<FollowerHolder> implements GetFollowerTask.Observer {

        private final List<User> users = new ArrayList<>();

        private User lastFollower;

        private boolean hasMorePages;
        private boolean isLoading = false;

        /**
         * Creates an instance and loads the first page of following data.
         */
        FollowerRecyclerViewAdapter() {
            loadMoreItems();
        }

        /**
         * Adds new users to the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that items have been added.
         *
         * @param newUsers the users to add.
         */
        void addItems(List<User> newUsers) {
            int startInsertPosition = users.size();
            users.addAll(newUsers);
            this.notifyItemRangeInserted(startInsertPosition, newUsers.size());
        }

        /**
         * Adds a single user to the list from which the RecyclerView retrieves the users it
         * displays and notifies the RecyclerView that an item has been added.
         *
         * @param user the user to add.
         */
        void addItem(User user) {
            users.add(user);
            this.notifyItemInserted(users.size() - 1);
        }

        /**
         * Removes a user from the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that an item has been removed.
         *
         * @param user the user to remove.
         */
        void removeItem(User user) {
            int position = users.indexOf(user);
            users.remove(position);
            this.notifyItemRemoved(position);
        }

        /**
         *  Creates a view holder for a followee to be displayed in the RecyclerView or for a message
         *  indicating that new rows are being loaded if we are waiting for rows to load.
         *
         * @param parent the parent view.
         * @param viewType the type of the view (ignored in the current implementation).
         * @return the view holder.
         */
        @NonNull
        @Override
        public FollowerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FollowerFragment.this.getContext());
            View view;

            if(viewType == LOADING_DATA_VIEW) {
                view =layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.user_row, parent, false);
            }

            return new FollowerHolder(view);
        }

        /**
         * Binds the followee at the specified position unless we are currently loading new data. If
         * we are loading new data, the display at that position will be the data loading footer.
         *
         * @param followingHolder the ViewHolder to which the followee should be bound.
         * @param position the position (in the list of followees) that contains the followee to be
         *                 bound.
         */
        @Override
        public void onBindViewHolder(@NonNull FollowerHolder followingHolder, int position) {
            if(!isLoading) {
                followingHolder.bindUser(users.get(position));
            }
        }

        /**
         * Returns the current number of followees available for display.
         * @return the number of followees available for display.
         */
        @Override
        public int getItemCount() {
            return users.size();
        }

        /**
         * Returns the type of the view that should be displayed for the item currently at the
         * specified position.
         *
         * @param position the position of the items whose view type is to be returned.
         * @return the view type.
         */
        @Override
        public int getItemViewType(int position) {
            return (position == users.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more following
         * data.
         */
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetFollowerTask getFollowerTask = new GetFollowerTask(presenter, this);
            FollowerRequest request = new FollowerRequest(user, PAGE_SIZE, lastFollower);
            getFollowerTask.execute(request);
        }

        /**
         * A callback indicating more following data has been received. Loads the new followees
         * and removes the loading footer.
         *
         * @param followerResponse the asynchronous response to the request to load more items.
         */
        @Override
        public void followersRetrieved(FollowerResponse followerResponse) {
            if(followerResponse.getMessage() != null){
                // It doesn't matter what values we put here. We will be logged in with a hard-coded dummy user.
                LogoutRequest logoutRequest = new LogoutRequest(user.getAlias(), StaticHelperClass.getAuthToken().getToken());
                executeAsyncTask(logoutRequest);
            }else {
                List<User> followees = followerResponse.getFollowers();

                lastFollower = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
                hasMorePages = followerResponse.getHasMorePages();

                isLoading = false;
                removeLoadingFooter();
                followerRecyclerViewAdapter.addItems(followees);
            }
        }

        /**
         * A callback indicating that an exception was thrown by the presenter.
         *
         * @param exception the exception.
         */
        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            removeLoadingFooter();
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * Adds a dummy user to the list of users so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        private void addLoadingFooter() {
            addItem(new User("Dummy", "User", ""));
        }

        /**
         * Removes the dummy user from the list of users so the RecyclerView will stop displaying
         * the loading footer at the bottom of the list.
         */
        private void removeLoadingFooter() {
            removeItem(users.get(users.size() - 1));
        }
    }

    /**
     * A scroll listener that detects when the user has scrolled to the bottom of the currently
     * available data.
     */
    private class FollowRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        FollowRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        /**
         * Determines whether the user has scrolled to the bottom of the currently available data
         * in the RecyclerView and asks the adapter to load more data if the last load request
         * indicated that there was more data to load.
         *
         * @param recyclerView the RecyclerView.
         * @param dx the amount of horizontal scroll.
         * @param dy the amount of vertical scroll.
         */
        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!followerRecyclerViewAdapter.isLoading && followerRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    followerRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }

}