package com.mta.loci;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LociUser implements Parcelable {

    private String mName;
    private String mUserId;
    private ArrayList<String> mUserPostsIds;
    private ArrayList<String> mTotalPostsIds;
    private ArrayList<String> mUnlockedPostsIds;
    private ArrayList<String> mFollowing; // list of Uid users that i folllow
    private ArrayList<String> mFollowers; // list of Uid users that folllow me

    //keep empty c'tor for firebase downloading the user
    public LociUser() { }

    public LociUser(String userId, String name) {
        mUserId = userId;
        mName = name;
        //TODO add more fhilds
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

    public ArrayList<String> getFollowing() {
        return mFollowing;
    }

    public void addNewFollowing(String Uid) {
        mFollowing.add(Uid);
        // todo add the data to database
        // todo add me to this user followers
    }
    public void removeFollowing(String Uid) {
        mFollowing.remove(Uid);
        // todo remove the data to database
        // todo remove me from this user followers
    }
}
