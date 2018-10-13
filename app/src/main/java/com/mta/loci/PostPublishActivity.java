package com.mta.loci;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostPublishActivity extends AppCompatActivity {

    private LatLng mPostLocation;
    private LociUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LociUtil.InitUserFromIntent(getIntent(), mUser);
        mPostLocation = getIntent().getExtras().getParcelable("4");
        setContentView(R.layout.activity_post_publish);
    }

    private void uploadPostToDatabase(String url, String mediaType ){
        DatabaseReference databasePosts = FirebaseDatabase.getInstance().getReference("posts");

        String id = databasePosts.push().getKey();
        //TODO: replace the following place holder !!!
        Post post = new Post(id, "replace this string with: mUser.GetUserId()", mPostLocation.latitude, mPostLocation.longitude, url, mediaType);

        databasePosts.child(id).setValue(post);

    }
}
