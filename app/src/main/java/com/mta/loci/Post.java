package com.mta.loci;



public class Post {

    private String id;
    private String mCreatorId; //user who made the post
    private String mCreatorName;
    private com.mta.loci.LatLng mLatlng; //where the post was made
    private long mDatePosted; //in seconds!; // for purging purposes


    private String mMediaUrl;
    private String mMediaType;
    private boolean mKill = false;

    public Post(){}

    Post(String id, String creatorId, double lat, double lng, String url, String mediaType , String name) {
        this.id = id;
        mCreatorId = creatorId;
        mCreatorName = name;
        mMediaUrl = url;
        mMediaType = mediaType;
        mLatlng = new com.mta.loci.LatLng(lat,lng);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmCreatorId() {
        return mCreatorId;
    }

    public void setmCreatorId(String mCreatorId) {
        this.mCreatorId = mCreatorId;
    }

    public com.google.android.gms.maps.model.LatLng getmLatlng() {
        return new com.google.android.gms.maps.model.LatLng(mLatlng.latitude, mLatlng.longitude);
    }

    public void setmLatlng(com.mta.loci.LatLng mLatlng) {
        this.mLatlng = mLatlng;
    }

    public long getmDatePosted() {
        return mDatePosted;
    }

    public void setmDatePosted(long mDatePosted) {
        this.mDatePosted = mDatePosted;
    }

    public String getmMediaUrl() {
        return mMediaUrl;
    }

    public void setmMediaUrl(String mMediaUrl) {
        this.mMediaUrl = mMediaUrl;
    }

    public String getmMediaType() {
        return mMediaType;
    }

    public void setmMediaType(String mMediaType) {
        this.mMediaType = mMediaType;
    }

    public boolean ismKill() {
        return mKill;
    }

    public void setmKill(boolean mKill) {
        this.mKill = mKill;
    }

    public String getmCreatorName() {
        return mCreatorName;
    }

    public void setmCreatorName(String mCreatorName) {
        this.mCreatorName = mCreatorName;
    }
}
