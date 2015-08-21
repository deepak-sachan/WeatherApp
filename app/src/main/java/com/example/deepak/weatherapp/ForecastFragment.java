package com.example.deepak.weatherapp;/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.deepak.weatherapp.R;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;

    private final   String  LOG_TAG= ForecastFragment.class.getSimpleName();

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
           // ProgressFragment progressFragment =  new ProgressFragment();
           // ProgressFragment.FetchWeatherTask weatherTask = progressFragment.new FetchWeatherTask();
           updateweather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
 /*
    public  void prefrencemethod(){
        double high= 0;
        double low = 0;

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unitType = sharedPrefs.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_metric));

        if (unitType.equals(getString(R.string.pref_units_imperial))) {
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        } else if (!unitType.equals(getString(R.string.pref_units_metric))) {
            Log.d(LOG_TAG, "Unit type not found: " + unitType);
        }

    }*/

    private static String getReadableDateString(long time) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d ");
        return format.format(date);
    }

    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        /*ForecastFragment forecastFragment = new ForecastFragment();
        forecastFragment.prefrencemethod();
*/
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unitType = sharedPrefs.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_metric));

        if (unitType.equals(getString(R.string.pref_units_imperial))) {
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        } else if (!unitType.equals(getString(R.string.pref_units_metric))) {
            Log.d(LOG_TAG, "Unit type not found: " + unitType);
        }

        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    private void updateweather(){

        FetchWeatherTaskRefesh weatherTaskRefesh = new FetchWeatherTaskRefesh();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String  location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        weatherTaskRefesh.execute(location);
        Toast.makeText(getActivity(),"Refresh",Toast.LENGTH_LONG).show();
        Log.v(LOG_TAG," Refersh Activity");

    }
   /* public  void onStart(){
        super.onStart();
        updateweather();
    }*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // String[] strings = new String[1];
       // strings[0] = savedInstanceState.getString("DHEERAJ");
       List<FormatString> weekForecast = new ArrayList<FormatString>(Arrays.asList(ProgressFragment.formatStrings));
        List<String> strings = new ArrayList<String>();
        for(FormatString formatString : weekForecast){
            strings.add(formatString.day+" "+formatString.description+" "+formatHighLows(formatString.high,formatString.low));
        }
        // static variable
        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_forecast, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        strings);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
              String forecast = mForecastAdapter.getItem(position);
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                Toast.makeText(getActivity(),forecast,Toast.LENGTH_LONG);
                intent.putExtra(intent.EXTRA_TEXT,forecast);
                startActivity(intent);



            }
        });

        return rootView;
    }

    public  class FetchWeatherTaskRefesh extends AsyncTask<String, Void, FormatString[]> {

        private final String LOG_TAG = FetchWeatherTaskRefesh.class.getSimpleName();

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
              */

            if (result != null) {
                mForecastAdapter.clear();
                for (FormatString dayForecastStr : result) {
                    mForecastAdapter.add(dayForecastStr.day+" deepak "+dayForecastStr.description+" "+formatHighLows(dayForecastStr.high,dayForecastStr.low));
                }
            }
            mForecastAdapter.add("deepak");
           /* Bundle bundle = new Bundle();
            bundle.putStringArray("DHEERAJ",result);
            Fragment fragment = new ForecastFragment();
            fragment.setArguments(bundle);
            getActivity().
                    getSupportFragmentManager().
                    beginTransaction()
                    .replace(R.id.container,fragment)
                    .commit();*/

        }
    }
}
