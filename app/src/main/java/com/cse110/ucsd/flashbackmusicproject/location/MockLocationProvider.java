package com.cse110.ucsd.flashbackmusicproject.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;

/**
 * The class provides mock location names for testing.
 */
public class MockLocationProvider implements ILocationProvider {
    private ILocation loc;
    private LocationParser parser;

    public MockLocationProvider(ILocation location, Context context) {
        this.loc = location;
        parser = new LocationParser(context);
    }

    @Override
    public String getLocation() {
        return parser.getLocationName(loc);
    }


}