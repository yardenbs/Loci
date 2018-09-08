package com.mta.loci;

// implement an inheritance for each media type (photo, text... )

public abstract class MediaPlayer {

    String mMediaType;
    String mUrl;

    public MediaPlayer ( String type, String mediaUrl)
    {
        mUrl = mediaUrl;
        mMediaType = type;
    }

    public abstract void playMedia();

}

