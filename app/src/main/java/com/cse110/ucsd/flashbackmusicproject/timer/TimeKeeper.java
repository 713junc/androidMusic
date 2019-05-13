package com.cse110.ucsd.flashbackmusicproject.timer;

/**
 * Interface to make classes that provide time. Based on Dependency Inversion.
 */
public interface TimeKeeper {
    public long getCurrentTimeInMillis();
    public String getGeneralTime();
    public int getMostRecentlyPlayedInDays( long lastlyPlayedInMillis );
}
