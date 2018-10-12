package com.mta.loci;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class PhotoPlayer extends AppCompatActivity {

    private ImageView mTakenImageView;
    private Uri mTakenImageUri;
    private static final String TAG_TAKE_PICTURE = "TAKE_PICTURE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitUI();
        Intent intent = getIntent();
        int i = intent.getIntExtra("1", 0);
        String imageUriString = intent.getStringExtra("imageUri");
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
            // Get all camera taken pictures in picture save folder.


            // Get content resolver object.
            ContentResolver contentResolver = getContentResolver();

            try {
                // Open display image input stream.
                InputStream inputStream = contentResolver.openInputStream(mTakenImageUri);

                // Decode the image input stream to a bitmap use BitmapFactory.
                Bitmap pictureBitmap = BitmapFactory.decodeStream(inputStream);

                // Set the image bitmap in the image view component to display it.
                mTakenImageView.setImageBitmap(pictureBitmap);

            } catch (FileNotFoundException ex) {
                Log.e(TAG_TAKE_PICTURE, ex.getMessage(), ex);
            }
        }
    }
}

