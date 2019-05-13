package com.cse110.ucsd.flashbackmusicproject.loaders;

import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.song.Song;

import java.util.Comparator;
import java.util.List;

/**
 * The class provides a compare method that compares two songs based on their flashback
 * scores.
 */

public class FlashbackComparator implements Comparator {
    String generalLocation, generalTime;
    List<String> players;


    public FlashbackComparator(String generalLocation, String generalTime){
        this.generalLocation = generalLocation;
        this.generalTime = generalTime;
        this.players = players;
    }

    @Override
    public int compare(Object o1, Object o2) {
        Song song1 = (Song) o1;
        Song song2 = (Song) o2;

        int song1Score = song1.getFlashbackScore( generalLocation );
        int song2Score =  song2.getFlashbackScore( generalLocation );

        Log.v("taggy", song1.getTitle() + " " + song1Score);
        Log.v("taggy", song2.getTitle() + " " + song2Score);

        return song2Score - song1Score;
    }
}
