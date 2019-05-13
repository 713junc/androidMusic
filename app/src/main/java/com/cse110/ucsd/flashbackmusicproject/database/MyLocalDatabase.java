package com.cse110.ucsd.flashbackmusicproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.user.User;
import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;
import com.cse110.ucsd.flashbackmusicproject.utility.StringArrayParser;

import java.util.List;

/**
 * The class controls the sqlite database, and provides implementations for CRUD.
 */

public class MyLocalDatabase extends SQLiteOpenHelper implements IDatabase {

    // database version
    public static final int DATABASE_VERSION = 5;

    // save file location
    public static final String NAME = "songs.db";

    // sql query to create table with columns
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseReaderContract.DatabaseEntry.TABLE_NAME + " (" +
                    DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " TEXT," +
                    DatabaseReaderContract.DatabaseEntry.COLUMN_FAVORITE + " INT," +
                    DatabaseReaderContract.DatabaseEntry.COLUMN_LOCATION + " TEXT," +
                    DatabaseReaderContract.DatabaseEntry.COLUMN_GENERAL_TIME + " TEXT," +
                    DatabaseReaderContract.DatabaseEntry.COLUMN_EXACT_TIME + " TEXT)";

    // sql query to delete table
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseReaderContract.DatabaseEntry.TABLE_NAME;

    public static final String TAG = "MyLocalDatabase";


    /**
     * Database ctor()
     * Initializes a MyLocalDatabase which delegates call to a SQLiteDatabase
     *
     * @param context - Context of the database
     */
    public MyLocalDatabase(Context context) {
        super(context, NAME, null, DATABASE_VERSION);
    }

    public MyLocalDatabase(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
    }

    /**
     * Determines whether or not a song key is in the database
     *
     * @param title
     * @return
     */
    public boolean exists(String title) {

        Log.d(TAG, "Song exists in the database");

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE};

        String selection = DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " = ?";
        String[] selectionArgs = {title};

        Cursor cursor = db.query(DatabaseReaderContract.DatabaseEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        boolean status = false;

        if (cursor.getCount() > 0) {
            status = true;
        }
        cursor.close();
        return status;
    }

    /**
     * Adds an entry to the database table with all columns empty
     *
     * @param song - name of the song to add
     */
    public void add(Song song) {
        String title = song.getTitle();
        Log.d(TAG, "Adds an entry to the database");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE, title);
        values.put(DatabaseReaderContract.DatabaseEntry.COLUMN_FAVORITE, Dictionary.NEUTRAL);
        values.put(DatabaseReaderContract.DatabaseEntry.COLUMN_LOCATION, "");
        values.put(DatabaseReaderContract.DatabaseEntry.COLUMN_GENERAL_TIME, "");
        values.put(DatabaseReaderContract.DatabaseEntry.COLUMN_EXACT_TIME, "");

