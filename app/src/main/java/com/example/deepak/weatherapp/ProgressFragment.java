package com.example.deepak.weatherapp;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Activities that contain this fragment must implement the
 */
public class ProgressFragment extends android.support.v4.app.Fragment {

    public static FormatString[] formatStrings;

    public ProgressFragment() {
        super();
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FetchWeatherTask weatherTask = new FetchWeatherTask();
        weatherTask.execute("94042");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    public  class FetchWeatherTask extends AsyncTask<String, Void, FormatString[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        /* The date/time conversion code is going to be moved outside the asynctask later,
         * so for convenience we're breaking it out into its own method now.
         */



        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */


        @Override
        protected FormatString[] doInBackground(String... params) {

            return HTTPconnection.httpconnection(params);

        }
        @Override
        protected   void onPostExecute(FormatString[] result) {

            /*Fragment fragment = new ForecastFragment();
           getActivity().getSupportFragmentManager().beginTransaction()
                   .replace(R.id.container,fragment).commit();
*/          formatStrings = result;
            Fragment fragment = new ForecastFragment();
            getActivity().
                    getSupportFragmentManager().
                    beginTransaction()
                    .replace(R.id.container,fragment)
                    .commit();
        }

        }
    }


