package com.codycaughlan.yoelevation.async;

import android.util.Log;

import com.codycaughlan.yoelevation.bus.BusProvider;
import com.codycaughlan.yoelevation.event.ElevateEvent;
import com.codycaughlan.yoelevation.event.SandboxEvent;
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

public class TestClient {

    private static final String ENDPOINT = "http://10.0.1.36:3000";

    interface SandboxApiClient {
        @GET("/sandbox/delay")
        void execute(Callback<String> cb);
    }

    public void execute() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ENDPOINT)
                .build();

        SandboxApiClient client = restAdapter.create(SandboxApiClient.class);

        Callback<String> callback = new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                //Log.i("Barney", "Producing!!!: " + elevateResult.toString());
                BusProvider.getInstance().post(produceSandboxEvent(result));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Barney Error", error.getMessage());
            }
        };

        client.execute(callback);
    }

    @Produce
    public SandboxEvent produceSandboxEvent(String result){
        return new SandboxEvent(result);
    }

}
