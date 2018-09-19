package com.mta.loci;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Vector;

public class UserProfileActivity extends AppCompatActivity {
    private LociUser mUser;
    private StaticGridView mStaticGridView;
    private GridViewAdapter mStaticGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        InitUserFromIntent(getIntent());
        InitUI();
    }

    private void InitUserFromIntent(Intent intent) {
        mUser = new LociUser();
        mUser.SetUserId(intent.getIntExtra("0", -1));
        mUser.SetUserPostsIds(intent.getIntegerArrayListExtra("1"));
        mUser.SetTotalPostsIds(intent.getIntegerArrayListExtra("2"));
        mUser.SetUnlockedPostsIds(intent.getIntegerArrayListExtra("3"));
    }

    private void InitUI() {
        setContentView(R.layout.activity_user_profile);
        InitGridView();
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
