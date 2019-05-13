package com.cse110.ucsd.flashbackmusicproject.timer;

import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;

import java.util.Calendar;

import android.util.Log;

/**
 * The class is used to get general time and time in milliseconds.
 */

public class RealTimeKeeper implements TimeKeeper {

    private Calendar calendar;
    public static final String TAG = "RealTimeKeeper";


    public RealTimeKeeper(){
        Log.d(TAG, "Keep Time");
        calendar = Calendar.getInstance();
    }

    @Override
    public long getCurrentTimeInMillis() {
        calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    @Override
    public String getGeneralTime() {
        Log.d(TAG, "General Time");
        calendar = Calendar.getInstance();

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if(hourOfDay < 5){
            Log.d(TAG, "Evening");
            return Dictionary.EVENING;
        }else if(hourOfDay < 11){
            Log.d(TAG, "Morning");
            return Dictionary.MORNING;
        }else if(hourOfDay < 17){
            Log.d(TAG, "Afternoon");
            return Dictionary.AFTERNOON;
        }else{
            Log.d(TAG, "Evening");
            return Dictionary.EVENING;
        }
    }

    public int getMostRecentlyPlayedInDays( long lastlyPlayedInMillis) {
        long currentTime = getCurrentTimeInMillis();
        long differenceInMillis = currentTime - lastlyPlayedInMillis;
        int day = (int) ( differenceInMillis / (1000*60*60*24));
        return day;
    }
}
