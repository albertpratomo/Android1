package com.example.albert.app;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends android.support.v4.app.Fragment {

    private CalendarView cv;
    private ListView lv;
//    private TextView tv;
    private String token;

    CustomAdapter customAdapter = new CustomAdapter();

    //ArrayList to contain the schedule data
    ArrayList<Sch> scheduleData = new ArrayList<Sch>();

    //Class to model each schedule data
    class Sch{
        public String subject;
        public String room;
        public String teacher;
        public String tstart;
        public String tend;
    }

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

        cv = (CalendarView) v.findViewById(R.id.cv);
        lv = (ListView) v.findViewById(R.id.lv);

        MainActivity activity = (MainActivity) getActivity();
        this.token = activity.token;

        scheduleData.clear();
        new JSONTask().execute("2018-10-09");

        lv.setAdapter(customAdapter);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;

                //Empty scheduleData first
                scheduleData.clear();
                new JSONTask().execute(date);
            }
        });

        return v;
    }

    public class JSONTask extends AsyncTask<String, Void, List<String>> {
        protected List<String> doInBackground(String... params) {
            URL url = null;
            String s = null;
            List<String> subjectList = null;
            try {
                url = new URL("https://api.fhict.nl/schedule/me?days=1&start=" + params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.connect();
                InputStream is = connection.getInputStream();
                Scanner scn = new Scanner(is);
                s = scn.useDelimiter("\\Z").next();
                JSONObject jsonObject = new JSONObject(s);

//                Log.d("babi",s);

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                subjectList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    // each array element is an object
                    JSONObject scheduleObject = jsonArray.getJSONObject(i);

                    // Create a new Sch
                    Sch row = new Sch();
                    // Fill the Sch row data
                    row.room = scheduleObject.getString("room").toUpperCase();
                    row.subject = scheduleObject.getString("subject").toUpperCase();
                    row.teacher = scheduleObject.getString("teacherAbbreviation");
                    row.tstart = scheduleObject.getString("start").substring(11,16);
                    row.tend = scheduleObject.getString("end").substring(11,16);
                    //Append to scheduleData
                    scheduleData.add(row);
                }

                if(scheduleData.size()<=0){
                    // Create a new Sch
                    Sch row = new Sch();
                    // Fill the Sch row data
                    row.room = " ";
                    row.subject = "No schedule for this date";
                    row.teacher = " ";
                    row.tstart = " ";
                    row.tend = " ";
                    //Append to scheduleData
                    scheduleData.add(row);
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

        protected void onPostExecute(List<String> result) {
            customAdapter.notifyDataSetChanged();

//            for (int i = 0; i < scheduleData.size(); i++) {
//                Log.d("babi",scheduleData.get(i).room + " "+ scheduleData.get(i).room + " "+ scheduleData.get(i).subject + " "+ scheduleData.get(i).teacher);
//            }
        }
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return scheduleData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);
            }

            TextView litv1 = (TextView) convertView.findViewById(R.id.litv1);
            TextView litv2 = (TextView) convertView.findViewById(R.id.litv2);

            litv1.setText(scheduleData.get(position).subject + " (" +scheduleData.get(position).teacher + ")");
            litv2.setText(scheduleData.get(position).tstart + " - " +scheduleData.get(position).tend);

            return convertView;
        }
    }
}
