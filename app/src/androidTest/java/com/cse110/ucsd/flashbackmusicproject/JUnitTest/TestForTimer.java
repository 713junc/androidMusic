package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContentProvider;

import com.cse110.ucsd.flashbackmusicproject.timer.RealTimeKeeper;
import com.cse110.ucsd.flashbackmusicproject.timer.MockTimeKeeper;
import com.cse110.ucsd.flashbackmusicproject.timer.TimeKeeper;
import com.cse110.ucsd.flashbackmusicproject.timer.TimeProvider;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.sql.Time;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


/**
 * Created by jungwon on 2/17/18.
 */

public class TestForTimer {

    //1. Instance of TimeProvider
    private TimeProvider timeProvider;
    private MockTimeKeeper mockTimeKeeper;
    private long timeInMillis;



    @Test
    public void testgetCurrentTimeInMillis(){
        timeInMillis = 123;
        mockTimeKeeper = new MockTimeKeeper(timeInMillis);
        timeProvider = new TimeProvider(mockTimeKeeper);
        //3.Using MockTimeKeeper and set some time on MockTimeKeeper(in milliseconds)

        mockTimeKeeper.setTimeInMillis(timeInMillis);
        //timeProvider.getCurrentTimeInMillis();

        mockTimeKeeper.getCurrentTimeInMillis();
        timeProvider.getCurrentTimeInMillis();

        assertEquals(123, mockTimeKeeper.getCurrentTimeInMillis());
        assertEquals(123, timeProvider.getCurrentTimeInMillis());













    }






}
