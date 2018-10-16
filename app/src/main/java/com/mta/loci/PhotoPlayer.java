package com.mta.loci;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class PhotoPlayer extends AppCompatActivity {

    private ImageView mTakenImageView;
    private Uri mTakenImageUri;
    private Bitmap mTakenImageBitmap = null;
    private static final String TAG_TAKE_PICTURE = "TAKE_PICTURE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitUI();
        Intent intent = getIntent();
        int i = intent.getIntExtra("1", 0);
        String imageUriString = intent.getStringExtra("mediaUri");
        if (imageUriString == null)


            Log.e("IMAGE URI IS NULL", "NULL");
        mTakenImageUri = Uri.parse(imageUriString);
        LoadAndDisplayTakenImage();
    }

    private void InitUI() {
        setContentView(R.layout.activity_photo_player);
        mTakenImageView = (ImageView) findViewById(R.id.takenImageView);
    }

    private void LoadAndDisplayTakenImage() {
        if (mTakenImageUri != null) {
            try {
                mTakenImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTakenImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mTakenImageView.setImageBitmap(mTakenImageBitmap);
        }
    }
}

