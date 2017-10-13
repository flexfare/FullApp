package com.flexfare.android.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.animation.LinearInterpolator;

import com.flexfare.android.R;
import com.flexfare.android.mapservice.MapConstants;
import com.flexfare.android.mapservice.MapInterpolator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SpotMe extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int REQ_LOC = 0;

    private GoogleMap gMap;
    private GoogleApiClient googleApiClient;

    private Location lastLocation;
    private LocationRequest locationRequest;

    private int markerCount;
    private boolean reqLocUpdates = false;

    SupportMapFragment mapFragment;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_me);

        markerCount = 0;
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.spot_me);

        if (getAvailableService()) {
            buildGoogleApiClient();
            taskLocationRequest();
            //Snackbar.make(coordinatorLayout, "Your location is missing!", Snackbar.LENGTH_LONG).show();
            //snackbar.show();
        }
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.spot_id);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }
    Marker point = null;
    public void addMarker(GoogleMap googleMap, double lat, double lng) {
        if (markerCount == 1) {
            animate(lastLocation, point);
        }
        else if (markerCount == 0) {
            //Solution found for bitmap pointer
            int height = 100;
            int width = 55;
            BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_human_greeting);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, width, height, false);
            gMap = googleMap;

            LatLng latLng = new LatLng(lat, lng);
            point = gMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng))
            .icon(BitmapDescriptorFactory.fromBitmap((bitmap1))));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.5f));

            markerCount = 1;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            launchLocUpdate();
        }
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Snackbar.make(coordinatorLayout, marker.getTitle(), Snackbar.LENGTH_LONG).show();
    }
    public boolean getAvailableService() {
        GoogleApiAvailability gaav = GoogleApiAvailability.getInstance();
        int isAvailable = gaav.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (gaav.isUserResolvableError(isAvailable)) {
            Dialog dialog = gaav.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            //Snackbar.make(coordinatorLayout, "Connection to Play Services not Established", Snackbar.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getAvailableService();
        if (googleApiClient.isConnected() && reqLocUpdates){
            launchLocUpdate();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_LOC);
        } else {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (lastLocation != null) {
                double lat = lastLocation.getLatitude();
                double lng = lastLocation.getLongitude();
                String location = "" + lat + " ," + lng + " ";
                Snackbar.make(coordinatorLayout, location, Snackbar.LENGTH_SHORT).show();

                addMarker(gMap, lat, lng);
            } else {
                Snackbar.make(coordinatorLayout, "Please turn on GPS to use this feature", Snackbar.LENGTH_INDEFINITE).show();
            }
        }
    }
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    protected void taskLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(MapConstants.UPDATE_INTERVAL);
        locationRequest.setFastestInterval(MapConstants.FATEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(MapConstants.DISPLACEMENT);
    }
    protected void launchLocUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_LOC);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest,  this);
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }
    @Override
    public void onConnected(Bundle arg0) {
        showLocation();

        if (reqLocUpdates) {
            launchLocUpdate();
        }
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        googleApiClient.connect();
    }
    public void onLocationChanged(Location location) {
        lastLocation = location;

        Snackbar.make(coordinatorLayout, "New Location", Snackbar.LENGTH_SHORT).show();

        showLocation();
    }
    public static void animate(final Location destination, final Marker marker) {
        if (marker != null) {
            final LatLng initialPos = marker.getPosition();
            final LatLng finalPos = new LatLng(destination.getLatitude(), destination.getLongitude());
            final  float initiateRotate = marker.getRotation();

            final MapInterpolator mapInterpolator = new MapInterpolator.MapCalc();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float anime = animation.getAnimatedFraction();
                        LatLng newPos = mapInterpolator.interpolate(anime, initialPos, finalPos);
                        marker.setPosition(newPos);
                        marker.setRotation(calcRotation(anime, initiateRotate, destination.getBearing()));
                    } catch (Exception e) {

                    }
                }
            });
            valueAnimator.start();
        }
    }
    private static float calcRotation(float anime, float start, float end) {
        float pickUp = end - start;
        float handle = (pickUp + 360) % 360;

        float point = (handle > 180) ? -1 : 1;
        float rotation;

        if (point > 0) {
            rotation = handle;
        } else {
            rotation = handle - 360;
        }
        float result = anime * rotation + start;
        return (result + 360) % 360;
    }

}

