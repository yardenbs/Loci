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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LociUtil.InitUserFromIntent(getIntent(), mUser);
        mPostLocation = getIntent().getExtras().getParcelable("4");
        mMediaType = getIntent().getStringExtra("mediaType");
        mUrl = getIntent().getStringExtra("url");
        setContentView(R.layout.activity_post_publish);
        initPublishButton();
    }

    private void initPublishButton(){
        Log.d(TAG, "initPublishButton: enter");
        mPublishButton = findViewById(R.id.PublishfloatingActionButton);

        mPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
