package com.mta.loci;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowsScrollingActivity extends AppCompatActivity {


    private ArrayList<String> mUidList;
    private DatabaseReference mUsersRef;
    private LociUser mProfileUser;
    private String mProfileUserId;
    private Boolean mIsFollowing;
    private String mTitle;
    private TextView mTitleView;
    private TextView mUserName;
    private TextView mUserEmail;
    private CircleImageView mProfileImage;
    private StaticGridView mStaticGridView;
    private GridViewAdapter mStaticGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follows_scrolling);

        mProfileUserId = getIntent().getStringExtra("uid");
        getIntent().getBooleanExtra("isFollowing", mIsFollowing);
        mTitle = getIntent().getStringExtra("title");

        mUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get full user (lists as well!
                mProfileUser = dataSnapshot.child(mProfileUserId).getValue(LociUser.class);
                getFollowersOrFollowing(mProfileUser, mProfileUserId, dataSnapshot, mIsFollowing);
                InitUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getFollowersOrFollowing(LociUser user, String uid, DataSnapshot ds, Boolean isFollowing) {

        if(isFollowing) {
            for (DataSnapshot uidDataSnapshot : ds.child(uid).child("Following").getChildren()) {
                user.getmFollowing().add(uidDataSnapshot.getValue().toString());
            }
        }
        else {
            for (DataSnapshot uidDataSnapshot : ds.child(uid).child("Followers").getChildren()) {
                user.getmFollowers().add(uidDataSnapshot.getValue().toString());
            }
        }

    }

    private void InitUI() {
        mTitleView = findViewById(R.id.TitleTextBox);
        mTitleView.setText(mTitle);

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

    private void InitGridView() {
        mStaticGridView = (StaticGridView) findViewById(R.id.staticGridView);
        mStaticGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        mStaticGridView.setAdapter(mStaticGridAdapter);
        mStaticGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                // Create intent to load Post Activity and add the clicked image info:
                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("uid", item.getTitle());
                intent.putExtra("image", item.getImage());
                //Start details activity
                startActivity(intent);
            }
        });
    }

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray images = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < images.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }




}
