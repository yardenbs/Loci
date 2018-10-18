package com.mta.loci;



import com.google.android.gms.maps.model.LatLng;

public class Post {

    private String id;
    private String mCreatorId; //user who made the post
    private LatLng mLatlng; //where the post was made
    private long mDatePosted; //in seconds!; // for purging purposes
    private String mMediaUrl;
    private String mMediaType;
    private boolean mKill = false;

    public Post(){}

    Post(String id, String creatorId, double lat, double lng, String url, String mediaType ) {
        mCreatorId = creatorId;
        mMediaUrl = url;
        mMediaType = mediaType;
        mLatlng = new LatLng(lat,lng);
        mDatePosted = System.currentTimeMillis() / 1000; //current time in seconds!
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

        if (PostUtils.isOldPost(this)){
            mKill = true;

            return true;
        }

        return false;
    }

    public String getPostId() {
        return id;
    }

    public String getCreatorId() {
        return mCreatorId;
    }

    public LatLng getLatlng() {
        return mLatlng;
    }

    public long getDatePosted(){
        return mDatePosted;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public String getMediaType() {
        return mMediaType;
    }

//    public MarkerOptions generateMarkerOptions() {
//    }
}
