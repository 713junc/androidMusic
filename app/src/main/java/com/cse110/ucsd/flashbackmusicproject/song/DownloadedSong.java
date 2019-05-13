package com.cse110.ucsd.flashbackmusicproject.song;

import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.MyMetaDataExtractor;
import com.cse110.ucsd.flashbackmusicproject.mediaplayer.DownloadStrategy;
import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;
import com.cse110.ucsd.flashbackmusicproject.utility.DownloadedSongsUtility;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a song that has been downloaded, relies on file path for info
 * and playing.
 */

public class DownloadedSong extends Song{

    public DownloadedSong(String localFilePath, String url) {
        this.songPath = localFilePath;
        this.url = url;

        locations = new ArrayList<>();
        generalTimes = new ArrayList<>();
        friends = new ArrayList<>();
        pseudos = new ArrayList<>();

        this.loadStrategy = new DownloadStrategy();
        if(title == null) {
            String[] songComp = localFilePath.split("/");
            String songName = songComp[songComp.length - 1].substring(0,
                    songComp[songComp.length - 1].length() - 4);
            title = songName;
        }
        title = validateTitle(title);

        artist = MyMetaDataExtractor.getSongArtist(localFilePath);
        if (artist == null) {
            artist = "Unknown";
        }

        album = MyMetaDataExtractor.getSongAlbum(localFilePath);
        if(album == null) {
            album = "GenericAlbum";
        }

        // extract album art
        this.image = MyMetaDataExtractor.getSongArt(localFilePath);
        if (this.image == null) {
            Log.d(TAG, "Image is null");
            this.image = DownloadedSongsUtility.getDefaultBitMap();
        }

        favStatus = Dictionary.NEUTRAL;
    }

    public DownloadedSong(DataSnapshot dataSnapshot){
        String title = (String) dataSnapshot.child("title").getValue();
        String artist = (String) dataSnapshot.child("artist").getValue();
        String album = (String) dataSnapshot.child("album").getValue();
        String url = (String) dataSnapshot.child("url").getValue();
        long favoriteStatus = Dictionary.NEUTRAL;

        if(dataSnapshot.child("favStatus").getValue() != null){
            favoriteStatus = (Long) dataSnapshot.child("favStatus").getValue();
        }

        this.loadStrategy = new DownloadStrategy();

        this.title = title;
        this.artist = artist;
        this.album = album;
        this.url = url;
        this.favStatus = (int) favoriteStatus;
        this.image = DownloadedSongsUtility.getDefaultBitMap();
        this.loadStrategy = new DownloadStrategy();

        locations = new ArrayList<>();
        generalTimes = new ArrayList<>();

        friends = new ArrayList<>();
        pseudos = new ArrayList<>();

        DataSnapshot generalTimesSnapshot = dataSnapshot.child("generalTimes");

        if(generalTimesSnapshot != null && generalTimesSnapshot.getValue() != null){
            generalTimes = (List) generalTimesSnapshot.getValue();
        }

        DataSnapshot locationsSnapshot = dataSnapshot.child("locations");

        if(locationsSnapshot != null && locationsSnapshot.getValue() != null){
            locations = (List) locationsSnapshot.getValue();
        }

        if (!generalTimes.isEmpty()) {
            setMostRecentTime(generalTimes.get(0));
        } else {
            setMostRecentTime("Unknown");
        }

        if (!locations.isEmpty()) {
            setMostRecentLocation(locations.get(0));
        } else {
            setMostRecentLocation("Unknown");
        }

    }
}
