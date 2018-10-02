package com.example.albert.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mMainToolbar;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private ScheduleFragment scheduleFragment;
    private NewsFragment newsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);
        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        homeFragment = new HomeFragment();
        scheduleFragment = new ScheduleFragment();
        newsFragment = new NewsFragment();

        setSupportActionBar(mMainToolbar);
        getSupportActionBar().setTitle("Home");
        setFragment(homeFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home :
                        getSupportActionBar().setTitle("Home");
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_schedule :
                        getSupportActionBar().setTitle("Schedule");
                        setFragment(scheduleFragment);
                        return true;
                    case R.id.nav_news :
                        getSupportActionBar().setTitle("News");
                        setFragment(newsFragment);
                        return true;

                        default:
                            return false;
                }
            }
        });
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
