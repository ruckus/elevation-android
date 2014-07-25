package com.codycaughlan.yoelevation.async;

import android.util.Log;

import com.codycaughlan.yoelevation.YoElevationApplication;
import com.codycaughlan.yoelevation.bus.BusProvider;
import com.codycaughlan.yoelevation.event.PlacesEvent;
import com.codycaughlan.yoelevation.model.PlacesResults;
import com.squareup.otto.Produce;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.QueryMap;

public class GooglePlacesClient {

    private static final String GOOGLE_PLACES_ENDPOINT = "https://maps.googleapis.com";

    interface GooglePlacesApiClient {
        @GET("/maps/api/place/textsearch/json")
        void search(@QueryMap Map<String, String> options, Callback<PlacesResults> cb);
    }

    public void search(String query) {
        RestAdapter.LogLevel level = YoElevationApplication.DEBUG_HTTP ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(level)
                .setEndpoint(GOOGLE_PLACES_ENDPOINT)
                .build();

        GooglePlacesApiClient client = restAdapter.create(GooglePlacesApiClient.class);

        Callback<PlacesResults> callback = new Callback<PlacesResults>() {
            @Override
            public void success(PlacesResults result, Response response) {
                BusProvider.getInstance().post(producePlacesEvent(result));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Barney Error", error.getMessage());
            }
        };

        final Map<String, String> options = new HashMap<String,String>();
        options.put("query", query);
        options.put("key", YoElevationApplication.GOOGLE_PLACES_API_KEY);
        client.search(options, callback);
    }

    @Produce
    public PlacesEvent producePlacesEvent(PlacesResults result){
        return new PlacesEvent(result);
    }
}
