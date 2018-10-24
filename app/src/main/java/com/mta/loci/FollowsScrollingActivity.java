package com.mta.loci;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    private String mCurrUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follows_scrolling);

        mProfileUserId = getIntent().getStringExtra("uid");
        mIsFollowing = getIntent().getBooleanExtra("isFollowing", true);
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

                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("uid", item.getTitle());
                startActivity(intent);
            }
        });
    }

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        mUidList = mIsFollowing? mProfileUser.getmFollowing(): mProfileUser.getmFollowers();

        TypedArray images = getResources().obtainTypedArray(R.array.image_ids);
        for (String uid: mUidList) {
            imageItems.add(new ImageItem(null, getNamefromDB(uid)));
        }
        return imageItems;
    }

    private String getNamefromDB(String uid) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCurrUserName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return mCurrUserName;
    }


}
