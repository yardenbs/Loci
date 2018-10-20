package com.mta.loci;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

class LociUtil {

    private static LociUser mLociUser = new LociUser();
/*
    public static LociUser InitUserFromIntent(Intent intent) {
        String uid = intent.getStringExtra("0");
        LociUser retUser =  getUserFromDatabase(uid);

        return retUser;
    }
*/
    public static String updateLocationAddress(Context context, LatLng latLng) {

        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            String currentAddress = "";
            if (addresses.size() > 0) {
                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++)
                    currentAddress += addresses.get(0).getAddressLine(i) + " ";
            }

            Toast.makeText(context, currentAddress, Toast.LENGTH_SHORT).show();

            return currentAddress;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void getUserFromDatabase(final OnUserFromDBCallback onUserFromDBCallback, String uid ) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseUsers = database.getReference("Users");

        databaseUsers.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLociUser = dataSnapshot.getValue(LociUser.class);
                onUserFromDBCallback.update(mLociUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LociUtil.class.getSimpleName(), "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void updateUserFromDB(final OnUserFromDBCallback onUserFromDBCallback,String uid){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("Posts");
        DatabaseReference UsersRef = database.getReference("Users");

        postsRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postIdSnapshot: dataSnapshot.getChildren()) {
                    mLociUser.getUserPostsIds().add(postIdSnapshot.getKey());
                }
                
                onUserFromDBCallback.update(mLociUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("Firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        UsersRef.child(uid).child("UnlockedPosts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postIdSnapshot: dataSnapshot.getChildren()) {
                    mLociUser.getUnlockedPostsIds().add(postIdSnapshot.getValue().toString());
                }

                onUserFromDBCallback.update(mLociUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("Firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


        UsersRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userImFollowing: dataSnapshot.child("following").getChildren()) {
                    mLociUser.getFollowing().add(userImFollowing.toString());
                }

                for (DataSnapshot userFollowsMe: dataSnapshot.child("followers").getChildren()) {
                    mLociUser.getmFollowers().add(userFollowsMe.toString());
                }

                onUserFromDBCallback.update(mLociUser);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("Firebase", "loadFollowers:onCancelled", databaseError.toException());
                // ...
            }
        });

    }


    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            return user.getUid();
        }

        return null;
    }

    public static void goHome(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }


}
