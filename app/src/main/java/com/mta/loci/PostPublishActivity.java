package com.mta.loci;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostPublishActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private LatLng mPostLocation;
    private LociUser mUser;
    private Button mPublishButton;
    private String mMediaType;
    private String mUrl;
    private String mMediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LociUtil.InitUserFromIntent(getIntent(), mUser);
        mPostLocation = getIntent().getExtras().getParcelable("4");

        //TODO: change this so the image is passed to here (via uri?) and only AFTER publish button click - Load image to DB and retrieve url !!!
        //mMediaUri = getIntent().getStringExtra("mediaUri");

        //TODO: define this in the intent sent to this activity !!!
        mMediaType = getIntent().getStringExtra("mediaType");

        setContentView(R.layout.activity_post_publish);

        //TODO: method to present media according to type
        //initMediaView();

        initMapView(mPostLocation);
        initPublishButton();
    }

    private void initMapView(LatLng mPostLocation) {

    }

    private void initPublishButton(){
        Log.d(TAG, "initPublishButton: enter");
        mPublishButton = findViewById(R.id.PublishfloatingActionButton);

        mPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: create this method:
                //mUrl = uploadMediaToDatabase();

                uploadPostToDatabase(mUrl, mMediaType);
            }
        });

        Log.d(TAG, "initPublishButton: end");
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
