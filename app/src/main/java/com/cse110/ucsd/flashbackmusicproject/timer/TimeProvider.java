package com.cse110.ucsd.flashbackmusicproject.timer;

/**
 * The class provides time in milliseconds.
 */

import android.util.Log;

public class TimeProvider {
    TimeKeeper timeKeeper;
    public static final String TAG = "TimeProvider";


    public TimeProvider(TimeKeeper timeKeeper) {
        Log.d(TAG, "Provide Time");
        this.timeKeeper = timeKeeper;
    }

    public long getCurrentTimeInMillis() {
        return timeKeeper.getCurrentTimeInMillis();
    }

}
