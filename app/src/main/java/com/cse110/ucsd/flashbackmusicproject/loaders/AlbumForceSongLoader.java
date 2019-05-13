package com.cse110.ucsd.flashbackmusicproject.loaders;

import com.cse110.ucsd.flashbackmusicproject.song.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 2/18/18.
 */

public class AlbumForceSongLoader implements Loader{

    private String album;
    private String songToForce;
    private List<Song> allSongs;

    public AlbumForceSongLoader(List<Song> allSongs, String album, String songToForce) {
        this.allSongs = allSongs;
        this.album = album;
        this.songToForce = songToForce;
    }

    public ArrayList<Song> getPlaylist() {

        ArrayList<Song> songs = new ArrayList<>();

        for (Song song : allSongs) {

            if (song.getAlbum().equals(album) && (!song.isDisliked() || song.getTitle().equals(songToForce))) {
                songs.add(song);
            }

        }

        return songs;
    }
}
