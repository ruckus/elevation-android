package com.codycaughlan.yoelevation;

import android.app.Application;

public class YoElevationApplication extends Application {

    public static final boolean DEBUG_HTTP = false;

    public static final String GOOGLE_PLACES_API_KEY = "";
    public static final String GOOGLE_ELEVATION_API_KEY = "";


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
