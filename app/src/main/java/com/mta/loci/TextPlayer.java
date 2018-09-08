package com.mta.loci;

public class TextPlayer extends MediaPlayer {

    public TextPlayer(String type, String mediaUrl) {
        super(type, mediaUrl);
    }

    @Override
    public void playMedia() {
        System.out.print("shows the text at "+ mUrl);
    }
}
