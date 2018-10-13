package com.mta.loci;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LociUser implements Parcelable {

    private String Name;
    private int mUserId;
    private ArrayList<Integer> mUserPostsIds;
    private ArrayList<Integer> mTotalPostsIds;
    private ArrayList<Integer> mUnlockedPostsIds;

    public LociUser() {
        mUserId = 10; // TODO: get unique user id from Firebase
        mUserPostsIds = new ArrayList<>();
        mTotalPostsIds = new ArrayList<>();
        mUnlockedPostsIds = new ArrayList<>();

        // text //fix
        mUserPostsIds.add(1);
        mUserPostsIds.add(1);
        mUserPostsIds.add(1);
        mUserPostsIds.add(1);
        mUserPostsIds.add(1);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    public Integer GetUserId() {
        return mUserId;
    }

    public void SetUserId(Integer userId) {
        mUserId = userId;
    }

    public ArrayList<Integer> GetUserPostsIds() {
        return mUserPostsIds;
    }

    public void SetUserPostsIds(ArrayList<Integer> userPostsIds) {
        mUserPostsIds = userPostsIds;
    }

    public ArrayList<Integer> GetTotalPostsIds() {
        return mTotalPostsIds;
    }

    public void SetTotalPostsIds(ArrayList<Integer> totalPostsIds) {
        mTotalPostsIds = totalPostsIds;
    }

    public ArrayList<Integer> GetUnlockedPostsIds() {
        return mUnlockedPostsIds;
    }

    public void SetUnlockedPostsIds(ArrayList<Integer> unlockedPostsIds) {
        mUnlockedPostsIds = unlockedPostsIds;
    }

    public void AddUserPostId(Integer userPostId) {
        mUserPostsIds.add(userPostId);
    }

    public void AddTotalPostsId(Integer lockedPostsId) {
        mTotalPostsIds.add(lockedPostsId);
    }

    public void AddUnlockedPostsId(Integer unlockedPostsId) {
        mUserPostsIds.add(unlockedPostsId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mUserId);
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
        mUserId = in.readInt();
    }
}
