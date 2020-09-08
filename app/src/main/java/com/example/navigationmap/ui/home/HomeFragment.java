package com.example.navigationmap.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import com.example.navigationmap.room.AppExecutors;
import com.example.navigationmap.room.LocationData;
import com.example.navigationmap.room.RoomDatabase;
import com.google.android.gms.location.LocationListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.navigationmap.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final int DATABASE_MARKER_ID = 1;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private LatLng currPos;
    private LatLng markerPos;
    private Marker marker;

    private FloatingActionButton fabSetMarker;
    private FloatingActionButton fabMore;
    private FloatingActionButton fabMeasure;
    private FloatingActionButton fabCameraMove;

    private HomeViewModel homeViewModel;
    private RoomDatabase database;
    private LocationData locationData;

    private LocationRequest mLocationRequest;

    private boolean isCameraFollowing;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        isCameraFollowing = true;

        database = RoomDatabase.getInstance(getContext());

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);


        fabSetMarker = view.findViewById(R.id.fabSetMarker);
        fabMore = view.findViewById(R.id.fabMore);
        fabMeasure = view.findViewById(R.id.fabMeasure);
        fabCameraMove = view.findViewById(R.id.fabCameraMove);

        setupFAB();
        getStoredMarker();

        return view;
    }

    private void setupFAB() {
        fabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabSetMarker.getVisibility() == View.GONE) {
                    fabSetMarker.setVisibility(View.VISIBLE);
                    fabMeasure.setVisibility(View.VISIBLE);
                    fabCameraMove.setVisibility(View.VISIBLE);
                } else {
                    fabSetMarker.setVisibility(View.GONE);
                    fabMeasure.setVisibility(View.GONE);
                    fabCameraMove.setVisibility(View.GONE);
                }
            }
        });

        fabSetMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (marker != null) {
                    marker.remove();
                    marker = null;
                    markerPos = null;
                    fabSetMarker.setImageResource(R.drawable.ic_location_on_24px);
                    removeStoredMarkerLocation();
                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(currPos));
                    markerPos = currPos;
                    fabSetMarker.setImageResource(R.drawable.ic_wrong_location_24px);
                    setLocationInDatabase(marker, markerPos);
                }
            }
        });

        fabMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (markerPos == null) {
                    return;
                }

                Location markerLocation = new Location("");
                markerLocation.setLatitude(markerPos.latitude);
                markerLocation.setLongitude(markerPos.longitude);

                Location myLocation = new Location("");
                myLocation.setLatitude(currPos.latitude);
                myLocation.setLongitude(currPos.longitude);

                float distanceInMeters = markerLocation.distanceTo(myLocation);
                Toast.makeText(getActivity(), distanceInMeters + " Meter!", Toast.LENGTH_LONG).show();
            }
        });

        fabCameraMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCameraFollowing) {
                    isCameraFollowing = false;
                    fabCameraMove.setImageResource(R.drawable.ic_near_me_24px);
                } else {
                    isCameraFollowing = true;
                    fabCameraMove.setImageResource(R.drawable.ic_near_me_disabled_24px);
                }
            }
        });
    }

    private void removeStoredMarkerLocation() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.locationDataDao().deleteMarkerLocation(locationData);
                locationData = null;
            }
        });

    }

    private void setLocationInDatabase(Marker marker, LatLng markerPos) {
        if (locationData == null) {
            locationData = new LocationData(DATABASE_MARKER_ID, Double.toString(markerPos.latitude), Double.toString(markerPos.longitude));
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                LocationData storedMarkerLocation = database.locationDataDao().getLocationData();
                if (storedMarkerLocation == null) {
                    database.locationDataDao().addNewMarkerLocation(locationData);
                } else {
                    database.locationDataDao().updateLocationData(locationData);
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        buildGoogleApiClient();

        if(markerPos != null) {
            marker = mMap.addMarker(new MarkerOptions().position(markerPos));
        }
    }


    private void getStoredMarker() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                LocationData storedMarker = database.locationDataDao().getLocationData();

                if (storedMarker != null) {
                    fabSetMarker.setImageResource(R.drawable.ic_wrong_location_24px);
                    markerPos = new LatLng(Double.parseDouble(storedMarker.getLat()), Double.parseDouble(storedMarker.getLng()));
                    locationData = new LocationData(DATABASE_MARKER_ID, storedMarker.getLat(), storedMarker.getLng());
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        currPos = new LatLng(location.getLatitude(), location.getLongitude());

        //move map camera
        if (isCameraFollowing) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPos, 19));
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

