package com.cse110.ucsd.flashbackmusicproject.utility;

/**
 * Created by Derek on 3/29/2018.
 */

public class TimeFormatter {

    public static String getTime(int milli) {
        int seconds = milli / 1000;
        int minutes = seconds / 60;
        return minutes + ":" + (seconds % 60);
    }

}
