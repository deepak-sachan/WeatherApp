package com.example.deepak.weatherapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by deepak on 4/3/15.
 */
public class FormatString {
    String day;
    String description;
    double high;
    double low;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public FormatString(String day, String description, double high, double low) {
        this.day = day;
        this.description = description;
        this.high = high;
        this.low = low;
    }
}
