package com.mta.loci;



import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Post implements Parcelable {

    private long PostId;
    private String mCreatorId; //user who made the post
    private LatLng mLatlng; //where the post was made
    private long mDatePosted; //in seconds!; // for purging purposes
    private String mMediaUrl;
    private String mMediaType;
    private boolean mKill = false;

    Post(String creatorId, double lat, double lng, String url, String mediaType ) {
        mMediaUrl = url;
        mMediaType = mediaType;
        mCreatorId = creatorId;
        mLatlng = new LatLng(lat,lng);
    }


    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            //return new Post( ); // Todo: add params!
            return null;
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {}


    // @Yarden @Zuf
    // when the posts are loaded they are checked in terms of Date that
    // they are less than a day old. otherwise the mKill is set to True!
    // 1) outdated posts will not be loaded into the users map!
    // 2) this is also for the Firebase code:
    // This means Firebase may delete the post on a single daily purge routine.
    public boolean isKillable(){
        return this.mKill;
    }

    public boolean RequestKill(){

        if (mKill){
            return true;
        }

        if (PostUtils.GetIsOldPost(mDatePosted)){
            mKill = true;

            return true;
        }

        return false;
    }

    public long getPostId() {
        return PostId;
    }

    public void setPostId(long postId) {
        PostId = postId;
    }

    public String getCreatorId() {
        return mCreatorId;
    }

    public void setCreatorId(String mCreatorId) {
        this.mCreatorId = mCreatorId;
    }

    public LatLng getLatlng() {
        return mLatlng;
    }
}
