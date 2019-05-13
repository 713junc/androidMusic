package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;

import com.cse110.ucsd.flashbackmusicproject.database.MyLocalDatabase;

/**
 * Database helper to fill up a test database.
 */

public class MockMyLocalDatabase extends MyLocalDatabase {
    public static final String NAME = "testers.db";
    public MockMyLocalDatabase(Context context ) {
        super( context, NAME );
    }



}
