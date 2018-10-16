package com.mta.loci;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoUtils {

    public static String getLatLngAddress(LatLng latLng, Context context) {
        String currentAddress = "";
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);


            if (addresses.size() > 0) {
                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex();
                     i++)
                    currentAddress += addresses.get(0).getAddressLine(i) + " ";
            }
            //Toast.makeText(context, currentAddress, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currentAddress;
    }
}
