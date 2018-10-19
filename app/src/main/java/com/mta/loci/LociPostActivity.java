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

public class LociPostActivity extends AppCompatActivity {

    private  Post mPost;

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
       mPost =  PostUtils.getPostFromDatabase(postId, creatorId);
       if(mPost != null) {
           loadImage(mPost.getMediaUrl(), postImageView);
           TextView creatorTextView = (TextView) findViewById(R.id.creator);
           LociUser creator =  LociUtil.getUserFromDatabase(creatorId);
           creatorTextView.setText(creator.getName());
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
}
