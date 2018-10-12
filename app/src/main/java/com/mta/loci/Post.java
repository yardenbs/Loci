package com.mta.loci;



import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class Post implements Parcelable {

    long PostId;
    private FirebaseUser mUser; //user who made the post TODO: change to LociUser type
    private LatLng mLatlng; //where the post was made

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

    //TODO: remove the right part to the class that makes the post!
    private long mDatePosted = System.currentTimeMillis()/1000; //in seconds!; // for purging purposes
    private final long SECONDS_IN_DAY = 24*60*60;
    private final int MINIMUM_UNLOCKING_DISTANCE = 15;
    private String mMediaUrl;
    private String mMediaType;
    private boolean mIsLocked = true;
    public MediaPlayer mMediaPlayer;
    private boolean mKill = false;

    Post(boolean isLocked, long datePosted, double lat, double lng, String url, String mediaType, FirebaseUser creator) {
        //
        // TODO: PostId initialize
        //
        mMediaUrl = url;
        mMediaType = mediaType;
        mUser = creator;
        mLatlng = new LatLng(lat,lng);
        mDatePosted = System.currentTimeMillis()/1000; //in seconds!
        mMediaPlayer = MediaPlayerFactory.createMediaPlayer(mediaType, url);
    }

    //generates a marker from the post data
    public MarkerOptions GenerateMarkerOptions() {
        MarkerOptions mo = new MarkerOptions().position(this.mLatlng)
                .title(mUser.getDisplayName()); //TODO    .icon(); need to get the user's icon here

        return mo;
    }

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

        if (this.GetIsOldPost()){
            mKill = true;

            return true;
        }

        return false;
    }

    private boolean GetIsOldPost(){
        return new Date().after(new Date(mDatePosted + SECONDS_IN_DAY));
    }

}
