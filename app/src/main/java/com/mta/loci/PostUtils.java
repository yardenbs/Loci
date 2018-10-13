package com.mta.loci;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

class PostUtils {
    public static final long SECONDS_IN_DAY = 24*60*60;
    public static final double MINIMUM_UNLOCKING_DISTANCE = 15;
    private static Post mPost = new Post();

    public static boolean isOldPost(Post post){
        return new Date().after(new Date(post.getDatePosted() + PostUtils.SECONDS_IN_DAY));
    }

    //generate markerOptions object from the post provided
    public static MarkerOptions GenerateMarkerOptions(Post post) {
        LociUser user = LociUtil.getUserFromDatabase(post.getCreatorId());
        MarkerOptions mo = new MarkerOptions().position(post.getLatlng())
                .title(user.getName()); //TODO    user.icon(); need to get the user's icon here

        return mo;
    }

    public static boolean AttemptUnlock(Location origin, Post post){
        return MINIMUM_UNLOCKING_DISTANCE <= getDistanceBetween(
                new LatLng(origin.getLatitude(), origin.getLatitude()), post.getLatlng());
    }

    private static Double getDistanceBetween(LatLng latLon1, LatLng latLon2) {
        if (latLon1 == null || latLon2 == null)
            return null;
        float[] result = new float[1];
        Location.distanceBetween(latLon1.latitude, latLon1.longitude,
                latLon2.latitude, latLon2.longitude, result);
        return (double) result[0];
    }

    public static Post getPostFromDatabase(final String postId ) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databasePosts = database.getReference("posts");

        databasePosts.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPost = dataSnapshot.getValue(Post.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LociUtil.class.getSimpleName(), "loadPost:onCancelled", databaseError.toException());
            }
        });

        return mPost;
    }
}
