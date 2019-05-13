package com.cse110.ucsd.flashbackmusicproject.loaders;

import android.content.Context;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.song.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The class provides a list of songs based on their likes.
 */

public class FlashbackLoader implements Loader {

    private Context c;
    private String location, time;
    List<String> players;
    private FlashbackComparator flashbackComparator;
    List<Song> allSongs;

    public static final String TAG = "FlashbackLoader";

    public FlashbackLoader(List<Song> allSongs, String location, String generalTime) {
        this.allSongs = allSongs;
        this.time = generalTime;
        this.location = location;
        this.players = players;
        flashbackComparator = new FlashbackComparator(location, generalTime);

    }

    public List<Song> getPlaylist() {
        PriorityQueue<Song> songs = new PriorityQueue<>(flashbackComparator);

        for (Song song : allSongs) {
            //Log.d(TAG, song.getTitle());
            //Log.d(TAG, song.getGeneralTimes().toString());
            //Log.d(TAG, song.getGeneralTimes().contains(time) + "");
            //Log.d(TAG, !song.isDisliked() + "");
            if (!song.isDisliked() && (song.getLocations().contains(location) || song.getGeneralTimes().contains(time)) ) {
                songs.add(song);
                Log.d(TAG, "Adding " + song.getTitle());
            }
        }


        if(songs.size() == 0){
            Log.d(TAG, "No songs for flashback match date/time . Looking for any played song");
            for(Song song : allSongs){
                if(song.hasBeenPlayed() && !song.isDisliked()){
                    songs.add(song);
                }
            }
        }

        List toReturn = new ArrayList<Song>();

        while(!songs.isEmpty()){
            Song song = songs.poll();
            toReturn.add(song);
        }

        return toReturn;
    }



}
