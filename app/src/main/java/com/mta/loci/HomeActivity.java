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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnInfoWindowLongClickListener {

    private final String TAG = this.getClass().getName();

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private Boolean mWriteExternalPermissionsGranted = false;

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
    private  LociUser mUser = new LociUser();

    private Button mButtonHome;
    private Button mButtonSearch;
    private Button mButtonPost;
    private Button mButtonFeed;
    private Button mButtonUserProfile;

    private ArrayList<Post> mTotalPostsIds; //all the posts of those I am following

    private void InitUI() {
        setContentView(R.layout.activity_home);
        InitButtons();
    }

    private void InitButtons() {
        mButtonHome = (Button) findViewById(R.id.buttonHome);
        mButtonSearch = (Button) findViewById(R.id.buttonSearch);
        mButtonPost = (Button) findViewById(R.id.buttonPost);
        mButtonFeed = (Button) findViewById(R.id.buttonFeed);
        mButtonUserProfile = (Button) findViewById(R.id.buttonUserProfile);

        mButtonUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("uid", LociUtil.getCurrentUserId());
                startActivity(intent);
            }
        });

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PostMakerActivity.class);
                intent.putExtra("uid", LociUtil.getCurrentUserId());
                if (mCurrLocation != null)
                    intent.putExtra("latLng", new LatLng(mCurrLocation.getLatitude(),mCurrLocation.getLongitude()));
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
    private void getPermissions() {
        Log.d(TAG, "getPermissions: enter");
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
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mWriteExternalPermissionsGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                CAMERA_SERVICE) == PackageManager.PERMISSION_GRANTED) {
            mWriteExternalPermissionsGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        Log.d(TAG, "getPermissions: done");

    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving camera to lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void addMarkers() {
        //assume from onCreate that user has all data from firebase


        for (Post post : mTotalPostsIds) {

            if (post.RequestKill()) {
                continue;
            }

            MarkerOptions mo = PostUtils.GenerateMarkerOptions(post);
            Marker currMarker = gmap.addMarker(mo);
            currMarker.setTag(post);
        }

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
            addMarkers();
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

        updateUserFromDB();
        //MOCK
        mUser.getmFollowing().add("OvHNTiNI3ygsGX6Sk6PgnppnJHF2");
        loadFollowingPostsFromDB();
        //MOCK
        //TODO: refresh data service to run on separate thread!
        //fetchFriendsLists();
        //fetchPosts(); //including my own


        while (!mLocationPermissionsGranted)
            getPermissions();

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
            getPermissions();
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

    private void loadFollowingPostsFromDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("Posts");

        mTotalPostsIds = new ArrayList<>();
        ArrayList<String> uidList =  new ArrayList<>(mUser.getmFollowing());
        uidList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

        for (String uid : uidList) {
            postsRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot post: dataSnapshot.getChildren()) {
                        mTotalPostsIds.add(post.getValue(Post.class));
                    }
                    addMarkers();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.e("Firebase", "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            });
        }

    }

    private void updateUserFromDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference UsersRef = database.getReference("Users");

        String uid  = FirebaseAuth.getInstance().getCurrentUser().getUid();

        UsersRef.child(uid).child("UnlockedPosts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postIdSnapshot: dataSnapshot.getChildren()) {
                    mUser.getUnlockedPostIds().add(postIdSnapshot.getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("Firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


        UsersRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userImFollowing: dataSnapshot.child("Following").getChildren()) {
                    mUser.getmFollowing().add(userImFollowing.toString());
                }

                for (DataSnapshot userFollowsMe: dataSnapshot.child("Followers").getChildren()) {
                    mUser.getmFollowers().add(userFollowsMe.toString());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("Firebase", "loadFollowers:onCancelled", databaseError.toException());
                // ...
            }
        });

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
        mapView.onResume();
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

        if (mTotalPostsIds != null) {
            for (Post post : mTotalPostsIds) {
                if( !mUser.getmUnlockedPostIds().contains(post.getId()) && PostUtils.AttemptUnlock(mCurrLocation, post)){
                    mUser.getmUnlockedPostIds().add(post.getId());
                    //TODO: add post creator name to post object
                    Toast.makeText(getBaseContext(), "unlocked new " + post.getmMediaType() + "!", Toast.LENGTH_SHORT).show();
                }
            }
        }
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

        if( mUser.getmUnlockedPostIds().contains(post.getId())){
            Intent intent = new Intent(this, LociPostActivity.class);
            intent.putExtra("creatorId", post.getmCreatorId());
            intent.putExtra("postId", post.getId());
            startActivity(intent);
        }
        else {
            Toast.makeText(getBaseContext(), "This Loci is still locked!", Toast.LENGTH_SHORT).show();
        }
    }
}
