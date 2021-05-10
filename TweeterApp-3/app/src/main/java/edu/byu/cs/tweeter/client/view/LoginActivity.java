package edu.byu.cs.tweeter.client.view;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import edu.byu.cs.tweeter.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginPagerAdapter loginPagerAdapter = new LoginPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.login_view_pager);
        viewPager.setAdapter(loginPagerAdapter);
        TabLayout tabs = findViewById(R.id.login_tabs);
        tabs.setupWithViewPager(viewPager);
    }
}