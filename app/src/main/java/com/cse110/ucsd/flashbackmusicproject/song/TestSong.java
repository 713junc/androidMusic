package com.cse110.ucsd.flashbackmusicproject.song;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.MyMetaDataExtractor;
import com.cse110.ucsd.flashbackmusicproject.mediaplayer.TestStrategy;

import java.util.ArrayList;

/**
 * Created by trevor on 3/9/18.
 */

public class TestSong extends Song {

    public TestSong(String title, String artist, String album, int favStatus){
        this.title = validateTitle(title);
        this.artist = artist;
        this.album = album;
        this.favStatus = favStatus;

        this.locations = new ArrayList<>();
        this.generalTimes = new ArrayList<>();

        this.loadStrategy = new TestStrategy();
        timeLastPlayed = 0;
    }

    public void addLocation(String location){
        this.locations.add(location);
    }

    public void addTime(String time){
        this.generalTimes.add(time);
    }
}
