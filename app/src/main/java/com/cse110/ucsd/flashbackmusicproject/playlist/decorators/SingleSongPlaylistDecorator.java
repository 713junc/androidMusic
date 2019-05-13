package com.cse110.ucsd.flashbackmusicproject.playlist.decorators;

import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 3/9/18.
 */

public class SingleSongPlaylistDecorator extends APlaylistDecorator {
    String songTitle;

    public SingleSongPlaylistDecorator(IPlaylist playlist, String songTitle){
        this.playlist = playlist;
        this.songTitle = songTitle;
    }

    @Override
    List<Song> filter(List<Song> songs) {
        List<Song> filteredList = new ArrayList<Song>();

        for(Song song : songs){
            if(song.getTitle().equals(songTitle)){
                filteredList.add(song);
            }
        }

        return filteredList;
    }
}
