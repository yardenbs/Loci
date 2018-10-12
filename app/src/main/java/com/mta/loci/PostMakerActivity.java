package com.mta.loci;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;

import com.google.android.gms.maps.model.LatLng;

public class PostMakerActivity extends AppCompatActivity {


    private LociUser mUser;
    private LatLng mPostLocation;
    private TextureView mTextureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_maker);
        LociUtil.InitUserFromIntent(getIntent(), mUser);
        mPostLocation = getIntent().getExtras().getParcelable("4");

        mTextureView = (TextureView)findViewById(R.id.textureView2);
    }
}
