package com.cse110.ucsd.flashbackmusicproject.loaders;

import com.cse110.ucsd.flashbackmusicproject.song.Song;

import java.util.List;

/**
 * Interface to return song list for playing.
 */

public interface Loader {

    public List<Song> getPlaylist();
}
