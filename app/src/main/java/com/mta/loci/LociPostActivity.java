package com.mta.loci;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class LociPostActivity extends AppCompatActivity implements  OnUserFromDBCallback, OnPostFromDBCallback {

    private  Post mPost;
    private LociUser mCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loci_post);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_loci_post);

        String creatorId = getIntent().getStringExtra("creatorId");
        String postId = getIntent().getStringExtra("postId");


        ImageView postImageView = (ImageView) findViewById(R.id.image);
        PostUtils.getPostFromDatabase(this, postId, creatorId);

        if(mPost != null) {
           loadImage(mPost.getMediaUrl(), postImageView);
           TextView creatorTextView = (TextView) findViewById(R.id.creator);
           LociUtil.getUserFromDatabase(this,creatorId);
           creatorTextView.setText(mCreator.getName());
       }

    }

    private   void loadImage(String url, ImageView imageView) {
        Context context = imageView.getContext();
        ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(context, R.color.common_google_signin_btn_text_light_default));
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(cd)
                        .centerCrop())
                .transition(withCrossFade())
                .into(imageView);
    }

    @Override
    public void update(LociUser user) {
        mCreator = user;
    }

    @Override
    public void UpdateFromDB(Post post) {
        mPost = post;
    }
}
