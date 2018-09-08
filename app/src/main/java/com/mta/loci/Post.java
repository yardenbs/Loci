package com.mta.loci;



import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Date;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class Post implements Parcelable {

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post( in.readDouble(), in.readDouble(), in.readString(),  in.readString() ); // Todo: add params!
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };


    long PostId;
    private FirebaseUser mUser; //user who made the post
    private LatLng mlatlng; //where the post was made
    private Date mDatePosted; // for purging purposes
    private String mMediaUrl;
    private String mMediaType;

    public MediaPlayer mMediaPlayer;
    private boolean mKill = false;


    Post(double lat, double lng, String url, String mediaType) {
        //
        // TODO: PostId initialize
        //
        mMediaUrl = url;
        mMediaType = mediaType;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mlatlng = new LatLng(lat,lng);
        mDatePosted = new Date();
        mMediaPlayer = MediaPlayerFactory.createMediaPlayer(mediaType, url);
    }

    //generates a marker from the post data
    private MarkerOptions generateMarkerFromPost() {
        MarkerOptions mo = new MarkerOptions().position(this.mlatlng)
                .title(mUser.getDisplayName()); //TODO    .icon(); need to get the user's icon here

        return mo;
    }


    public String getMedia(){
        return mMediaUrl;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
