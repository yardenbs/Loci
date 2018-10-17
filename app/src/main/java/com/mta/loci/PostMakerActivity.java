package com.mta.loci;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostMakerActivity extends AppCompatActivity {

    private Post mPostToPublish = new Post();
    private LatLng mPostLocation;
    private Button mTakePictureButton;
    private Button mShootVideoButton;
    private Button mWriteTextButton;
    private Button mRecoredVoiceButton;
    private Button mDisplayContentButton;
    private LociUser mUser;
    private Uri mOutputImgUri;

    // This tag is used for error or debug log.
    private static final String TAG_TAKE_PICTURE = "TAKE_PICTURE";

    // This is the request code when start camera activity use implicit intent.
    public static final int REQUEST_CODE_TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitUI();
        LociUtil.InitUserFromIntent(getIntent(), mUser);
        mPostLocation = getIntent().getExtras().getParcelable("4");
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


        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Create a new temporary file.
                    File outputImageFile = CustomCreateImageFile();

                    // Get the output image file Uri wrapper object.
                    mOutputImgUri = Uri.fromFile(outputImageFile);

                    // Startup camera app.
                    // Create an implicit intent which require take picture action..
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Specify the output image uri for the camera app to save taken picture.
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputImgUri);
                    // Start the camera activity with the request code and waiting for the app process result.
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);

                } catch (IOException ex) {
                    Log.e(TAG_TAKE_PICTURE, ex.getMessage(), ex);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this,PostPublishActivity.class);
            intent.putExtra("mediaUri", mOutputImgUri.toString());
            intent.putExtra("latLng", mPostLocation);
            intent.putExtra("mediaType", "photo");
            startActivity(intent);
        }
    }



    private File CustomCreateImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",    // suffix
                storageDir      // directory
        );
        return image;
    }
}
