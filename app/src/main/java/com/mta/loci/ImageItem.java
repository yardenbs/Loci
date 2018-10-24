package com.mta.loci;

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap image;
    private String creatorId;

    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.creatorId = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return creatorId;
    }

    public void setTitle(String creatorId) {
        this.creatorId = creatorId;
    }
}