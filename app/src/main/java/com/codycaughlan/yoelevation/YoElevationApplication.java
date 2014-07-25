package com.codycaughlan.yoelevation;

import android.app.Application;

public class YoElevationApplication extends Application {

    public static final boolean DEBUG_HTTP = false;

    public static final String GOOGLE_PLACES_API_KEY = "AIzaSyCqBwAq8hPb29xrd4nKe35F2YLfq46pknM";
    public static final String GOOGLE_ELEVATION_API_KEY = "AIzaSyCqBwAq8hPb29xrd4nKe35F2YLfq46pknM";


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
