package edu.byu.cs.tweeter.client.view.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Dialog;
import android.util.Log;
import android.view.*;
import android.os.Handler;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.StaticClass.StaticHelperClass;
import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.client.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.client.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.client.model.service.request.PostRequest;
import edu.byu.cs.tweeter.client.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.client.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.model.service.response.PostResponse;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;
import edu.byu.cs.tweeter.client.view.LoginActivity;
import edu.byu.cs.tweeter.client.view.asyncTasks.FollowTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetUserTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.PostTask;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

import java.io.IOException;
import java.time.LocalDateTime;  // Import the LocalDateTime class
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements MainPresenter.View,LogoutTask.Observer,
        FollowTask.Observer, PostTask.Observer, View.OnClickListener  {

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    public static final String IS_FOLLOW = "Token";
    private static final String LOG_TAG = "Logout";
    private  Button followingButton = null;
    private Dialog dialog = null;
    private TextView postUserName = null;
    private TextView postUserMention = null;
    private ImageView postUserImage = null;
    private EditText postContent = null;
    private User user = null;
    private Integer followerCountVal = 0;
    private Integer followeeCountVal = 0;
    private String url =null;
    private String mentioned =null;
    private Toast logoutToast;
    private MainPresenter presenter;
    private  static boolean unfollow = false;
    private Handler handler;
    private Runnable r;
    private TextView followeeCount;



    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }


        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.post_layout);

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);
        unfollow = (boolean) getIntent().getSerializableExtra(IS_FOLLOW);




        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

       presenter = new MainPresenter(this);

        Button logout = findViewById(R.id.logout);
        followingButton = findViewById(R.id.following);
        FloatingActionButton fab = findViewById(R.id.fab);

        if(!StaticHelperClass.isIsUser()){
             followingButton.setVisibility(View.VISIBLE);
            if(!user.getAlias().equals(StaticHelperClass.getMainUser().getAlias())){
                changedButtonText(unfollow);
            }
        }

//        handler = new Handler();
//        r = new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                // It doesn't matter what values we put here. We will be logged in with a hard-coded dummy user.
//                Toast.makeText(MainActivity.this, "user is inactive from last 5 minutes",Toast.LENGTH_SHORT).show();
//                LogoutRequest logoutRequest = new LogoutRequest(user.getAlias(), StaticHelperClass.getAuthToken().getToken());
//                executeAsyncTask(logoutRequest);
//
//
//            }
     //   };

//        startHandler();

        // We should use a Java 8 lambda function for the listener (and all other listeners), but
        // they would be unfamiliar to many students who use this code.
        fab.setOnClickListener(this);

        logout.setOnClickListener(this);

        followingButton.setOnClickListener(this);

        Button postButton = dialog.findViewById(R.id.postButton);

        Button close = dialog.findViewById(R.id.close);



        postUserName = dialog.findViewById(R.id.postUserName);
        postUserMention =  dialog.findViewById(R.id.postMention);
        postUserImage = dialog.findViewById(R.id.userImage);
        postContent = dialog.findViewById(R.id.postContent);

        postButton.setOnClickListener(this);


        close.setOnClickListener(this);

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));
        followeeCountVal = StaticHelperClass.getNumOfFollowees();
        followerCountVal = StaticHelperClass.getNumOfFollowers();
        followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText("Following: " + followeeCountVal);

        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText("Followers: " + followerCountVal);
    }


    public void executeAsyncTask(LogoutRequest logoutRequest){
        LogoutTask logoutTask = new LogoutTask(presenter, this);
        logoutTask.execute(logoutRequest);
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    String getPostDate(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");

        String formattedDate = myDateObj.format(myFormatObj);

        return formattedDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:

                postUserName.setText(user.getName());
                postUserMention.setText(user.getAlias());
                User all = StaticHelperClass.getCurrentUser();
                postUserImage.setImageDrawable(ImageUtils.drawableFromByteArray(all.getImageBytes()));
                postContent.setText("");
                dialog.show();
                break;
            case R.id.logout:
                logoutToast = Toast.makeText(this, "Logging out", Toast.LENGTH_LONG);
                logoutToast.show();
                // It doesn't matter what values we put here. We will be logged in with a hard-coded dummy user.
                LogoutRequest logoutRequest = new LogoutRequest(user.getAlias(), StaticHelperClass.getAuthToken().getToken());
                LogoutTask logoutTask = new LogoutTask(presenter, this);
                logoutTask.execute(logoutRequest);
                break;
            case R.id.following:
                unfollow = StaticHelperClass.isIs_follow();
                FollowRequest followRequest = new FollowRequest(StaticHelperClass.getMainUser(), user, unfollow);
                FollowTask followTask = new FollowTask(presenter, this);
                followTask.execute(followRequest);
                break;
            case  R.id.postButton:
                Status post = new Status(user, getPostDate(), postContent.getText().toString());
                PostRequest postRequest = new PostRequest(post);
                PostTask postTask = new PostTask(presenter, this);
                postTask.execute(postRequest);
                dialog.hide();
                break;
            case R.id.close:
                dialog.hide();
                break;
        }
    }

    public void changedButtonText(boolean unfollow){
        String buttonText = followingButton.getText().toString();
        if(!unfollow){
            followingButton.setText("Follow");
            StaticHelperClass.setIs_follow(unfollow);
            followingButton.setBackgroundColor(getApplication().getResources().getColor(R.color.colorAccent));
            followingButton.setTextColor(getApplication().getResources().getColor(R.color.white));
        }
        else {
            StaticHelperClass.setIs_follow(unfollow);
            followingButton.setText("Following");
            followingButton.setBackgroundColor(getApplication().getResources().getColor(R.color.white));
            followingButton.setTextColor(getApplication().getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void logoutSuccessful(LogoutResponse logoutResponse) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void logoutUnsuccessful(LogoutResponse logoutResponse) {
        Toast.makeText(this, "Failed to logout. " + logoutResponse.getMessage(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void followSuccessful(FollowResponse followResponse) {
        changedButtonText(!StaticHelperClass.isIs_follow());
        Toast.makeText(this, "Follow Successful ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void followUnsuccessful(FollowResponse followResponse) {
        Toast.makeText(this, "Failed to follow. " + followResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void postSuccessful(PostResponse postResponse) {
        Toast.makeText(this, "Post Successful ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void postUnsuccessful(PostResponse postResponse) {
        Toast.makeText(this, "Failed to post. " + postResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception ex) {
        Log.e(LOG_TAG, ex.getMessage(), ex);
        Toast.makeText(this, "Failed because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
//        stopHandler();//stop first and then start
//        startHandler();
    }

    /**
     * Loads the profile image data for the user.
     *
     */
    private User loadAllImages(User user) throws IOException {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
            return user;

    }

//    public void stopHandler() {
//        handler.removeCallbacks(r);
//    }
    public void startHandler() {
        handler.postDelayed(r, 1*60*1000); //for 5 minutes
    }
}