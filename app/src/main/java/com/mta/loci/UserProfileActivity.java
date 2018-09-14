package com.mta.loci;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Vector;

public class UserProfileActivity extends AppCompatActivity {
    private LociUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        InitUserFromIntent(getIntent());
    }

    private void InitUserFromIntent(Intent intent){
        mUser = new LociUser();
        mUser.SetUserId(intent.getIntExtra("0", -1));
        mUser.SetUserPostsIds(intent.getIntegerArrayListExtra("1"));
        mUser.SetTotalPostsIds(intent.getIntegerArrayListExtra("2"));
        mUser.SetUnlockedPostsIds(intent.getIntegerArrayListExtra("3"));
    }
}
