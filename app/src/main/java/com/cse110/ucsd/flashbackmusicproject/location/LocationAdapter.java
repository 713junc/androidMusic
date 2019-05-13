package com.cse110.ucsd.flashbackmusicproject.location;

import android.location.Location;

/**
 * The class is an adapter to convert Location to ILocation.
 */

public class LocationAdapter implements ILocation {

    private Location location;

    public LocationAdapter(Location location) {
        this.location = location;
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public double getLatitude() {
        return location.getLatitude();
    }
}
