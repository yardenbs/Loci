package com.mta.loci;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

class LociUtil {

    public static void InitUserFromIntent(Intent intent, LociUser mUser) {
        mUser = new LociUser();
        mUser.SetUserId(intent.getIntExtra("0", -1));
        mUser.SetUserPostsIds(intent.getIntegerArrayListExtra("1"));
        mUser.SetTotalPostsIds(intent.getIntegerArrayListExtra("2"));
        mUser.SetUnlockedPostsIds(intent.getIntegerArrayListExtra("3"));
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
}