        db.insert(DatabaseReaderContract.DatabaseEntry.TABLE_NAME, null, values);
    }

    /**
     * Deletes a song key from the database
     *
     * @param title - name of the song to delete
     */
    public void delete(String title) {

        Log.d(TAG, "Delete a song from the database");

        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {title};
        // Issue SQL statement.
        db.delete(DatabaseReaderContract.DatabaseEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Determines the favorite status of a song:
     * See Dictionary for values of favorite status
     *
     * @param title - name of song to search for
     * @return value of favorite status
     */
    public int getFavoriteStatus(String title) {

        Log.d(TAG, "The Favorite Status of a song");
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE, DatabaseReaderContract.DatabaseEntry.COLUMN_FAVORITE};

        String selection = DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " = ?";
        String[] selectionArgs = {title};

        Cursor cursor = db.query(DatabaseReaderContract.DatabaseEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        if (cursor.getCount() <= 0) {
            return -1;
        }
        cursor.moveToNext();
        int status = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseReaderContract.DatabaseEntry.COLUMN_FAVORITE));
        cursor.close();

        return status;
    }

    /**
     * Gets the location of where the song was played last
     *
     * @param title - name of the song to search for
     * @return the location the song was last played
     */
    public List<String> getLocations(String title) {

        Log.d(TAG, "The Location of where the song was played last");
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE, DatabaseReaderContract.DatabaseEntry.COLUMN_LOCATION};

        String selection = DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " = ?";
        String[] selectionArgs = {title};

        Cursor cursor = db.query(DatabaseReaderContract.DatabaseEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        cursor.moveToNext();
        String location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseReaderContract.DatabaseEntry.COLUMN_LOCATION));
        cursor.close();

        return StringArrayParser.toArray(location);
    }

    /**
     * Gets the general time of when the song was played last
     * Possible values: Unknown, Morning, Afternoon, Evening
     *
     * @param title - name of the song to search for
     * @return the general time when the song was played last
     */
    public List<String> getGeneralTimes(String title) {

        Log.d(TAG, "The general Time of when the song was played last");
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE, DatabaseReaderContract.DatabaseEntry.COLUMN_GENERAL_TIME};

        String selection = DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " = ?";
        String[] selectionArgs = {title};

        Cursor cursor = db.query(DatabaseReaderContract.DatabaseEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        cursor.moveToNext();
        String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseReaderContract.DatabaseEntry.COLUMN_GENERAL_TIME));
        cursor.close();

        return StringArrayParser.toArray(time);
    }

    /**
     * Gets the exact time of when the song was played last
     *
     * @param title - name of the song to search for
     * @return the exact time when the song was played last
     */
    public List<String> getExactTimes(String title) {

        Log.d(TAG, "The exact Time of when the song was played last");
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE, DatabaseReaderContract.DatabaseEntry.COLUMN_EXACT_TIME};

        String selection = DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " = ?";
        String[] selectionArgs = {title};

        Cursor cursor = db.query(DatabaseReaderContract.DatabaseEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        cursor.moveToNext();
        String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseReaderContract.DatabaseEntry.COLUMN_EXACT_TIME));
        cursor.close();

        return StringArrayParser.toArray(time);
    }

    /**
     * Sets the favorite status of a song
     *
     * @param title    - name of song to update
     * @param favorite - See Dictionary for values
     */
    public void setFavoriteStatus(String title, int favorite) {
        SQLiteDatabase db = this.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DatabaseReaderContract.DatabaseEntry.COLUMN_FAVORITE, favorite);

        // Which row to update, based on the title
        String selection = DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " LIKE ?";
        String[] selectionArgs = {title};

        db.update(
                DatabaseReaderContract.DatabaseEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    /**
     * Sets the general time of a song
     *
     * @param title - name of song to update
     * @param time  - Either afternoon, morning, or evening
     */
    public void addGeneralTime(String title, String time) {

        Log.d(TAG, "Add the general time of a song");
        SQLiteDatabase db = this.getWritableDatabase();

        List<String> times = getGeneralTimes(title);
        if (!times.contains(time)) {
            times.add(time);
        }

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DatabaseReaderContract.DatabaseEntry.COLUMN_GENERAL_TIME, StringArrayParser.toString(times));

        // Which row to update, based on the title
        String selection = DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " LIKE ?";
        String[] selectionArgs = {title};

        db.update(
                DatabaseReaderContract.DatabaseEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    /**
     * Sets the exact time of a song
     *
     * @param title - name of song to update
     * @param time  - DateTime value from android
     */
    public void addExactTime(String title, String time) {

        Log.d(TAG, "Add the exact time of a song");
        SQLiteDatabase db = this.getWritableDatabase();

        List<String> times = getExactTimes(title);
        if (!times.contains(time)) {
            times.add(time);
        }

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DatabaseReaderContract.DatabaseEntry.COLUMN_EXACT_TIME, StringArrayParser.toString(times));

        // Which row to update, based on the title
        String selection = DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " LIKE ?";
        String[] selectionArgs = {title};

        db.update(
                DatabaseReaderContract.DatabaseEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    /**
     * Adds a location to the song's list
     *
     * @param title    - name of song to update
     * @param location - location name to add
     */
    public void addLocation(String title, String location) {

        Log.d(TAG, "Add a location to the song's List");
        SQLiteDatabase db = this.getWritableDatabase();

        List<String> locations = getLocations(title);
        if (!locations.contains(location)) {
            locations.add(location);
        }

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DatabaseReaderContract.DatabaseEntry.COLUMN_LOCATION, StringArrayParser.toString(locations));

        // Which row to update, based on the title
        String selection = DatabaseReaderContract.DatabaseEntry.COLUMN_TITLE + " LIKE ?";
        String[] selectionArgs = {title};

        db.update(
                DatabaseReaderContract.DatabaseEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
            Log.d(TAG, "Upgrade to the new version");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        Log.d(TAG, "Downgrade to the old version");

    }

    public void update(Song song) {
        addLocation(song.getTitle(), song.getMostRecentLocation());
        addGeneralTime(song.getTitle(), song.getMostRecentTime());
    }

    @Override
    public void load(Song song) {

        // add to db if it isn't in the db
        if (!exists(song.getTitle())) {
            add(song);
        }

    }

    @Override
    public void getVibeSongs(String time, String location, PlaylistLoadedListener listener, User user) {

    }

}
