package com.mta.loci;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnInfoWindowLongClickListener {

    private final String TAG = this.getClass().getName();
    private static final String LOCI_USER_CODE = "0";
    private static final String USER_POSTS_CODE = "1";
    private static final String TOTAL_POSTS_CODE = "2";
    private static final String UNLOCKED_POSTS_CODE = "3";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private MapView mapView;
    private GoogleMap gmap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final float DEFAULT_ZOOM = 15f;
    private static final int MIN_ZOOM = 10;
    private static String currentAddress;
    private LocationManager mLocationManager;
    private Location mCurrLocation;
    private String mProvider;

    // User needs to be initialized in OnCreate: //fix
    private LociUser mUser;

    private Button mButtonHome;
    private Button mButtonSearch;
    private Button mButtonPost;
    private Button mButtonFeed;
    private Button mButtonUserProfile;

    private void InitUI() {
        setContentView(R.layout.activity_home);
        InitButtoms();
    }

    private void InitButtoms() {
        mButtonHome = (Button) findViewById(R.id.buttonHome);
        mButtonSearch = (Button) findViewById(R.id.buttonSearch);
        mButtonPost = (Button) findViewById(R.id.buttonPost);
        mButtonFeed = (Button) findViewById(R.id.buttonFeed);
        mButtonUserProfile = (Button) findViewById(R.id.buttonUserProfile);

        // test //fix
        mUser = new LociUser();

        mButtonUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);

                // Parsing the Loci user field by field in order to load to intent:
                // fix - need to load user image url and more...
                intent.putExtra(LOCI_USER_CODE, mUser.GetUserId());
                intent.putExtra(USER_POSTS_CODE, mUser.GetUserPostsIds());
                intent.putExtra(TOTAL_POSTS_CODE, mUser.GetTotalPostsIds());
                intent.putExtra(UNLOCKED_POSTS_CODE, mUser.GetUnlockedPostsIds());
                startActivity(intent);
            }
        });
    }

    private void updateLocationAddress(Location location) {

        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            currentAddress = "";
            if (addresses.size() > 0) {
                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex();
                     i++)
                    currentAddress += addresses.get(0).getAddressLine(i) + " ";
            }

            Toast.makeText(getBaseContext(), currentAddress, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                            updateLocationAddress(currentLocation);
                        } else {
                            Log.e(TAG, "onComplete: current location is null");
                            Toast.makeText(HomeActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    // refactor
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: enter");
        String[] permissions = {FINE_LOCATION,
                COURSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        Log.d(TAG, "getLocationPermission: done");
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving camera to lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void addMarkers(List<Marker> markers) {

        //user should have all his posts and the posts of the people he follows uploaded when he logged in ! @Yarden @Zuf
        // so the following needs to be removed:
        //LociUser thisUser = new LociUser();
        //List<Post> userFriendsPosts = thisUser.;
        //Marker currMarker;
        ////until here
//
        //if (userFriendsPosts != null) {
        //    for (Post post : userFriendsPosts) {
        //        if (post.RequestKill()) {
        //            //TODO add method to remove this post from my list and from the database
        //            continue;
        //        }
//
        //        MarkerOptions mo = post.GenerateMarkerOptions();
        //        currMarker = gmap.addMarker(mo);
        //        currMarker.setTag(post); //this is what needs to be called when user clicks on the InfoWindow!!!
        //    }
        //}
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        this.gmap = googleMap;
        if (mLocationPermissionsGranted) {

            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, COURSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            gmap.setMyLocationEnabled(true);
            gmap.setMinZoomPreference(MIN_ZOOM);

            //add all the markers/posts here!
            addMarkers(null);
            //

            // Set a listener for info window events.
            gmap.setOnInfoWindowLongClickListener(this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "--> HomeActivity --> onCreate");
        InitUI();
        while (!mLocationPermissionsGranted)
            getLocationPermission();

        // Getting LocationManager object
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        mProvider = mLocationManager.getBestProvider(criteria, false);
        if (mProvider != null && !mProvider.equals("")) {

            @SuppressLint("MissingPermission") Location location = mLocationManager.getLastKnownLocation(mProvider);
            mLocationManager.requestLocationUpdates(mProvider, 200, 1, this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }

        while (!mLocationPermissionsGranted)
            getLocationPermission();
        if (mLocationPermissionsGranted) {
            Bundle mapViewBundle = null;
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
            }

            mapView = findViewById(R.id.mapView);
            mapView.onCreate(mapViewBundle);
            mapView.getMapAsync(this);
        } else {
            Log.e(TAG, "mLocationPermissionsGranted: " + mLocationPermissionsGranted);
        }

        Log.d(TAG, "<-- HomeActivity <-- onCreate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mapView.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //@Yarden @Zuf This is where we check if we can unlock a nearby Loci:
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: enter");
        mCurrLocation = new Location(location);
        //@Yarden @Zuf TODO: remove this after the user has the Posts List
        //LociUser thisUser = new LociUser();
        //List<Post> userFriendsPosts = thisUser.mFriendsPosts;
        ////until here
//
        //if (userFriendsPosts != null) {
        //    for (Post post : userFriendsPosts) {
        //        post.AttemptUnlock(mCurrLocation);
        //        //the above statement returns TRUE when a post has been changed from locked to unlocked!
        //        // so... TODO: add 'toast' or notification for user that he unlocked a new Loci!
        //    }
        //}
        Log.d(TAG, "onLocationChanged: done");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Post post = (Post) marker.getTag();
        // post ==> playMedia(); // TODO: replace this statement with a call to the Post Viewing activity, and send the post as an intent
    }
}
