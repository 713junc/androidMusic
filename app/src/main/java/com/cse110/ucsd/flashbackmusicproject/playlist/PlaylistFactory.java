package com.cse110.ucsd.flashbackmusicproject.playlist;

import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.loaders.FlashbackComparator;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.AlbumPlaylistDecorator;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.LocationPlaylistDecorator;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.SingleSongPlaylistDecorator;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.TimePlaylistDecorator;

import java.util.Collections;
import java.util.List;

/**
 * Created by trevor on 3/8/18.
 */

public class PlaylistFactory {

    public static IPlaylist getEmptyPlaylist(){
        return new Playlist();
    }
    public static IPlaylist getAlbumPlaylist(List<Song> allSongs, String album){
        return new AlbumPlaylistDecorator(new Playlist(allSongs), album);
    }

    public static IPlaylist getSingleSongPlaylist(List<Song> allSongs, String songTitle){
        return new SingleSongPlaylistDecorator(new Playlist(allSongs), songTitle);
    }

    public static IPlaylist getVibePlaylist(List<Song> allSongs, String location, String time){

        IPlaylist playlist = new Playlist(allSongs);
        IPlaylist locationPlaylistDecorator = new LocationPlaylistDecorator(playlist, location);
        IPlaylist timePlaylistDecorator = new TimePlaylistDecorator(playlist, time);


        IPlaylist vibeModePlaylist = new Playlist();

        for(Song song : locationPlaylistDecorator.getPlaylist()){
            vibeModePlaylist.addSong(song);
        }

        for(Song song : timePlaylistDecorator.getPlaylist()){
            if(!vibeModePlaylist.contains(song)){
                vibeModePlaylist.addSong(song);
            }
        }


        Log.v("taggy", vibeModePlaylist.getPlaylist().toString());
        Collections.sort(vibeModePlaylist.getPlaylist(), new FlashbackComparator(location, time));
        Log.v("taggy", vibeModePlaylist.getPlaylist().toString());

        return vibeModePlaylist;
    }


}
