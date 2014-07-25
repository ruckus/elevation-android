package com.codycaughlan.yoelevation.async;

import android.util.Log;

import com.codycaughlan.yoelevation.YoElevationApplication;
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

    private static final String GOOGLE_ELEVATION_ENDPOINT = "https://maps.googleapis.com";

    interface GoogleElevationApiClient {
        @GET("/maps/api/elevation/json")
        void getElevationForLatLng(@QueryMap Map<String, String> options, Callback<ElevationResults> cb);
    }

    public void fetchElevation(final String requestId, double lat, double lng) {
        RestAdapter.LogLevel level = YoElevationApplication.DEBUG_HTTP ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(level)
                .setEndpoint(GOOGLE_ELEVATION_ENDPOINT)
                .build();

        GoogleElevationApiClient client = restAdapter.create(GoogleElevationApiClient.class);

        Callback<ElevationResults> callback = new Callback<ElevationResults>() {
            @Override
            public void success(ElevationResults elevateResult, Response response) {
                BusProvider.getInstance().post(produceElevateEvent(requestId, elevateResult));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Barney Error", error.getMessage());
            }
        };

        final Map<String, String> options = new HashMap<String,String>();
        final String latLng = String.format("%f,%f", lat, lng);
        options.put("locations", latLng);
        options.put("key", YoElevationApplication.GOOGLE_ELEVATION_API_KEY);
        client.getElevationForLatLng(options, callback);
    }

    @Produce
    public ElevateEvent produceElevateEvent(String requestId, ElevationResults result){
        return new ElevateEvent(requestId, result);
    }
}
