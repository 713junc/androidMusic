package com.cse110.ucsd.flashbackmusicproject.loaders;

import com.cse110.ucsd.flashbackmusicproject.song.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Another implementation of Loader to just return the normal list (without any sorting).
 */
public class NullLoader implements Loader {
    public NullLoader() {
    }

    public List<Song> getPlaylist() {
        return new ArrayList<Song>();
    }
}
