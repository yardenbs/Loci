package com.mta.loci;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class LociPostActivity extends AppCompatActivity {

    private Post mPost;
    private LociUser mCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loci_post);
        String creatorId = getIntent().getStringExtra("creatorId");
        String postId = getIntent().getStringExtra("postId");
        getPostAndUserFromDatabase(postId, creatorId);
    }

    private void initUI() {
        setContentView(R.layout.activity_loci_post);
        ImageView postImageView = (ImageView) findViewById(R.id.image);
        if(mPost != null) {
           loadImage(mPost.getMediaUrl(), postImageView);
           TextView creatorTextView = (TextView) findViewById(R.id.creator);
           creatorTextView.setText(mCreator.getName());
       }
    }

    private   void loadImage(String url, ImageView imageView) {
        Context context = imageView.getContext();
        ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(context, R.color.common_google_signin_btn_text_light_default));
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(cd)
                        .centerCrop())
                .transition(withCrossFade())
                .into(imageView);
    }

    private void getPostAndUserFromDatabase(String postId ,final String uid) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databasePosts = database.getReference("Posts");

        databasePosts.child(uid).child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPost = dataSnapshot.getValue(Post.class);
                getUserFromDatabase(uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LociUtil.class.getSimpleName(), "loadPost:onCancelled", databaseError.toException());
            }
        });

    }
    private void getUserFromDatabase(String uid) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseUsers = database.getReference("Users");

        databaseUsers.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               mCreator = dataSnapshot.getValue(LociUser.class);
                initUI();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LociUtil.class.getSimpleName(), "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
