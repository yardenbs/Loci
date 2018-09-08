package com.mta.loci;

public class PhotoPlayer extends MediaPlayer {

    public PhotoPlayer(String type, String url){
        super(type,url);
    }

    @Override
    public void playMedia() {
        System.out.print("shows the photo at "+ mUrl);
    }
}
