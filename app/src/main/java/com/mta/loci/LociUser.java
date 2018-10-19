package com.mta.loci;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LociUser implements Parcelable {

    private String mName;
    private String mUserId;
    private ArrayList<String> mUserPostsIds; //my posts
    private ArrayList<String> mTotalPostsIds; //all the posts of those I am following + mine
    private ArrayList<String> mUnlockedPostsIds; //all the viewable (unlocked) posts (mine + the ones I unlocked)
    private ArrayList<String> mFollowing; // list of Uid users that i folllow
    private ArrayList<String> mFollowers; // list of Uid users that folllow me

    //keep empty c'tor for firebase downloading the user
    public LociUser() { }

    //new user c'tor
    public LociUser(String userId, String name) {
        mUserId = userId;
        mName = name;
        mUserPostsIds  = new ArrayList<>();
        mTotalPostsIds = new ArrayList<>();
        mUnlockedPostsIds = new ArrayList<>();
        mFollowing = new ArrayList<>();
        mFollowers = new ArrayList<>();
    }

    public void updateUserFromDB(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databasePosts = database.getReference("Posts");
        DatabaseReference databaseUsers = database.getReference("Users");

        databasePosts.child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postIdSnapshot: dataSnapshot.getChildren()) {
                    mUserPostsIds.add(postIdSnapshot.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("Firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        databasePosts.child(mUserId).child("unlockedPosts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postIdSnapshot: dataSnapshot.getChildren()) {
                    mUnlockedPostsIds.add(postIdSnapshot.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("Firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        databaseUsers.child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userImFollowing: dataSnapshot.child("following").getChildren()) {
                    mFollowing.add(userImFollowing.toString());
                }

                for (DataSnapshot userFollowsMe: dataSnapshot.child("followers").getChildren()) {
                    mFollowers.add(userFollowsMe.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("Firebase", "loadFollowers:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String GetUserId() {
        return mUserId;
    }

    public void SetUserId(String userId) {
        mUserId = userId;
    }

    public ArrayList<String> GetUserPostsIds() {
        return mUserPostsIds;
    }

    public void SetUserPostsIds(ArrayList<String> userPostsIds) {
        mUserPostsIds = userPostsIds;
    }

    public ArrayList<String> GetTotalPostsIds() {
        return mTotalPostsIds;
    }

    public void SetTotalPostsIds(ArrayList<String> totalPostsIds) {
        mTotalPostsIds = totalPostsIds;
    }

    public ArrayList<String> GetUnlockedPostsIds() {
        return mUnlockedPostsIds;
    }

    public void SetUnlockedPostsIds(ArrayList<String> unlockedPostsIds) {
        mUnlockedPostsIds = unlockedPostsIds;
    }

    public void AddUserPostId(String userPostId) {
        mUserPostsIds.add(userPostId);
    }

    public void AddTotalPostsId(String lockedPostsId) {
        mTotalPostsIds.add(lockedPostsId);
    }

    public void AddUnlockedPostsId(String unlockedPostsId) {
        mUserPostsIds.add(unlockedPostsId);
    }

    public ArrayList<String> getFollowing() {
        return mFollowing;
    }

    public void addNewFollowing(String Uid) {
        mFollowing.add(Uid);
        //TODO: the following todo's need to be done by the profileActivity and not the LociUser
        // todo add the data to database
        // todo add me to this user followers
    }

    public void removeFollowing(String Uid) {
        mFollowing.remove(Uid);
        //TODO: the following todo's need to be done by the profileActivity and not the LociUser
        // todo remove the data to database
        // todo remove me from this user followers
    }

    public ArrayList<String> getmFollowers() {
        return mFollowers;
    }

    public ArrayList<String> getTotalPostsIds() {
        return mTotalPostsIds;
    }

    public ArrayList<String> getUnlockedPostsIds() {
        return mUnlockedPostsIds;
    }

    public ArrayList<String> getUserPostsIds() {
        return mUserPostsIds;
    }

    public String getUserId(){
        return mUserId;
    }

    //parcel garbage: ----------------------------------------------------------------------//

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mUserId);
        out.writeList(mUserPostsIds);
        out.writeList(mTotalPostsIds);
        out.writeList(mUnlockedPostsIds);
    }

    public static final Parcelable.Creator<LociUser> CREATOR = new Parcelable.Creator<LociUser>() {
        public LociUser createFromParcel(Parcel in) {
            return new LociUser(in);
        }

        public LociUser[] newArray(int size) {
            return new LociUser[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private LociUser(Parcel in) {
        mUnlockedPostsIds = in.readArrayList(null);
        mTotalPostsIds = in.readArrayList(null);
        mUserPostsIds = in.readArrayList( null);
        mUserId = in.readString();
    }

}
