package com.cse110.ucsd.flashbackmusicproject.song;

import android.graphics.Bitmap;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.MyMetaDataExtractor;
import com.cse110.ucsd.flashbackmusicproject.mediaplayer.ILoadStrategy;
import com.cse110.ucsd.flashbackmusicproject.timer.RealTimeKeeper;
import com.cse110.ucsd.flashbackmusicproject.timer.TimeKeeper;
import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * A song must be loaded through the resource ID assigned by android at compile time.
 * The metadata of a song consists of the title, artist, album, and art. In addition,
 * a song also tracks whether or not it is liked by the user, and the last known locations it
 * was played at the last known time
 */
public abstract class Song{

    protected String title, artist, album, url;

    long timeLastPlayed;

    private boolean isDownloading;

    protected int favStatus;

    // the resource ID of the song assigned from android
    @Exclude
    protected int resource;

    @Exclude
    protected ILoadStrategy loadStrategy;

    @Exclude
    protected String songPath;

    // the values matched by the flashback algorithm

    long mostRecentlyPlayedInMillis;
    private String mostRecentLocation, mostRecentTime;

    // locations and general time of when song was last played
    protected List<String> locations, generalTimes, friends, pseudos;
    // the album art loaded from metadata
    @Exclude
    protected Bitmap image;

    @Exclude
    public static final String TAG = "Song";

    /**
     * Sets the exact time and general time from android's datetime
     * @param generalTime - the date and time to set
     */
    public void addTime(String generalTime) {
        if(!this.generalTimes.contains(generalTime)){
            this.generalTimes.add(generalTime);
        }

        mostRecentTime = generalTime;
    }

    public void setMostRecentlyPlayedTime( long lastlyPlayedInMillis ) {
        mostRecentlyPlayedInMillis = lastlyPlayedInMillis;
    }
    public int getMostRecentlyPlayedInDays() {
        TimeKeeper timeKeeper;
        timeKeeper = new RealTimeKeeper();
        return timeKeeper.getMostRecentlyPlayedInDays( mostRecentlyPlayedInMillis );
    }

    @Exclude
    public void addLocation(String location) {
        Log.d(TAG, "Adding " + location + " to " + title);
        if(!this.locations.contains(location)){
            this.locations.add(location);
        }
        mostRecentLocation = location;
    }
    public List<String> getLocations() {return locations;}

    public List<String> getGeneralTimes() {return generalTimes;}

    public List<String> getListOfFriends() { return friends; }
    @Exclude
    public int getFavoriteStatus() { return favStatus;}

    @Exclude
    public int getResource() {
        return resource;
    }

    public String getArtist(){
        return artist.replace("_", " ") ;
    }

    public String getAlbum(){
        return album.replace("_", " ") ;
    }

    public String getTitle(){
        return title.replace("_", " ") ;
    }

    @Exclude
    public Bitmap getArt() {return image; }

    @Exclude
    public void setFavoriteStatus(int favoriteStatus) {
        this.favStatus = favoriteStatus;
    }

    @Exclude
    public boolean isDisliked(){
        return favStatus == Dictionary.DISLIKED;
    }

    @Exclude
    public boolean isLiked(){
        return favStatus == Dictionary.LIKED;
    }

    @Exclude
    public String getMostRecentLocation() {return mostRecentLocation;}

    @Exclude
    public String getMostRecentTime() {return mostRecentTime;}

    @Exclude
    public int getFlashbackScore(String currentLocation ){

        int score = 0;

        if( locations.contains( currentLocation )){
            score++;
        }
        if( getMostRecentlyPlayedInDays() <= 7 ){
            score++;
        }

        if(friends != null && friends.size() > 0){
            score++;
        }
        Log.d("SongClass", getTitle() + ": " + score);
        return score;
    }

    public boolean hasBeenPlayed(){
        return getLocations().size() > 0 || getGeneralTimes().size() > 0;
    }

    public void setLocations(List locations) {
        this.locations = locations;
    }

    public void setGeneralTimes(List times) {
        this.generalTimes = times;
    }

    public void setListOfFriends( List friends ) { this.friends = friends; }

    public void setMostRecentLocation(String mostRecentLocation) {
        this.mostRecentLocation = mostRecentLocation;
    }

    public void setMostRecentTime(String mostRecentTime) {
        this.mostRecentTime = mostRecentTime;
    }

    // This is needed for DownloadSongsActivity
    @Override
    public String toString(){
        return getTitle() + " - " + getArtist();
    }

    @Exclude
    public void onLoadComplete(List<String> locations, List<String> generalTimes, int favoriteStatus) {

        this.locations = locations;
        this.generalTimes = generalTimes;
        this.favStatus = favoriteStatus;

        if (this.generalTimes.size() > 0) {
            mostRecentTime = this.generalTimes.get(0);
        } else {
            mostRecentTime = "Unknown";
        }

        if (this.locations.size() > 0) {
            mostRecentLocation = this.locations.get(0);
        } else {
            mostRecentLocation = "Unknown";
        }
    }

    @Exclude
    public String getSongPath() {
        return songPath;
    }

    @Exclude
    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    @Exclude
    public ILoadStrategy getLoadStrategy() {
        return loadStrategy;
    }

    @Exclude
    public String getURL() {
        return url;
    }

    @Exclude
    public void setURL() {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Song)){
            return false;
        }

        Song song = (Song) obj;
        return (title + artist + album).equals(song.getTitle() + song.getArtist() + song.getAlbum());
    }

    @Exclude
    public List<String> getPseudos() { return pseudos; }

    @Exclude
    public void addFriend( String nameOfFriend ) { friends.add( nameOfFriend ); }

    @Exclude
    public void addPseudo( String pseduo ) { this.pseudos.add( pseduo ); }

    @Exclude
    public boolean isPlayedByFriend() { return !friends.isEmpty(); }

    @Exclude
    public boolean isPlayedByPseudo() { return !pseudos.isEmpty(); }

    @Exclude
    public void setFriends(List<String> friends){
        this.friends = friends;
    }

    @Exclude
    public void setPseudos(List<String> pseudos) {
        this.pseudos = pseudos;
    }

    public String validateTitle(String title) {
        String regx =".#$[]";
        char[] regArr = regx.toCharArray();
        for(char reg : regArr) {
            title = title.replace("" + reg, "");
        }
        return title;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

    public String getPlayer() {
        if (friends.size() > 0) {
            return friends.get(0);
        } else if (pseudos.size() > 0) {
            return pseudos.get(0);
        } else {
            return "you";
        }
    }
    public void extractArt() {
        if (songPath != null) {
            Bitmap map = MyMetaDataExtractor.getSongArt(songPath);
            if (map != null) {
                this.image = map;
            }
        }
    }
}
