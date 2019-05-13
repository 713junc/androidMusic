package com.cse110.ucsd.flashbackmusicproject.database;

import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.song.Song;

import java.util.List;

/**
 * Created by trevor on 3/10/18.
 */

public interface PlaylistLoadedListener {
    public void onPlaylistLoaded(List<Song> songs, String location, String time);
}
