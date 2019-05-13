package com.cse110.ucsd.flashbackmusicproject.playlist.decorators;

import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 3/8/18.
 */

public class LocationPlaylistDecorator extends APlaylistDecorator {
    String location;

    public LocationPlaylistDecorator(IPlaylist playlist, String location){
        this.playlist = playlist;
        this.location = location;
    }

    @Override
    List<Song> filter(List<Song> songs) {
        List<Song> filteredList = new ArrayList<Song>();

        for(Song song : songs){
            if(song.getLocations().contains(location)){
                filteredList.add(song);
            }
        }

        return filteredList;
    }
}
