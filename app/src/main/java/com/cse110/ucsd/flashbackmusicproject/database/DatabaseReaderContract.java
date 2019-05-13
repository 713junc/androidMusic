package com.cse110.ucsd.flashbackmusicproject.database;

/**
 * The class sets up the schema for the database and its table.
 */

public final class DatabaseReaderContract {

    private DatabaseReaderContract(){}

    public static class DatabaseEntry {
        public static final String TABLE_NAME = "Songs";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_FAVORITE = "Status";
        public static final String COLUMN_GENERAL_TIME = "General_Time";
        public static final String COLUMN_EXACT_TIME = "Exact_Time";
        public static final String COLUMN_LOCATION = "Location";
    }
}
