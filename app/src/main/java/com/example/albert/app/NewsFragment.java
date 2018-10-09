package com.example.albert.app;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends android.support.v4.app.Fragment {

    private ListView lv;
    private String token;
 
    CustomAdapter customAdapter = new CustomAdapter();

    //ArrayList to contain the news data
    ArrayList<News> newsData = new ArrayList<News>();

    //Class to model each news data
    class News{
        public String title;
        public String description;
    }

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news, container, false);

       lv = (ListView) v.findViewById(R.id.lv2);

        MainActivity activity = (MainActivity) getActivity();
        this.token = activity.token;

        if(newsData.size()<=0) {
            new JSONTask().execute();
        }

        lv.setAdapter(customAdapter);

        return v;
    }

    public class JSONTask extends AsyncTask<String, Void, List<String>> {
        protected List<String> doInBackground(String... params) {
            URL url = null;
            String s = null;
            List<String> subjectList = null;
            try {
                url = new URL("https://api.fhict.nl/newsfeeds");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.connect();
                InputStream is = connection.getInputStream();
                Scanner scn = new Scanner(is);
                s = scn.useDelimiter("\\Z").next();
                JSONArray jsonArray = new JSONArray(s);
                subjectList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    // each array element is an object
                    JSONObject newsObject = jsonArray.getJSONObject(i);

                    // Create a new News
                    News row = new News();
                    // Fill the News row data
                    row.title = newsObject.getString("title");
                    row.description = newsObject.getString("description");
                    //Append to newsData
                    newsData.add(row);
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

//            for (int i = 0; i < newsData.size(); i++) {
//                Log.d("babi",newsData.get(i).room + " "+ newsData.get(i).room + " "+ newsData.get(i).subject + " "+ newsData.get(i).teacher);
//            }
        }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsData.size();
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
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);
            }

            TextView litv1 = (TextView) convertView.findViewById(R.id.litv1);
            TextView litv2 = (TextView) convertView.findViewById(R.id.litv2);

            litv1.setText(newsData.get(position).title);
            litv2.setText(newsData.get(position).description);

            return convertView;
        }
    }
}
