package com.cse110.ucsd.flashbackmusicproject.playlist.decorators;

import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 3/8/18.
 */

public class LikedPlaylistDecorator extends APlaylistDecorator {

    public LikedPlaylistDecorator(IPlaylist playlist){
        this.playlist = playlist;
    }
    @Override
    List<Song> filter(List<Song> songs) {
        List<Song> filteredList = new ArrayList<Song>();

        for(Song song : songs){
            if(song.isLiked()){
                filteredList.add(song);
            }
        }

        return filteredList;
    }
}
