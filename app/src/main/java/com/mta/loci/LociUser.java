package com.mta.loci;

import java.util.ArrayList;

public class LociUser {

    private String mName;
    private String mUserId;
    private String mEmail;
    private String mToken;
    private ArrayList<String> mFollowing = new ArrayList<>(); // list of Uid users that i follow
    private ArrayList<String> mFollowers = new ArrayList<>(); // list of Uid users that follow me
    private ArrayList<String> mUnlockedPostIds = new ArrayList<>(); // list of Uids of posts that are viewable


    public String getmUserId() {
        return mUserId;
    }

    public ArrayList<String> getmFollowing() {
        return mFollowing;
    }

    public ArrayList<String> getmUnlockedPostIds() {
        return mUnlockedPostIds;
    }


    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public void setmFollowing(ArrayList<String> mFollowing) {
        this.mFollowing = mFollowing;
    }

    public void setmFollowers(ArrayList<String> mFollowers) {
        this.mFollowers = mFollowers;
    }

    public void setmUnlockedPostIds(ArrayList<String> mUnlockedPostIds) {
        this.mUnlockedPostIds = mUnlockedPostIds;
    }

    //keep empty c'tor for firebase downloading the user
    public LociUser() { }

    //new user c'tor
    public LociUser(String uid, String email, String displayName, String token) {
        mUserId = uid;
        mEmail = email;
        mName = displayName;
        mToken = token;
        mFollowing = new ArrayList<>();
        mFollowers = new ArrayList<>();
        mUnlockedPostIds = new ArrayList<>();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

//    public void SetUserId(String userId) {
//        mUserId = userId;
//    }
//
//    public String getmToken() {return mToken; }
//
//    public void setmToken(String mToken) {this.mToken = mToken; }
//
//    public String getmEmail() { return mEmail; }
//
//    public void setmEmail(String mEmail) {  this.mEmail = mEmail; }
//
//    public ArrayList<String> GetmUnlockedPostIds() {
//        return mUnlockedPostIds;
//    }
//
//    public ArrayList<String> getFollowing() {
//        return mFollowing;
//    }

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

    public ArrayList<String> getUnlockedPostIds() {
        return mUnlockedPostIds;
    }

    public String getUserId(){
        return mUserId;
    }


    //parcel garbage: ----------------------------------------------------------------------//
/*
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
*/

}
