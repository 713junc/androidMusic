package com.cse110.ucsd.flashbackmusicproject.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;


import com.google.firebase.database.Exclude;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * This utility class provides location names based on coordinates.
 */

public class LocationParser {

    private Geocoder geocoder;
    public static final String TAG = "LocationParser";


    public LocationParser(Context context) {
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public String getLocationName(ILocation location) {

        String name = "Unknown";

        try {
            List<Address> list = geocoder.getFromLocation(location
                    .getLatitude(), location.getLongitude(), 1);
            if (list != null & list.size() > 0) {
                Address address = list.get(0);
                if (address.getLocality() == null) {
                    Log.d(TAG, "Something went wrong with the geocoder");
                    name = address.getCountryName();
                } else {
                    name = address.getLocality();
                }
                return name;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
            e.printStackTrace();
        }

        return name;
    }

}
