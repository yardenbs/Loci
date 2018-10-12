package com.mta.loci;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;

public class PostMakerActivity extends AppCompatActivity {


    private LatLng mPostLocation;
    private Button mTakePictureButton;
    private Button mShootVideoButton;
    private Button mWriteTextButton;
    private Button mRecoredVoiceButton;
    private Button mDisplayContentButton;

    private LociUser mUser;
    private Uri mOutputImgUri;
    private String mImageFilePath;

    // This tag is used for error or debug log.
    private static final String TAG_TAKE_PICTURE = "TAKE_PICTURE";

    // This is the request code when start camera activity use implicit intent.
    public static final int REQUEST_CODE_TAKE_PICTURE = 1;

    // Save the camera taken picture in this folder.
    private File pictureSaveFolderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitUI();
        LociUtil.InitUserFromIntent(getIntent(), mUser);
        mPostLocation = getIntent().getExtras().getParcelable("4");
        mImageFilePath = getFilesDir().getPath().toString() + "/";
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    private void InitUI() {
        setContentView(R.layout.activity_post_maker);
        InitButtons();
    }

    private void InitButtons() {
        mTakePictureButton = (Button) findViewById(R.id.takePictureButton);
        mShootVideoButton = (Button) findViewById(R.id.shootVideoButton);
        mWriteTextButton = (Button) findViewById(R.id.writeTextButton);
        mRecoredVoiceButton = (Button) findViewById(R.id.recordVoiceButton);
        mDisplayContentButton = (Button) findViewById(R.id.displayContentButton);


        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Create a random image file name.
                    String imageFileName = "outputImage_" + System.currentTimeMillis() + ".png";

                    // Construct an output file to save camera taken picture temporary.
                    File outputImageFile = new File(mImageFilePath, imageFileName);

                    // If cached temporary file exist then delete it.
                    if (outputImageFile.exists()) {
                        outputImageFile.delete();
                    }

                    // Create a new temporary file.
                    outputImageFile.createNewFile();

                    // Get the output image file Uri wrapper object.
                    mOutputImgUri = Uri.fromFile(outputImageFile);

                    // Startup camera app.
                    // Create an implicit intent which require take picture action..
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Specify the output image uri for the camera app to save taken picture.
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputImgUri);
                    // Start the camera activity with the request code and waiting for the app process result.
                    startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);

                } catch (IOException ex) {
                    Log.e(TAG_TAKE_PICTURE, ex.getMessage(), ex);
                }
            }
        });

        mDisplayContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),PhotoPlayer.class);
                intent.putExtra("1",1);
                intent.putExtra("imageUri", mOutputImgUri.toString());
                startActivity(intent);
            }
        });
    }
}
