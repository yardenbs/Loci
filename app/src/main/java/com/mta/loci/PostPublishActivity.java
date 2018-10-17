package com.mta.loci;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class PostPublishActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final String TAG = this.getClass().getName();
    private LatLng mPostLocation;
    private LociUser mUser;
    private FloatingActionButton mPublishButton;
    private String mMediaType;
    private String mUrl;
    private Uri mMediaUri;
    private ImageView mTakenImageView;
    private Bitmap mTakenImageBitmap = null;
    private TextView mLocationTextView;
    private MapView mapView;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Bitmap mPostImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String imageUriString = intent.getStringExtra("mediaUri");
        if (imageUriString == null)
            Log.e("IMAGE URI IS NULL", "NULL");
        mMediaUri = Uri.parse(imageUriString);
        mPostLocation = getIntent().getExtras().getParcelable("latLng");
        mMediaType = getIntent().getStringExtra("mediaType");

        InitUI();
        initPublishButton();


    }

    private void initMapView() {
        Bundle mapViewBundle = null;
        mapView = findViewById(R.id.PublishmapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    private void initPublishButton(){
        Log.d(TAG, "initPublishButton: enter");
        mPublishButton = findViewById(R.id.PublishfloatingActionButton);

        mPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: create this method: uploadMediaToDatabase
                //mUrl = PostUtils.uploadMediaToDatabase(Uri uri);
                if( mUrl != null) {
                    uploadPostToDatabase(mUrl, mMediaType);
                }
            }
        });

        Log.d(TAG, "initPublishButton: end");
    }

    private void InitUI() {
        setContentView(R.layout.activity_post_publish);
        mLocationTextView = findViewById(R.id.LocationText);
        mTakenImageView = findViewById(R.id.imageView);
        initMapView();
        mLocationTextView.setText(GeoUtils.getLatLngAddress(mPostLocation, this.getBaseContext()));
        LoadAndDisplayTakenImage();
    }

    private void LoadAndDisplayTakenImage() {
        if (mMediaUri != null) {
            try {
                mTakenImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mMediaUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPostImage = PostUtils.rotateImage(mTakenImageBitmap, mMediaUri.getPath());
            mTakenImageView.setImageBitmap(mPostImage);
        }
    }

    private void uploadPostToDatabase(String url, String mediaType ){
        Log.d(TAG, "uploadPostToDatabase: enter");

        DatabaseReference databasePosts = FirebaseDatabase.getInstance().getReference("Posts");

        String id = databasePosts.push().getKey();
        Post post = new Post(id, mUser.GetUserId(), mPostLocation.latitude, mPostLocation.longitude, url, mediaType);

        databasePosts.child(id).setValue(post);

        Log.d(TAG, "uploadPostToDatabase: end");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(16);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(mPostLocation));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String name  = LociUtil.getUserFromDatabase(uid).getName();
        gmap.addMarker(new MarkerOptions().position(mPostLocation).title(name));
        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        gmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
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
}
