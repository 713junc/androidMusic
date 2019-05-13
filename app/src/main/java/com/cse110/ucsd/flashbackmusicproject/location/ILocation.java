package com.cse110.ucsd.flashbackmusicproject.location;

/**
 * Interface to represent a location in terms of its coordinates. This applies
 * dependency inversion.
 */

public interface ILocation {

    public double getLatitude();
    public double getLongitude();

}
