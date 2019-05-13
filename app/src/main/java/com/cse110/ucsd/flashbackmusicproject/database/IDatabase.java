package com.cse110.ucsd.flashbackmusicproject.database;

import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.user.User;

import java.util.List;

/**
 * Created by Derek on 3/5/2018.
 */

public interface IDatabase {

    /**
     * Adds an entry to the database table with all columns empty
     * @param song - name of the song to add
     */
    public void add(Song song);

    /**
     * Deletes a song key from the database
     * @param title - name of the song to delete
     */
    public void delete(String title);

    /**
     * Determines the favorite status of a song:
     * See Dictionary for values of favorite status
     * @param title - name of song to search for
     * @return value of favorite status
     */
    public int getFavoriteStatus(String title);

    /**
     * Gets the location of where the song was played last
     * @param title - name of the song to search for
     * @return the location the song was last played
     */
    public List<String> getLocations(String title);

    /**
     * Gets the general time of when the song was played last
     * Possible values: Unknown, Morning, Afternoon, Evening
     * @param title - name of the song to search for
     * @return the general time when the song was played last
     */
    public List<String> getGeneralTimes(String title);

    /**
     * Sets the favorite status of a song
     * @param title - name of song to update
     * @param favorite - See Dictionary for values
     */
    public void setFavoriteStatus(String title, int favorite);

    /**
     * Update song data in the database
     * @param song - song to update
     */
    public void update(Song song);

    /**
     * Loads location and time data from database into song
     * @param song - song to load
     */
    public void load(Song song);

    public void getVibeSongs(String time, String location, PlaylistLoadedListener listener, User user);

}
