package com.mta.loci;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    private String mCurrUserName;
    private ArrayList<String> mUsersNamesList;
    private ArrayList<String> mUsersUidsList;
    private Boolean mIsUserNamesListFull = false;
    private ListView mUsersNamesListView = null;
    private ArrayAdapter<String> mUsersNamesListAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follows_scrolling);
        mUsersNamesList = new ArrayList<String>();

        mProfileUserId = getIntent().getStringExtra("uid");
        mIsFollowing = getIntent().getBooleanExtra("isFollowing", true);
        mTitle = getIntent().getStringExtra("title");
        mUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get full user (lists as well!
                mProfileUser = dataSnapshot.child(mProfileUserId).getValue(LociUser.class);
                // Init both my followers and followed by me lists:
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
                user.getmFollowingUIDs().add(uidDataSnapshot.getValue().toString());
            }
            mUsersUidsList = user.getmFollowingUIDs();
        }
        else {
            for (DataSnapshot uidDataSnapshot : ds.child(uid).child("Followers").getChildren()) {
                user.getmFollowersUIDs().add(uidDataSnapshot.getValue().toString());
            }
            mUsersUidsList = user.getmFollowersUIDs();
        }

        // Create users names list by uids:
        DatabaseReference databaseUnlockedPosts = FirebaseDatabase.getInstance().getReference("Users");
        for (int index = 0; index < mUsersUidsList.size(); index++) {
            String currentUid = mUsersUidsList.get(index);
            Log.e("TAG", "getFollowersOrFollowing: " + databaseUnlockedPosts.child(currentUid).toString());
            databaseUnlockedPosts.child(currentUid).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            try {
                                String currentUserName = snapshot.getValue().toString();
                                Log.e("TAG", "" + currentUserName);
                                mUsersNamesList.add(currentUserName);
                                if (mUsersNamesList.size() == mUsersUidsList.size()) {
                                    mIsUserNamesListFull = true;
                                    // Refresh list adapter:
                                    mUsersNamesListAdapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("TAG", " it's null.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("onCancelled", " cancelled");
                }
            });
            //String currentUserName = databaseUnlockedPosts.child(currentUid).child("name").toString();
            //mUsersNamesList.add(currentUserName);
        }


    }

    private void InitUI() {
        mUsersNamesListView = (ListView) findViewById(R.id.followList);
        mUsersNamesListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, mUsersNamesList);
        mUsersNamesListView.setAdapter(mUsersNamesListAdapter);
        // ListView Item Click Listener
        mUsersNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                // Get the uid of the chosen user:
                String chosenUserUid = mUsersUidsList.get(position);
                // Start he's profile activity:
                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("uid", chosenUserUid);
                startActivity(intent);
            }

        });
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
    }

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        mUidList = mIsFollowing? mProfileUser.getmFollowingUIDs(): mProfileUser.getmFollowersUIDs();

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
