package com.mta.loci;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LociUser implements Parcelable {

    private String mName;
    private String mUserId;
    private String mEmail;
    private String mToken;
    private ArrayList<String> mFollowing = new ArrayList<>(); // list of Uid users that i folllow
    private ArrayList<String> mFollowers = new ArrayList<>(); // list of Uid users that folllow me
    private ArrayList<String> mUserPostsIds = new ArrayList<>(); //my posts
    private ArrayList<String> mTotalPostsIds = new ArrayList<>(); //all the posts of those I am following + mine
    private ArrayList<String> mUnlockedPostsIds = new ArrayList<>(); //all the viewable (unlocked) posts (mine + the ones I unlocked)

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
        mUserPostsIds = new ArrayList<>();
        mTotalPostsIds = new ArrayList<>();
        mUnlockedPostsIds = new ArrayList<>();
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

    public String getmToken() {return mToken; }

    public void setmToken(String mToken) {this.mToken = mToken; }

    public String getmEmail() { return mEmail; }

    public void setmEmail(String mEmail) {  this.mEmail = mEmail; }



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
