package com.cse110.ucsd.flashbackmusicproject.location;

/**
 * MockLocation is used to test the location part of the app.
 */

public class MockLocation implements ILocation {

    double lon, lat;

    public MockLocation(double lat, double lon) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLongitude() {
        return lon;
    }

    public double getLatitude() {
        return lat;
    }
}
