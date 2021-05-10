package edu.byu.cs.tweeter.client.view.main.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.byu.cs.tweeter.client.StaticClass.StaticHelperClass;
import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.client.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.client.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.client.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.presenter.FeedPresenter;
import edu.byu.cs.tweeter.client.presenter.GetUserPresenter;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.LoginActivity;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFeedTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetUserTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;
import edu.byu.cs.tweeter.client.model.domain.Status;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment implements FeedPresenter.View,MainPresenter.View, LogoutTask.Observer {

    private static final String LOG_TAG = "FeedFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private User user;
    private AuthToken authToken;
    private FeedPresenter presenter;
    private MainPresenter mainPresenter;
    private Status status;
    private int mentioned_start = -1;
    private int url_start = -1;
    private String url =null;
    private String mentioned =null;
    boolean is_mentioned = false;
    boolean is_url = false;
    Context context;
    private Toast logoutToast;
    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @param authToken Parameter 2.
     * @return A new instance of fragment feedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(User user, AuthToken authToken) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new FeedPresenter(this);


        RecyclerView feedRecyclerView = view.findViewById(R.id.feedRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        feedRecyclerView.setLayoutManager(layoutManager);

        feedRecyclerViewAdapter = new FeedFragment.FeedRecyclerViewAdapter();
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);

        feedRecyclerView.addOnScrollListener(new FeedFragment.FeedRecyclerViewPaginationScrollListener(layoutManager));
        return view;

    }

    private ArrayList pullLinks(String text) {
        ArrayList links = new ArrayList();

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while(m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")"))
            {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            links.add(urlStr);
        }
        return links;
    }

    private String getUserMentions(String message){
        int aliasIndex = message.indexOf("@");

        String mentioned = "";

        if(aliasIndex != -1) {
            char at = message.charAt(aliasIndex);
            mentioned_start = aliasIndex;
            while (at != ' ' && (aliasIndex != message.length() - 1)) {
                mentioned += at;
                aliasIndex += 1;
                System.out.println(at);
                at = message.charAt(aliasIndex);
            }
            mentioned += at;
        }else {
            mentioned = null;
        }
        return mentioned;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    String getPostDate(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");

        String formattedDate = myDateObj.format(myFormatObj);

        return formattedDate;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private String splitPosts(String message){

        boolean is_url_or_mentioned = false;
        is_url = false;
        is_mentioned = false;
        String status = "";
        ArrayList<String> links = null;
        mentioned = getUserMentions(message);
        links = pullLinks(message);
        if (mentioned != null) {
            message = message.replaceFirst(mentioned, "");
            status = "Post mention: " + mentioned;
            is_url_or_mentioned = true;
            is_mentioned = true;
        }

        if(links.size() > 0){
            is_url = true;
            url = links.get(0);
//            SpannableString text = new SpannableString(url);
//            text.setSpan(new UnderlineSpan(), 0, url.length() -1, 0);
            message = message.replaceFirst(url, "");
            status = status + "Post url: " + url + " ";
            is_url_or_mentioned = true;
            status = status + message;
        }else {
            status = status + " " + message;
        }


        if(is_url && is_mentioned){
            mentioned_start = 14;
            url_start = 13 + (mentioned.length() + 1) + 10;
        }else {
            if(is_url){
                url_start = 10;
            }

            if(is_mentioned){
                mentioned_start = 14;
            }
        }


        if(!is_url_or_mentioned) {
            status = message;
        }


        return status;
    }

    public void executeAsyncTask(LogoutRequest logoutRequest){
        mainPresenter = new MainPresenter(this);
        LogoutTask logoutTask = new LogoutTask(mainPresenter, this);
        logoutTask.execute(logoutRequest);
    }


    public void executeAsyncTask(LoginRequest loginRequest){
        GetUserTask getUserTask = new GetUserTask(this.getActivity());
        getUserTask.execute(loginRequest);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
    private class FeedHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView message;
        private final TextView date_and_time;

        /**
         * Creates an instance and sets an OnClickListener for the user's row.
         *
         * @param itemView the view on which the user will be displayed.
         */
        FeedHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);
            date_and_time = itemView.findViewById(R.id.date);
            message =itemView.findViewById(R.id.userMessage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        /**
         * Binds the user's data to the view.
         *
         * @param feed the user.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        void bindUser(Status feed) {
            userImage.setImageDrawable(ImageUtils.drawableFromByteArray(feed.getUser().getImageBytes()));
            userAlias.setText(feed.getUser().getAlias());
            userName.setText(feed.getUser().getName());
            date_and_time.setText(feed.getDate());
            String text = splitPosts(feed.getMessage());
            SpannableString ss = new SpannableString(text);
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    String text = splitPosts(feed.getMessage());
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://" + url));
                    Toast.makeText(getContext(), "One", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.BLUE);
                    ds.setUnderlineText(false);
                }
            };
            ClickableSpan clickableSpan2 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    StaticHelperClass.setIsUser(false);
                    String text = splitPosts(feed.getMessage());
                    System.out.println(mentioned);
                    LoginRequest loginRequest = new LoginRequest(mentioned, "", StaticHelperClass.getMainUser().getAlias());
                    executeAsyncTask(loginRequest);

                }
            };

            if(is_url && is_mentioned){
                ss.setSpan(clickableSpan1, url_start, url_start + url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(clickableSpan2, mentioned_start, mentioned_start + mentioned.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                if (is_url) {
                    ss.setSpan(clickableSpan1, url_start, url_start + url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (is_mentioned) {
                    ss.setSpan(clickableSpan2, mentioned_start, mentioned_start + mentioned.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }


            message.setText(ss);
            message.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedFragment.FeedHolder> implements GetFeedTask.Observer {

        private final List<Status> feeds = new ArrayList<>();

        private Status lastFeeds;

        private boolean hasMorePages;
        private boolean isLoading = false;

        /**
         * Creates an instance and loads the first page of following data.
         */
        FeedRecyclerViewAdapter() {
            loadMoreItems();
        }

        /**
         * Adds new users to the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that items have been added.
         *
         * @param newFeeds the users to add.
         */
        void addItems(List<Status> newFeeds) {
            int startInsertPosition = feeds.size();
            feeds.addAll(newFeeds);
            this.notifyItemRangeInserted(startInsertPosition, newFeeds.size());
        }

        /**
         * Adds a single user to the list from which the RecyclerView retrieves the users it
         * displays and notifies the RecyclerView that an item has been added.
         *
         * @param feed the user to add.
         */
        void addItem(Status feed) {
            feeds.add(feed);
            this.notifyItemInserted(feeds.size() - 1);
        }

        /**
         * Removes a user from the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that an item has been removed.
         *
         * @param status the user to remove.
         */
        void removeItem(Status status) {
            int position = feeds.indexOf(status);
            feeds.remove(position);
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
        public FeedFragment.FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FeedFragment.this.getContext());
            View view;

            if(viewType == LOADING_DATA_VIEW) {
                view =layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new FeedFragment.FeedHolder(view);
        }


        /**
         * Binds the followee at the specified position unless we are currently loading new data. If
         * we are loading new data, the display at that position will be the data loading footer.
         *
         * @param feedHolder the ViewHolder to which the followee should be bound.
         * @param position the position (in the list of followees) that contains the followee to be
         *                 bound.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull FeedHolder feedHolder, int position) {
            if(!isLoading) {
                feedHolder.bindUser(feeds.get(position));
            }
        }

        /**
         * Returns the current number of followees available for display.
         * @return the number of followees available for display.
         */
        @Override
        public int getItemCount() {
            return feeds.size();
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
            return (position == feeds.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more following
         * data.
         */
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();
            status = new Status(user, "11-05-24" ,"in here");
            GetFeedTask getFeedTask = new GetFeedTask(presenter, this);
            FeedRequest request = new FeedRequest(status, PAGE_SIZE, lastFeeds);
            getFeedTask.execute(request);
        }



        /**
         * A callback indicating more following data has been received. Loads the new followees
         * and removes the loading footer.
         *
         * @param feedResponse the asynchronous response to the request to load more items.
         */
        @Override
        public void feedsRetrieved(FeedResponse feedResponse) {

            if(feedResponse.getMessage() != null){
                // It doesn't matter what values we put here. We will be logged in with a hard-coded dummy user.
                LogoutRequest logoutRequest = new LogoutRequest(user.getAlias(), StaticHelperClass.getAuthToken().getToken());
               executeAsyncTask(logoutRequest);
            }
            else {
                List<Status> feeds = feedResponse.getUserFollowersFeed();

                lastFeeds = (feeds.size() > 0) ? feeds.get(feeds.size() - 1) : null;
                hasMorePages = feedResponse.getHasMorePages();

                isLoading = false;
                removeLoadingFooter();
                feedRecyclerViewAdapter.addItems(feeds);
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
            addItem(new Status(user,"11-05-24", "User"));
        }

        /**
         * Removes the dummy user from the list of users so the RecyclerView will stop displaying
         * the loading footer at the bottom of the list.
         */
        private void removeLoadingFooter() {
            removeItem(feeds.get(feeds.size() - 1));
        }
    }

    /**
     * A scroll listener that detects when the user has scrolled to the bottom of the currently
     * available data.
     */
    private class FeedRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        FeedRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
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

            if (!feedRecyclerViewAdapter.isLoading && feedRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    feedRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}