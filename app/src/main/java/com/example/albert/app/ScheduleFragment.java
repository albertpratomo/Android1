package com.example.albert.app;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends android.support.v4.app.Fragment {

    private CalendarView cv;
    private TextView tv;


    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

        cv = (CalendarView) v.findViewById(R.id.cv);
        tv = (TextView) v.findViewById(R.id.tv);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                tv.setText(date);
            }
        });

        return v;
    }

}
