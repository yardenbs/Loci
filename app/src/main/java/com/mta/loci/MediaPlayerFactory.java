package com.mta.loci;

import com.google.android.gms.maps.model.BitmapDescriptor;

public class MediaPlayerFactory {

    public static MediaPlayer createMediaPlayer(String mediaType, String url) {

        switch (mediaType){
            case "photo": return new PhotoPlayer(mediaType, url);
            case "text": return new TextPlayer(mediaType, url);
            default: return new TextPlayer(mediaType, url);
        }
    }
}
