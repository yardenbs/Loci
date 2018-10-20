package com.mta.loci;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    private LociUser mCurrentUser;
    private LociUser mProfileUser; //the user of the profile that we are looking at
    private StaticGridView mStaticGridView;
    private GridViewAdapter mStaticGridAdapter;
    private Button mFollowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        InitUI();
    }

    private void InitUI() {
        setContentView(R.layout.activity_user_profile);
        initButtons();

        // get currnt user
      //  mCurrentUser = LociUtil.getUserFromDatabase(FirebaseAuth.getInstance().getUid());
     //   mProfileUser = LociUtil.InitUserFromIntent(getIntent()); // get feom intent the user of the profile// todo : fix home activity user intent !!!

        if(mProfileUser.getUserId() != mCurrentUser.getUserId()) { // is this is my profile?
            mFollowButton.setVisibility(View.VISIBLE);

            mFollowButton.setText("Follow");
            if(mCurrentUser.getmFollowing().contains(mProfileUser.getUserId())){
                mFollowButton.setText("Unfollow");
            }
            // todo do i follow him?
        }

        InitGridView();
    }

    private void initButtons(){
        mFollowButton = findViewById(R.id.FollowButton);
        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFollowButton.getText() == "Follow"){
                    mCurrentUser.addNewFollowing(mProfileUser.getUserId());
                }
                else{
                    mCurrentUser.removeFollowing(mProfileUser.getUserId());
                }

            }
        });
    }


    private void InitGridView() {

        mStaticGridView = (StaticGridView) findViewById(R.id.staticGridView);
        mStaticGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        mStaticGridView.setAdapter(mStaticGridAdapter);
        mStaticGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                // Create intent to load Post Activity and add the clicked image info:
                Intent intent = new Intent(view.getContext(), LociPostActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());
                //Start details activity
                startActivity(intent);
            }
        });

        //mGridView.setOnTouchListener(new View.OnTouchListener(){
        //
        //    @Override
        //    public boolean onTouch(View v, MotionEvent event) {
        //        return event.getAction() == MotionEvent.ACTION_MOVE;
        //    }
        //
        //});
    }

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray images = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < images.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }

}
