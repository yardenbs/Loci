package com.mta.loci;

import java.util.ArrayList;

public class LociUser {

    private String mPhotoUrl;
    private String mName;
    private String mUserId;
    private String mEmail;
    private String mToken;
    private ArrayList<String> mFollowing = new ArrayList<>(); // list of Uid users that i follow
    private ArrayList<String> mFollowers = new ArrayList<>(); // list of Uid users that follow me
    private ArrayList<String> mUnlockedPostIds = new ArrayList<>(); // list of Uids of posts that are viewable

    //keep empty c'tor for firebase downloading the user
    public LociUser() { }

    //new user c'tor
    public LociUser(String uid, String email, String displayName, String token, String photoUrl) {
        mUserId = uid;
        mEmail = email;
        mName = displayName;
        mToken = token;
        mPhotoUrl = photoUrl;
        mFollowing = new ArrayList<>();
        mFollowers = new ArrayList<>();
        mUnlockedPostIds = new ArrayList<>();
    }

    public String getmUserId() {
        return mUserId;
    }

    public ArrayList<String> getmFollowingUIDs() {
        return mFollowing;
    }

    public ArrayList<String> getmUnlockedPostIds() {
        return mUnlockedPostIds;
    }

    public String getmPhotoUrl() { return mPhotoUrl; }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }


   public String getmToken() {return mToken; }
//
    public void setmToken(String mToken) {this.mToken = mToken; }
//
   public String getmEmail() { return mEmail; }
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
    }

    public void removeFollowing(String Uid) {
        mFollowing.remove(Uid);
    }

    public ArrayList<String> getmFollowersUIDs() {
        return mFollowers;
    }

    public ArrayList<String> getUnlockedPostIds() {
        return mUnlockedPostIds;
    }

    public String getUserId(){
        return mUserId;
    }
}
