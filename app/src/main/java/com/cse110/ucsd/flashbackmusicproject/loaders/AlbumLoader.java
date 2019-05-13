package com.cse110.ucsd.flashbackmusicproject.loaders;

import com.cse110.ucsd.flashbackmusicproject.song.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used to provide an arraylist of songs.
 */

public class AlbumLoader implements Loader {

    private String album;
    private List<Song> allSongs;

    public AlbumLoader(List<Song> allSongs, String album) {
        this.allSongs = allSongs;
        this.album = album;
    }

    public ArrayList<Song> getPlaylist() {

        ArrayList<Song> songs = new ArrayList<>();

        for (Song song : allSongs) {
            if (song.getAlbum().equals(album) && !song.isDisliked()) {
                songs.add(song);
            }

        }

        return songs;
    }
}
