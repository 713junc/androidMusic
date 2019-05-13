package com.cse110.ucsd.flashbackmusicproject.timer;

import android.util.Log;

/**
 * This file is a mock version of the RealTimeKeeper class. This is used
 * to return some random time that has been set by the tester.
 */

public class MockTimeKeeper implements TimeKeeper {

    private long timeInMillis;
    public static final String TAG = "MockTimeKeeper";


    public MockTimeKeeper(long timeInMillis) {
        Log.d(TAG, "Keep Time");
        this.timeInMillis = timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }
    
    @Override
    public long getCurrentTimeInMillis() {
        return (timeInMillis);
    }

    @Override
    public String getGeneralTime() {
        return null;
    }

    @Override
    public int getMostRecentlyPlayedInDays( long timeInMillis) { return 0; }
}
