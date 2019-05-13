package com.cse110.ucsd.flashbackmusicproject.playlist.decorators;

import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 3/8/18.
 */

public class ArtistPlaylistDecorator extends APlaylistDecorator {
    String artist;

    public ArtistPlaylistDecorator(IPlaylist playlist, String artist){
        this.playlist = playlist;
        this.artist = artist;
    }

    @Override
    List<Song> filter(List<Song> songs) {
        List<Song> filteredList = new ArrayList<Song>();

        for(Song song : songs){
            if(song.getArtist().equals(artist)){
                filteredList.add(song);
            }
        }

        return filteredList;
    }
}
