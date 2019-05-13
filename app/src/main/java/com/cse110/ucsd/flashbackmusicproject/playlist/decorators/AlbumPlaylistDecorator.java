package com.cse110.ucsd.flashbackmusicproject.playlist.decorators;

import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 3/9/18.
 */

public class AlbumPlaylistDecorator extends APlaylistDecorator {
    String album;

    public AlbumPlaylistDecorator(IPlaylist playlist, String album){
        this.playlist = playlist;
        this.album = album;
    }

    @Override
    List<Song> filter(List<Song> songs) {
        List<Song> filteredList = new ArrayList<Song>();

        for(Song song : songs){
            if(song.getAlbum().equals(album)){
                filteredList.add(song);
            }
        }

        return filteredList;
    }

    public String getAlbumTitle(){
        return album;
    }
}
