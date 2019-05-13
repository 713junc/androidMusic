package com.cse110.ucsd.flashbackmusicproject.playlist.decorators;

import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;

import java.util.List;

/**
 * Created by trevor on 3/8/18.
 */

public abstract class APlaylistDecorator implements IPlaylist {
    IPlaylist playlist;

    abstract List<Song> filter(List<Song> songs);

    @Override
    public List<Song> getPlaylist(){
        List<Song> filtered = filter(playlist.getPlaylist());

        return filtered;
    }

    @Override
    public void addSong(Song song) {
        playlist.addSong(song);
    }

    @Override
    public int size() {
        return getPlaylist().size();
    }

    @Override
    public Song get(int index) {
        return getPlaylist().get(index);
    }

    @Override
    public boolean isEmpty() {
        return getPlaylist().size() == 0;
    }

    @Override
    public boolean contains(Song song) {
        return getPlaylist().contains(song);
    }
}
