package com.cse110.ucsd.flashbackmusicproject.playlist.decorators;

import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 3/8/18.
 */

public class TimePlaylistDecorator extends APlaylistDecorator {
    String time;

    public TimePlaylistDecorator(IPlaylist playlist, String time){
        this.playlist = playlist;
        this.time = time;
    }

    @Override
    List<Song> filter(List<Song> songs) {
        List<Song> filteredList = new ArrayList<Song>();

        for(Song song : songs){
            if(song.getGeneralTimes().contains(time)){
                filteredList.add(song);
            }
        }

        return filteredList;
    }
}
