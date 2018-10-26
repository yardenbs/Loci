package com.mta.loci;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

class LociUtil {

    public static void loadImage(String url, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        
                        .fitCenter())
                .transition(withCrossFade())
                .into(imageView);
    }

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
