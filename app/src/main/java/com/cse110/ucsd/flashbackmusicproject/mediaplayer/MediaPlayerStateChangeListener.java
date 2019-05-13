package com.cse110.ucsd.flashbackmusicproject.mediaplayer;

import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.song.Song;

/**
 * Listener interface to keep track of song states.
 */

public interface MediaPlayerStateChangeListener {

    public void onSongChanged(Song newSong);
    public void onSongComplete(Song song);
    public void onPlaylistChanged(IPlaylist playlist);
}
