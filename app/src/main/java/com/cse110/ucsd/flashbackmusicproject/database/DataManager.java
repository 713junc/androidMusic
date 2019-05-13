package com.cse110.ucsd.flashbackmusicproject.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.cse110.ucsd.flashbackmusicproject.MainActivity;
import com.cse110.ucsd.flashbackmusicproject.loaders.FlashbackComparator;
import com.cse110.ucsd.flashbackmusicproject.location.ILocationProvider;
import com.cse110.ucsd.flashbackmusicproject.location.RealLocationProvider;
import com.cse110.ucsd.flashbackmusicproject.mediaplayer.ModeListener;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.timer.RealTimeKeeper;
import com.cse110.ucsd.flashbackmusicproject.timer.TimeKeeper;
import com.cse110.ucsd.flashbackmusicproject.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.cse110.ucsd.flashbackmusicproject.MainActivity.SHAREDPREF_NAME;

/**
 * Created by gSong on 2018-02-21.
 */

public class DataManager implements ModeListener{
    
    private IDatabase database;

    private static final String TAG = "DataManager";

    private List<PlaylistLoadedListener> playlistLoadedListeners = new ArrayList<>();
    private List<Comparator<Song>> sortingComparators = new ArrayList<>();

    TimeKeeper timeKeeper;
    ILocationProvider locationProvider;

    private User user;

    Context context;
    // name of the key in shared pref
    private static final String IS_FLASH_SHARED_PREF_KEY = "isFlash";
    public DataManager( User user, Context c ) {

        this.context = c;
        database = new MyFirebaseDatabase(user);
        this.user = user;

        playlistLoadedListeners = new ArrayList<>();

        sortingComparators.add(new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getTitle().compareTo(t1.getTitle());
            }
        });

        sortingComparators.add(new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getAlbum().compareTo(t1.getAlbum());
            }
        });

        sortingComparators.add(new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getArtist().compareTo(t1.getArtist());
            }
        });

        sortingComparators.add(new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return  song.getFavoriteStatus() - t1.getFavoriteStatus();
            }
        });
    }
    public DataManager(User user, MainActivity c, IUserLoader l) {

        this.context = c;
        database = new MyLocalDatabase(c, "LocalDatabase");
        this.user = user;

        timeKeeper = new RealTimeKeeper();
        locationProvider = new RealLocationProvider(c);

        playlistLoadedListeners = new ArrayList<>();
        sortingComparators = new ArrayList<>();

        sortingComparators.add(new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getTitle().compareTo(t1.getTitle());
            }
        });

        sortingComparators.add(new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getAlbum().compareTo(t1.getAlbum());
            }
        });

        sortingComparators.add(new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getArtist().compareTo(t1.getArtist());
            }
        });

        sortingComparators.add(new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return  song.getFavoriteStatus() - t1.getFavoriteStatus();
            }
        });
    }

    public void setFavoriteStatus(String title, int status) {
        database.setFavoriteStatus(title, status);
    }

    public int getFavoriteStatus(String title ) {
        return database.getFavoriteStatus( title );
    }


    public void load(Song song) {
        song.addTime(timeKeeper.getGeneralTime());
        song.addLocation(locationProvider.getLocation());
        database.load(song);
    }

    @Override
    public void setState(boolean isFlashMode) {

        SharedPreferences.Editor sharedpref =
                context.getSharedPreferences(SHAREDPREF_NAME,MODE_PRIVATE).edit();
        sharedpref.putBoolean(IS_FLASH_SHARED_PREF_KEY, isFlashMode);
        sharedpref.apply();
    }

    @Override
    public boolean getState() {
        SharedPreferences sharedpref =
                context.getSharedPreferences(SHAREDPREF_NAME,MODE_PRIVATE);
        return sharedpref.getBoolean(IS_FLASH_SHARED_PREF_KEY, false);

    }

    public void getVibeSongs(PlaylistLoadedListener listener){
        database.getVibeSongs(timeKeeper.getGeneralTime(), locationProvider.getLocation(), listener, user);
    }

    public IPlaylist sortSongsByTitle( IPlaylist playlist ) {
        IPlaylist newPlayList = playlist;
        Collections.sort( newPlayList.getPlaylist(), sortingComparators.get(0));
        return newPlayList;
    }
    public IPlaylist sortSongsByAlbum( IPlaylist playlist ) {
        IPlaylist newPlayList = playlist;
        Collections.sort( newPlayList.getPlaylist(), sortingComparators.get(1));
        return newPlayList;
    }
    public IPlaylist sortSongsByArtist( IPlaylist playlist ) {
        IPlaylist newPlayList = playlist;
        Collections.sort( newPlayList.getPlaylist(), sortingComparators.get(2));
        return newPlayList;
    }
    public IPlaylist sortSongsByFavoriteStatus( IPlaylist playlist ) {
        IPlaylist newPlayList = playlist;
        Collections.sort( newPlayList.getPlaylist(), sortingComparators.get(3));
        return newPlayList;
    }
    public IPlaylist sortByScore(IPlaylist playlist, final String currentLocation) {
        IPlaylist newPlayList = playlist;
        FlashbackComparator comparator = new FlashbackComparator( currentLocation, "time");
        Collections.sort(newPlayList.getPlaylist(), comparator);
        return newPlayList;
    }

    public void updateSongInfo(Song song){
        database.update(song);
    }

    public void addPlaylistLoadListener(PlaylistLoadedListener playlistLoadedListener){
        playlistLoadedListeners.add(playlistLoadedListener);
    }

}
