package com.mta.loci;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    private LociUser mProfileUser; //the user of the profile that we are looking at
    private LociUser mCurrentUser;
    private StaticGridView mStaticGridView;
    private GridViewAdapter mStaticGridAdapter;
    private Button mFollowButton;
    private TextView mUserName;
    private TextView mUserEmail;
    private CircleImageView mProfileImage;
    private DatabaseReference mUsersRef;
    private DatabaseReference mPostsref;
    ArrayList<Post> mPostItems;

    private Boolean mIsFollow = false;
    private Boolean mIsThisUsersProfile = false;
    private String mCurrentUserId;
    private String mProfileUserId;
    private Button mFollowers;
    private Button mFollowing;
    private Intent mFollowIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mCurrentUserId = FirebaseAuth.getInstance().getUid();
        mProfileUserId = getIntent().getStringExtra("uid");
        mUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mPostsref = FirebaseDatabase.getInstance().getReference().child("Posts");

        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get full user (lists as well!
                mProfileUser = dataSnapshot.child(mProfileUserId).getValue(LociUser.class);
                mCurrentUser = dataSnapshot.child(mCurrentUserId).getValue(LociUser.class);
                getFollowersAndFollowing(mProfileUser, mProfileUserId, dataSnapshot);
                getFollowersAndFollowing(mCurrentUser, mCurrentUserId, dataSnapshot);

                for (DataSnapshot postIdSnapshot: dataSnapshot.child(mCurrentUserId).child("UnlockedPosts").getChildren()) {
                    mCurrentUser.getUnlockedPostIds().add(postIdSnapshot.getValue().toString());
                }
                InitUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void getFollowersAndFollowing(LociUser user, String uid, DataSnapshot ds) {

        for( DataSnapshot uidDataSnapshot: ds.child(uid).child("Following").getChildren()){
            user.getmFollowingUIDs().add(uidDataSnapshot.getValue().toString());
        }

        for( DataSnapshot uidDataSnapshot: ds.child(uid).child("Followers").getChildren()){
            user.getmFollowersUIDs().add(uidDataSnapshot.getValue().toString());
        }

    }

    private void InitUI() {
        setContentView(R.layout.activity_user_profile);
        initButtons();

        if(!mProfileUserId.equals(mCurrentUserId)) { // is this not my profile?

            mFollowButton.setText("Follow");

            if(mCurrentUser.getmFollowingUIDs().contains(mProfileUser.getUserId())){
                mFollowButton.setText("Unfollow");
                mIsFollow = true;
            }
        }
        else{
            mFollowButton.setText("Logout");
            mIsThisUsersProfile = true;
        }
        mUserName = findViewById(R.id.textViewUserName);
        mUserName.setText(mProfileUser.getName());

        mUserEmail = findViewById(R.id.textView_email);
        mUserEmail.setText(mProfileUser.getmEmail());

        mProfileImage = findViewById(R.id.circleImageProfile);
       if(!mProfileUser.getmPhotoUrl().equals("")){
            LociUtil.loadImage(mProfileUser.getmPhotoUrl(), mProfileImage);
        }

        InitGridView();
    }

    private void initButtons(){
        mFollowButton = findViewById(R.id.FollowButton);
        mFollowers = findViewById(R.id.FollowersButton);
        mFollowing = findViewById(R.id.FollowingButton);

        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mIsThisUsersProfile) {

                    if (mIsFollow == false) {
                        mCurrentUser.addNewFollowing(mProfileUser.getUserId());
                        mFollowButton.setText("Unfollow");
                    } else {
                        mCurrentUser.removeFollowing(mProfileUser.getUserId());
                        mFollowButton.setText("Follow");
                    }

                    updateDatabaseFollowingFollowers(mIsFollow);
                    mIsFollow = !mIsFollow;
                }
                else{
                    // This is the user's profile:
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent = new Intent( getBaseContext(), SplashActivity.class);
                    startActivity(intent);
                }
            }
        });

        mFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFollowListActivity("Following", true);
            }
        });


        mFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFollowListActivity("Followers", false);
            }
        });

    }

    private void startFollowListActivity(String title, Boolean isRequestForFollowingList) {

        mFollowIntent = new Intent(getBaseContext(), FollowsScrollingActivity.class);
        mFollowIntent.putExtra("title", title);
        mFollowIntent.putExtra("isFollowing", isRequestForFollowingList);
        mFollowIntent.putExtra("uid", mProfileUserId);

        startActivity(mFollowIntent);
    }

    private void updateDatabaseFollowingFollowers(Boolean isFollow) {

        DatabaseReference databaseRefCurrentUser = FirebaseDatabase.getInstance().getReference("Users").child(mCurrentUserId);
        DatabaseReference databaseRefProfileUser = FirebaseDatabase.getInstance().getReference("Users").child(mProfileUserId);

        if(!isFollow){
            databaseRefCurrentUser.child("Following").child(mProfileUserId).setValue(mProfileUserId);
            databaseRefProfileUser.child("Followers").child(mCurrentUserId).setValue(mCurrentUserId);
        }
        else{
            databaseRefCurrentUser.child("Following").child(mProfileUserId).removeValue();
            databaseRefProfileUser.child("Followers").child(mCurrentUserId).removeValue();
        }

    }


    private void InitGridView() {

        mStaticGridView = (StaticGridView) findViewById(R.id.staticGridView);
        mPostItems = new ArrayList<>();

        DatabaseReference databasePosts = FirebaseDatabase.getInstance().getReference("Posts").child(mProfileUserId);
        mStaticGridAdapter = new GridViewAdapter( UserProfileActivity.this, R.layout.grid_item_layout, mPostItems);
        mStaticGridView.setAdapter(mStaticGridAdapter);
        databasePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if(!mIsThisUsersProfile && !mCurrentUser.getmUnlockedPostIds().contains(post.getId()))
                    {
                        String uri = "android.resource://"+ getPackageName() +"/drawable/lock";
                        post.setmMediaUrl(uri);
                    }
                    mPostItems.add(post);
                }

        mStaticGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post item = (Post) parent.getItemAtPosition(position);

                        if(!mIsThisUsersProfile && !mCurrentUser.getmUnlockedPostIds().contains(item.getId()))
                        {
                            // Create intent to load Post Activity and add the clicked image info:
                            Intent intent = new Intent(view.getContext(), HomeActivity.class);
                            intent.putExtra("zoom_in", true);
                            intent.putExtra("latLng", item.getmLatlng());
                            //Start details activity
                            startActivity(intent);
                        }

                        // Create intent to load Post Activity and add the clicked image info:
                        Intent intent = new Intent(view.getContext(), LociPostActivity.class);
                        intent.putExtra("creatorId", item.getmCreatorId());
                        intent.putExtra("postId", item.getId());
                        //Start details activity
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
