package com.cse110.ucsd.flashbackmusicproject.playlist;

import com.cse110.ucsd.flashbackmusicproject.song.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 3/8/18.
 */

public class Playlist implements IPlaylist {
    List<Song> songs;

    public Playlist(){
        songs = new ArrayList<Song>();
    }

    public Playlist(List<Song> songs){
        this.songs = songs;
    }

    public void addSong(Song song){
        songs.add(song);
    }

    @Override
    public int size() {
        return songs.size();
    }

    @Override
    public Song get(int index) {
        return songs.get(index);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Song song) {
        return getPlaylist().contains(song);
    }

    @Override
    public List<Song> getPlaylist() {
        return songs;
    }

    public void set( int index, Song song ) {
        songs.set( index, song);
    }
}
