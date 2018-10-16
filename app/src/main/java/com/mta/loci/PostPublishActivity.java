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

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class PostPublishActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String imageUriString = intent.getStringExtra("mediaUri");
        if (imageUriString == null)
            Log.e("IMAGE URI IS NULL", "NULL");
        mMediaUri = Uri.parse(imageUriString);
        LociUtil.InitUserFromIntent(getIntent(), mUser);
        mPostLocation = getIntent().getExtras().getParcelable("latLng");
        mMediaType = getIntent().getStringExtra("mediaType");
        InitUI();
        mLocationTextView.setText(GeoUtils.getLatLngAddress(mPostLocation, this.getBaseContext()));
        LoadAndDisplayTakenImage();
        initPublishButton();
    }

    private void initMapView(LatLng mPostLocation) {
        //TODO: get back to this after reinventing the camera!
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

        initMapView(mPostLocation);
        mTakenImageView = findViewById(R.id.imageView);
    }

    private void LoadAndDisplayTakenImage() {
        if (mMediaUri != null) {
            try {
                mTakenImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mMediaUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mTakenImageView.setImageBitmap(PostUtils.rotateImage(mTakenImageBitmap, mMediaUri.getPath()));
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
}
