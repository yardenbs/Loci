package com.mta.loci;

import android.graphics.Bitmap;

public class ImageItem {
    private String image;
    private String creatorId;

    public ImageItem(String image, String title) {
        super();
        this.image = image;
        this.creatorId = title;
    }

    public String getTitle() {
        return creatorId;
    }

    public void setTitle(String creatorId) {
        this.creatorId = creatorId;
    }
}