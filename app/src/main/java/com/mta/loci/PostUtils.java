package com.mta.loci;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;

class PostUtils {
    public static final long SECONDS_IN_DAY = 24*60*60;
    public static final double MINIMUM_UNLOCKING_DISTANCE = 15;

    public static boolean isOldPost(Post post){
        return (System.currentTimeMillis() / 1000) > post.getmDatePosted() + PostUtils.SECONDS_IN_DAY;
    }

    //generate markerOptions object from the post provided
    public static MarkerOptions GenerateMarkerOptions(Post post) {
        MarkerOptions mo = new MarkerOptions().position(post.getmLatlng())
                .title(post.getmCreatorName());
        //TODO    user.icon(); need to get the user's icon here

        return mo;
    }

    public static boolean AttemptUnlock(Location origin, Post post){
        return MINIMUM_UNLOCKING_DISTANCE <= getDistanceBetween(
                new LatLng(origin.getLatitude(), origin.getLatitude()), post.getmLatlng());
    }

    private static Double getDistanceBetween(LatLng latLon1, LatLng latLon2) {
        if (latLon1 == null || latLon2 == null)
            return null;
        float[] result = new float[1];
        Location.distanceBetween(latLon1.latitude, latLon1.longitude,
                latLon2.latitude, latLon2.longitude, result);
        return (double) result[0];
    }

    public static void getPostFromDatabase(String postId , String uid) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databasePosts = database.getReference("Posts");

        databasePosts.child(uid).child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post= dataSnapshot.getValue(Post.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LociUtil.class.getSimpleName(), "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    public static Bitmap rotateImage(Bitmap bitmap, String imageFileLocation){
        ExifInterface exifIF = null;
        try{
            exifIF = new ExifInterface(imageFileLocation);
        } catch (IOException e){
            e.printStackTrace();
        }
        int orientation = exifIF.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();

        switch (orientation) {
            case (ORIENTATION_ROTATE_90) :
                matrix.setRotate(90);
                break;
            case (ORIENTATION_ROTATE_180) :
                matrix.setRotate(180);
                break;
            default:
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }

    public static Bitmap resizeMapIcons(Bitmap bitmap, int width, int height){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return resizedBitmap;
    }


}
