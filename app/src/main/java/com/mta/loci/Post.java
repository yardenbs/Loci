package com.mta.loci;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Date;

class Post {
    //members: UserId, Location, Media (Interface)
    private String mUserId;
    private MediaPresenter mMediaPresenter;
    private LatLng mlatlng;
    private Date mtimeOfPost;
    private Marker mMarker;

    Post(String UserId, MediaPresenter mp, LatLng latlng) {
        //add inputs for c'tor
        this.mUserId = UserId;
        this.mMediaPresenter = mp;
        this.mlatlng = latlng;
    }

}
