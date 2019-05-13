package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.database.sqlite.SQLiteOpenHelper;

import com.cse110.ucsd.flashbackmusicproject.database.MyFirebaseDatabase;
import com.cse110.ucsd.flashbackmusicproject.user.User;

/**
 * Created by sseung385 on 3/13/18.
 */

public class MockFirebaseDatabase extends MyFirebaseDatabase{
    public static final String NAME = "testers.db";
    public MockFirebaseDatabase( User user) {
        super( user );
    }
}
