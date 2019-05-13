package com.cse110.ucsd.flashbackmusicproject.mediaplayer;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.MainActivity;
import com.cse110.ucsd.flashbackmusicproject.location.ILocationProvider;
import com.cse110.ucsd.flashbackmusicproject.location.RealLocationProvider;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.playlist.Playlist;
import com.cse110.ucsd.flashbackmusicproject.playlist.PlaylistFactory;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.timer.RealTimeKeeper;
import com.cse110.ucsd.flashbackmusicproject.timer.TimeKeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * MyMediaPlayer represents an actual MediaPlayer. It is used to load songs, change songs,
 * pause, play, etc.
 */

public class MyMediaPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
                                      MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener{

    private static final int ACTION_LOAD_NEXT = 1;
    private static final int ACTION_LOAD_PREV = 2;

    private MainActivity context;
    private MediaPlayer mediaPlayer;
    IPlaylist playlist;
    private boolean mediaIsLoaded;
    private int index;
    String TAG = "MyMediaPlayer";
    private int lastAction;
    TimeKeeper timeKeeper;
    ILocationProvider locationProvider;
    private List<MediaPlayerStateChangeListener> stateChangeListeners;
    private boolean isRepeating;

    private static final boolean DEBUG = false;

    public MyMediaPlayer(MainActivity context){
        this.context = context;
        playlist = new Playlist();
        setupMediaPlayer();
        index = 0;
        mediaIsLoaded = false;
        lastAction = -1;
        timeKeeper = new RealTimeKeeper();
        locationProvider = new RealLocationProvider(context);
        stateChangeListeners = new ArrayList<MediaPlayerStateChangeListener>();

        Log.d(TAG, "Raw loader");
        updatePlaylist(PlaylistFactory.getEmptyPlaylist());

    }

    public MyMediaPlayer(MainActivity context, TimeKeeper timeKeeper){
        this(context);
        this.timeKeeper = timeKeeper;
    }

    private void setupMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public boolean playlistIsEmpty(){
        return playlist.isEmpty();
    }

    private void loadMedia(Song song) {
        mediaIsLoaded = false;
        Log.d(TAG, playlist.get(index).getTitle());
        playlist.get(index).getLoadStrategy().loadSong(mediaPlayer, context, playlist.get(index));
    }

    private void incrementIndex(){
        Log.d(TAG, "Incrementing index");
        if(playlistIsEmpty()){
            Log.d(TAG, "Playlist is empty");
            return;
        }
        index = (index + 1) % playlist.size();
    }

    private void decrementIndex(){
        Log.d(TAG, "decrementing index");
        if(playlistIsEmpty()){
            Log.d(TAG, "Playlist is empty");
            return;
        }
        index = index - 1;
        if(index < 0){
            index = playlist.size() - 1;
        }
    }

    private void loadNextSong(){
        lastAction = ACTION_LOAD_NEXT;
        if (!isRepeating) {
            incrementIndex();
        }
        mediaPlayer.reset();
        loadMedia(playlist.get(index));
    }

    private void loadPrevSong(){
        lastAction = ACTION_LOAD_PREV;
        decrementIndex();
        mediaPlayer.reset();
        loadMedia(playlist.get(index));
    }

    public void play(){
        if(playlistIsEmpty()){
            Log.v(TAG, "Playlist is empty. Nothing to play");
            return;
        }
        if(!mediaIsLoaded){
            Log.d(TAG, "Media is not loaded. Loading current song");
            loadMedia(playlist.get(index));
        }else if(!mediaPlayer.isPlaying()){
            Log.d(TAG, "Playing song at index " + index);
            mediaPlayer.start();

            if (DEBUG) {
                onCompletion(mediaPlayer);
            }
        }
    }

    public void pause(){
        Log.d(TAG, "Pausing Music");
        if(mediaIsLoaded && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    public void next(){
        if(playlistIsEmpty()){
            return;
        }

        loadNextSong();
    }

    public void prev(){
        if(playlistIsEmpty()){
            return;
        }
        loadPrevSong();
    }

    public void toggleRepeat() {
        isRepeating = !isRepeating;
    }

    public void seekTo(int position) {
        if (mediaIsLoaded) {
            mediaPlayer.seekTo(position);
        }
    }

    public void restart(){
        Log.d(TAG, "Restarting Music");
        if(mediaIsLoaded){
            mediaPlayer.seekTo(0);
        }
    }

    public int getCurrentPosition(){
        Log.d(TAG, "get CurrentPosition");
        return mediaPlayer.getCurrentPosition();
    }

    public int getSongDuration(){
        Log.d(TAG, "get Duration of a Song");
        return mediaPlayer.getDuration();
    }

    public String getSongTitle(){
        return playlist.get(index).getTitle();
    }

    public String getNextSongTitle(){
        Log.d(TAG, "get Next Song Title");
        if(index == playlist.size()-1)
            return playlist.get( 0 ).getTitle();
        return playlist.get(index+1).getTitle();
    }
    public String getPlayer() {
        return getCurrentSong().getPlayer();
    }

    public String getSongArtist(){
        return playlist.get(index).getArtist();
    }

    public String getSongAlbum(){
        return playlist.get(index).getAlbum();
    }

    public Bitmap getSongArt(){
        return playlist.get(index).getArt();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public int getSongFavoriteStatus(){
        Log.d(TAG, "Favorite Status: " + getCurrentSong().getFavoriteStatus());
        return getCurrentSong().getFavoriteStatus();
    }

    public String getSongLocation() {
        Log.d(TAG, "Song Location: " + playlist.get(index).getMostRecentLocation());
        return playlist.get(index).getMostRecentLocation();
    }

    public String getSongTime() {
        Log.d(TAG, "Song Time: " + playlist.get(index).getMostRecentTime());
        return playlist.get(index).getMostRecentTime();
    }

    public void setSongFavoriteStatus(int favoriteStatus){
        getCurrentSong().setFavoriteStatus(favoriteStatus);
    }

    public void release(){
        Log.d(TAG, "Releasing MediaPlayer");
        mediaPlayer.release();
    }


    public void updatePlaylist(IPlaylist playlist){
        Log.d(TAG, "Updating playlist");
        Log.v(TAG, playlist.getPlaylist().toString());
        mediaPlayer.reset();
        this.playlist = playlist;
        index = 0;
        mediaIsLoaded = false;
        notifyListenerOfSongChange();
        notifyListenerOfPlaylistChange(playlist);
    }

    public void skipToTrack(String songTitle){
        for(int i = 0; i < playlist.size(); i++){
            Song song = playlist.get(i);
            if(song.getTitle().equals(songTitle)){
                index = i;
                decrementIndex();
                loadNextSong();
                break;
            }
        }
    }

    private Song getCurrentSong(){
        if(playlistIsEmpty()){
            return null;
        }
        return playlist.get(index);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG, "Song completed");

        Song song = getCurrentSong();
        String newLocation = locationProvider.getLocation();
        String newTime = timeKeeper.getGeneralTime();

        if(newTime != null && !newTime.toLowerCase().equals("unknown")){
            song.addTime(newTime);
        }

        if(newLocation != null && !newLocation.toLowerCase().equals("unknown")){
            song.addLocation(newLocation);
        }

        notifyListenerOfSongComplete(song);

        if (!DEBUG) {
            loadNextSong();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "Mediaplayer Prepared");
        mediaIsLoaded = true;
        notifyListenerOfSongChange();
        play();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        Log.d(TAG, "Seek complete");
        play();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

        Log.d(TAG, "Error while playing song: " + i + " " + i1);

        if(i == MediaPlayer.MEDIA_ERROR_UNKNOWN && i1 == -2147483648 /*MEDIA_ERROR_SYSTEM*/){
            switch (lastAction){
                case ACTION_LOAD_NEXT:
                    Log.d(TAG, "switch music to next song");
                    loadNextSong();
                    return true;

                case ACTION_LOAD_PREV:
                    Log.d(TAG, "switch music to previous song");
                    loadPrevSong();
                    return true;

            }
        }
        return false;
    }

    public void addStateChangeListener(MediaPlayerStateChangeListener newStateChangeListener){
        stateChangeListeners.add(newStateChangeListener);
    }

    private void notifyListenerOfSongChange(){
        for(MediaPlayerStateChangeListener stateChangeListener : stateChangeListeners){
            stateChangeListener.onSongChanged(getCurrentSong());
        }
    }

    private void notifyListenerOfSongComplete(Song song){
        for(MediaPlayerStateChangeListener stateChangeListener : stateChangeListeners){
            stateChangeListener.onSongComplete(song);
        }
    }

    private void notifyListenerOfPlaylistChange(IPlaylist newPlaylist) {
        for(MediaPlayerStateChangeListener stateChangeListener : stateChangeListeners){
            stateChangeListener.onPlaylistChanged(newPlaylist);
        }
    }
}
