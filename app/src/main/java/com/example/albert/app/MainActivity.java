package com.example.albert.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.s);
    }

    public void clickSchedule(View v){
        Intent intent = new Intent(this, Schedule.class);
        startActivity(intent);
    }

    public void clickNews(View v){
        Intent intent = new Intent(this, News.class);
        startActivity(intent);
    }
}
