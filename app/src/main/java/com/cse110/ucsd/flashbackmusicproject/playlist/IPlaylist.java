package com.cse110.ucsd.flashbackmusicproject.playlist;

import com.cse110.ucsd.flashbackmusicproject.song.Song;

import java.util.List;

/**
 * Created by trevor on 3/8/18.
 */

public interface IPlaylist{
    List<Song> getPlaylist();
    void addSong(Song song);
    int size();
    Song get(int index);
    boolean isEmpty();
    boolean contains(Song song);
}
