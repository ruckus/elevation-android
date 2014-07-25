package com.codycaughlan.yoelevation.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codycaughlan.yoelevation.R;
import com.codycaughlan.yoelevation.async.GoogleElevationClient;
import com.codycaughlan.yoelevation.bus.BusProvider;
import com.codycaughlan.yoelevation.event.ElevateEvent;
import com.codycaughlan.yoelevation.model.ElevationResult;
import com.codycaughlan.yoelevation.model.ElevationResults;
import com.codycaughlan.yoelevation.util.ConversionUtil;
import com.codycaughlan.yoelevation.util.UuidUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

public class YouOnMapFragment extends Fragment implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener
{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private com.google.android.gms.maps.MapFragment mMapView;
    private GoogleElevationClient mElevateClient;
    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private TextView mInfoLabel;
    private LocationRequest mLocationRequest;
    private boolean mUpdatesRequested;
    private View mRootView;
    private boolean mGooglePlayEnabled;
    private String mElevationRequestId;

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 60 * 2;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGooglePlayEnabled = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity().getApplicationContext())
                == ConnectionResult.SUCCESS;
        mRootView = inflater.inflate(R.layout.fragment_you_on_map, container, false);

        mElevateClient = new GoogleElevationClient();
        mLocationClient = new LocationClient(this.getActivity(), this, this);
        mInfoLabel = (TextView) mRootView.findViewById(R.id.info_label);

        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL);

        // Start with updates turned on
        mUpdatesRequested = true;

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        BusProvider.getInstance().register(this);
        mLocationClient.connect();
    }

    /*
    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    public void onStop() {
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }
        mLocationClient.disconnect();
        super.onStop();
    }
    */

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }
        mLocationClient.disconnect();
        BusProvider.getInstance().unregister(this);
    }

    private void updateUiWithLocation() {
        if(mGooglePlayEnabled) {
            mElevationRequestId = UuidUtil.generate();
            mElevateClient.fetchElevation(mElevationRequestId, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
    }

    @Subscribe
    public void onElevateEvent(ElevateEvent event) {
        if(mElevationRequestId == null) {
            return;
        }
        if(mElevationRequestId.equals(event.requestId)) {
            final ElevationResults result = event.result;
            if(result.results.size() == 1) {
                final ElevationResult entry = result.results.get(0);
                final LatLng latLng = new LatLng(entry.location.lat, entry.location.lng);
                if(mMap != null) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title("You"));
                    mInfoLabel.setText(String.format("%.2f ft.", ConversionUtil.metersToFeet(entry.elevation)));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                    mMap.animateCamera(cameraUpdate);
                }
            }
        }
    }

    private void setUpMapIfNeeded() {
        if(mGooglePlayEnabled) {
            // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
                mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mUpdatesRequested) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateUiWithLocation();
    }


}
