package com.cse110.ucsd.flashbackmusicproject.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.cse110.ucsd.flashbackmusicproject.MainActivity;

/**
 * The class provides location in both coordinates and real addresses. It requires the
 * user to grant location permission before executing.
 */

public class RealLocationProvider implements ILocationProvider {

    private ILocation location;
    private LocationParser parser;

    private static final String TAG = "RealLocationProvider";

    public RealLocationProvider(MainActivity context) {

        this.parser = new LocationParser(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d(TAG, "permissions granted");
        } else {
            Log.d(TAG, "permissions already granted");
        }

        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.GPS_PROVIDER;

            Location last = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (last != null) {
                this.location = new LocationAdapter(last);
            }

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    RealLocationProvider.this.location = new LocationAdapter(location);
                    Log.d(TAG, location.getLongitude() + " " + location.getLatitude());
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
        } catch (SecurityException e) {
            Toast.makeText(context, "Location layer not enabled.", Toast.LENGTH_LONG).show();
        }
    }

    public void setLocation(ILocation location) {
        this.location = location;
    }

    public String getLocation() {
        return location != null ? parser.getLocationName(location) : "Unknown";
    }
}