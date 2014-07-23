package com.codycaughlan.yoelevation.async;

import android.util.Log;

import com.codycaughlan.yoelevation.bus.BusProvider;
import com.codycaughlan.yoelevation.event.ElevateEvent;
import com.codycaughlan.yoelevation.model.ElevationResults;
import com.squareup.otto.Produce;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.QueryMap;

public class GoogleElevationClient {

    private static final String GOOGLE_ELEVATION_ENDPOINT =
            //"http://codycaughlan.com";
            "https://maps.googleapis.com";
    private static final String GOOGLE_ELEVATION_API_KEY = "AIzaSyCSW_JEqONZdwkAbYo9oFUpPcXPpCEN7HQ";

    interface GoogleElevationApiClient {
        //urlWithValues = "https://maps.googleapis.com/maps/api/elevation/json?locations=#{lat},#{lng}&key=#{GoogleConfig::ELEVATION_API_KEY}"
        @GET("/maps/api/elevation/json")
        //@GET("/elevation.json")
        void getElevationForLatLng(@QueryMap Map<String, String> options, Callback<ElevationResults> cb);
    }

    public void fetchElevation(double lat, double lng) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(GOOGLE_ELEVATION_ENDPOINT)
                .build();

        GoogleElevationApiClient client = restAdapter.create(GoogleElevationApiClient.class);

        Callback<ElevationResults> callback = new Callback<ElevationResults>() {
            @Override
            public void success(ElevationResults elevateResult, Response response) {
                //Log.i("Barney", "Producing!!!: " + elevateResult.toString());
                BusProvider.getInstance().post(produceElevateEvent(elevateResult));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Barney Error", error.getMessage());
            }
        };

        final Map<String, String> options = new HashMap<String,String>();
        final String latLng = String.format("%f,%f", lat, lng);
        options.put("locations", latLng);
        //options.put("key", GOOGLE_ELEVATION_API_KEY);
        client.getElevationForLatLng(options, callback);
    }

    @Produce
    public ElevateEvent produceElevateEvent(ElevationResults result){
        return new ElevateEvent(result);
    }
}
