package com.mta.loci;

import android.provider.MediaStore;

class Post {
    //members: UserId, Location, Media (Interface)
    private String UserId;
    private MediaPresentor mp;

    Post(String UserId, MediaPresentor mp) {
        //add inputs for c'tor
        this.UserId = UserId;
        this.mp = mp;
    }
}
