package com.example.albert.app;

import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements TokenFragment.OnFragmentInteractionListener{

    private String token;
    private TextView tv;

    private android.support.v7.widget.Toolbar mMainToolbar;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private ScheduleFragment scheduleFragment;
    private NewsFragment newsFragment;
    private TokenFragment tokenFragment;

    @Override
    public void onFragmentInteraction(String token) {
        this.token = token;
        tv.setText(token);
        setFragment(homeFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);

        mMainToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);
        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        homeFragment = new HomeFragment();
        scheduleFragment = new ScheduleFragment();
        newsFragment = new NewsFragment();
        tokenFragment = new TokenFragment();

        setSupportActionBar(mMainToolbar);
        getSupportActionBar().setTitle("Home");

        //if token is not yet received, display token fragment
        if (this.token == null) {
            setFragment(tokenFragment);
        }else{
            setFragment(homeFragment);
        }

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

    public void getSchedule(View v) {
        new JSONTask().execute();
    }

    public class JSONTask extends AsyncTask<String, Void, List<String>> {
        protected List<String> doInBackground(String... params){
            URL url = null;
            String s=null;
            List<String> subjectList = null;
            try {
                url = new URL("https://api.fhict.nl/schedule/me");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.connect();
                InputStream is = connection.getInputStream();
                Scanner scn = new Scanner(is);
                s = scn. useDelimiter("\\Z").next();
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                subjectList = new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++) {
                    // each array element is an object
                    JSONObject scheduleObject = jsonArray.getJSONObject(i);
                    // from each object, get field "subject", which is a string
                    String subName = scheduleObject.getString("subject");
                    // add each "subject" string to list
                    subjectList.add(subName);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return subjectList;
        }
        protected void onPostExecute(List<String> result){
            tv.setText(result.get(1));

            for(int i = 0; i < result.size(); i++) {
                System.out.println(result.get(i));
            }
        }

    }
}
