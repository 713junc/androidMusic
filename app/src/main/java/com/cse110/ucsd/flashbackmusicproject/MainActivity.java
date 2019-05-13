package com.cse110.ucsd.flashbackmusicproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.cse110.ucsd.flashbackmusicproject.database.DataManager;
import com.cse110.ucsd.flashbackmusicproject.database.IUserLoader;
import com.cse110.ucsd.flashbackmusicproject.database.PlaylistLoadedListener;
import com.cse110.ucsd.flashbackmusicproject.download.MusicDownloadManager;
import com.cse110.ucsd.flashbackmusicproject.download.SongDownloadListener;
import com.cse110.ucsd.flashbackmusicproject.mediaplayer.MediaPlayerStateChangeListener;
import com.cse110.ucsd.flashbackmusicproject.mediaplayer.MyMediaPlayer;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.playlist.PlaylistFactory;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.AlbumPlaylistDecorator;
import com.cse110.ucsd.flashbackmusicproject.song.DownloadedSong;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.ui.MainActivityUI;
import com.cse110.ucsd.flashbackmusicproject.ui.NavDrawerExpandableListViewAdapter;
import com.cse110.ucsd.flashbackmusicproject.ui.NavDrawerListener;
import com.cse110.ucsd.flashbackmusicproject.ui.UIListener;
import com.cse110.ucsd.flashbackmusicproject.user.User;
import com.cse110.ucsd.flashbackmusicproject.user.UserBuilder;
import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;
import com.cse110.ucsd.flashbackmusicproject.utility.DownloadedSongsUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/*
 * MainActivity controls the app functionality and layout
 */
public class MainActivity extends AppCompatActivity implements MediaPlayerStateChangeListener, NavDrawerListener,
        UIListener, PlaylistLoadedListener, IUserLoader, SongDownloadListener {

    // wrapper of android mediaplayer
    private MyMediaPlayer myMediaPlayer;

    // whether or not the app is currently in flashback mode
    //private boolean flash_mode;

    // for loading previous state
    public static final String SHAREDPREF_NAME = "flashback";

    // user currently logged in
    private User user;

    public static final String TAG = "MainActivity";

    List<Song> allUserSongs;

    private DataManager dataManager;

    private MainActivityUI uiManager;

    private MusicDownloadManager musicDownloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        allUserSongs = new ArrayList<>();
        DownloadedSongsUtility.loadBitmap(this);

        String name, email, token, pseudo, google;
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);

        name = sharedPreferences.getString("name", null);
        email = sharedPreferences.getString("email", null);
        token = sharedPreferences.getString("token", null);
        pseudo = sharedPreferences.getString("pseudo", null);
        google = sharedPreferences.getString("google", "false");

        // initialize user
        UserBuilder builder = new UserBuilder()
                .setName(name)
                .setEmail(email)
                .setCode(token)
                .buildPseudoName(pseudo);
        if (google.equals("true")) {
            builder.setGoogleLogin(true).buildFriends(this);
        }

        user = builder.build();

        // Get download manager
        musicDownloadManager = new MusicDownloadManager(this);
        musicDownloadManager.addSongDownloadListener(this);

        // initialize app data

        dataManager = new DataManager(user, this, this);

        //flash_mode = dataManager.getState();

        // initialize ui manager
        uiManager = new MainActivityUI(this, this);

        myMediaPlayer = new MyMediaPlayer(MainActivity.this);
        myMediaPlayer.addStateChangeListener(this);
        myMediaPlayer.addStateChangeListener(uiManager);
        dataManager.addPlaylistLoadListener(this);

        // update song data
        updateSongDisplay();

        if (user != null) {
            Toast.makeText(this, "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
            // remove if firebase database
            onUserLoaded();
        } else if (user == null) {
            finish();
        }

        // update seek bar
        final Handler handler = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myMediaPlayer.isPlaying()) {
                    uiManager.setSeekBarProgress(myMediaPlayer.getCurrentPosition());
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onUserLoaded() {
        allUserSongs = loadAllUserSongs();
        uiManager.loadNavDrawer(this, this);
        uiManager.enableVibeModeButton();
        Log.d(TAG, "User Loaded");

        if (!user.isGoogleUser()) {
            //dataManager.getVibeSongs();
        }
    }

    @Override
    public void onFriendsLoaded() {
        //dataManager.getVibeSongs();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateSongDisplay() {
        if (myMediaPlayer.playlistIsEmpty()) {
            Log.d(TAG, "Resetting Song Information");
            uiManager.resetSongInformation();
            return;
        }

        uiManager.updateSongInformation(myMediaPlayer.getSongTitle(), myMediaPlayer.getSongArtist(), myMediaPlayer.getSongLocation(), myMediaPlayer.getSongTime(),
                myMediaPlayer.getSongArt(), myMediaPlayer.getNextSongTitle(), myMediaPlayer.getPlayer());


        uiManager.updateFavoriteButton(myMediaPlayer.getSongFavoriteStatus());
        uiManager.updatePlayButton(false);

        Log.d(TAG, "Update DisplayData");
    }

    @Override
    protected void onDestroy() {
        myMediaPlayer.release();

        if (musicDownloadManager != null)
            unregisterReceiver(musicDownloadManager.getReceiver());

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        uiManager.setMenu(menu);
        uiManager.updateToolbar();
        uiManager.disableVibeModeButton();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.music_note) {
            Log.d(TAG, "Toggling Mode");
            flash_mode = !flash_mode;
            dataManager.setState(flash_mode);

            if (flash_mode) {
                dataManager.getVibeSongs(this);
            } else {
                myMediaPlayer.updatePlaylist(PlaylistFactory.getEmptyPlaylist());
            }


            uiManager.updateToolbar(flash_mode);
            updateSongDisplay();
            uiManager.updatePlayButton(true);
        } else*/ if (id == R.id.downloader) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String currentSongName = musicDownloadManager.queueInfo();

            if (currentSongName.equals("flashback_queue_empty")) {
                builder.setTitle("Give the download URL of song...");
                final EditText input = new EditText(this);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "dialog ok clicked");

                        String downloadURL = input.getText().toString();

                        try {
                            musicDownloadManager.downloadMusic(downloadURL);
                        } catch (IllegalArgumentException e) {
                            Log.d(TAG, "Hello Hacker!");
                            Toast.makeText(MainActivity.this, "Song download failed!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

            } else {
                builder.setTitle("Downloading " + currentSongName + "...");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }

            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Song> loadAllUserSongs() {
        List<Song> songs = DownloadedSongsUtility.getAllDownloadedSongs(this);
        for (Song s : songs) {
            dataManager.load(s);
        }
        return songs;
    }

    public List<Song> getListOfAllSongs() {
        return DownloadedSongsUtility.getAllDownloadedSongs(this);
    }

    @Override
    public void onSongDownloaded(List<String> urls, List<String> paths) {
        for (int i = 0; i < paths.size(); i++) {

            Song downloadedSong = new DownloadedSong(paths.get(i), urls.get(i));
            if (!allUserSongs.contains(downloadedSong)) {
                dataManager.load(downloadedSong);
                allUserSongs.add(downloadedSong);
                uiManager.loadNavDrawer(this, this);
            }
        }
    }
    /* MediaplayerStateChangeListener callbacks */

    @Override
    public void onSongChanged(Song newSong) {
        Log.d("MainActivity", "Song Changed");
        updateSongDisplay();
        uiManager.setSeekBarLength(myMediaPlayer.getSongDuration());
    }

    @Override
    public void onSongComplete(Song song) {
        dataManager.updateSongInfo(song);
    }

    @Override
    public void onPlaylistChanged(IPlaylist playlist) {

    }

    /* Nav Drawer Listener Callbacks */

    @Override
    public void onNavDrawerLoaded(NavDrawerExpandableListViewAdapter adapter) {
        Log.d(TAG, "populateNavDrawer");
        //List<Song> songs = new RawLoader(getListOfAllSongs()).getPlaylist();
        List<Song> songs = DownloadedSongsUtility.getAllDownloadedSongs(this);

        Set<String> albumTitles = new TreeSet<>();

        for (Song song : songs) {
            albumTitles.add(song.getAlbum());
        }

        for (String albumTitle : albumTitles) {
            adapter.addAlbum((AlbumPlaylistDecorator) PlaylistFactory.getAlbumPlaylist(songs, albumTitle));
        }
    }

    @Override
    public void onDownloadedHistoryClicked() {
        Intent downloadedSongsIntent = new Intent(MainActivity.this, DownloadedSongsActivity.class);
        startActivity(downloadedSongsIntent);
    }

    @Override
    public void onLogOutClicked() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHAREDPREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", null);
        editor.putString("email", null);
        editor.putString("token", null);
        editor.putString("pseudo", null);
        editor.putString("google", "false");
        editor.apply();

        finish();
    }

    @Override
    public void onAlbumClicked(String album) {
        //if (!flash_mode) {
            myMediaPlayer.updatePlaylist(PlaylistFactory.getAlbumPlaylist(getListOfAllSongs(), album));
            myMediaPlayer.play();
            Log.d(TAG, "Play Album");
        /*} else {
            Toast.makeText(this, "Exit vibe mode to play your own music", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onSongClicked(String title, String album) {
        //if (!flash_mode) {
            myMediaPlayer.updatePlaylist(PlaylistFactory.getSingleSongPlaylist(getListOfAllSongs(), title));
            //myMediaPlayer.skipToTrack(title);
            myMediaPlayer.play();
            Log.d(TAG, "Play Song");
        /*} else {
            Toast.makeText(this, "Exit vibe mode to play your own music", Toast.LENGTH_LONG).show();
        }*/
    }

    /* UI Listener callbacks */

    @Override
    public void onPlayClicked() {
        //track is playing
        if (!myMediaPlayer.isPlaying()) {
            if (!myMediaPlayer.playlistIsEmpty()) {
                myMediaPlayer.play();
                uiManager.updatePlayButton(false);
                Log.d(TAG, "Playing Music");
            }
        }
        // not playing
        else {
            if (!myMediaPlayer.playlistIsEmpty()) {
                myMediaPlayer.pause();
                uiManager.updatePlayButton(true);
                Log.d(TAG, "Pausing Music");
            }
        }
    }

    @Override
    public void onSkipClicked() {
        myMediaPlayer.next();
        updateSongDisplay();
    }

    @Override
    public void onPrevClicked() {
        myMediaPlayer.prev();
        updateSongDisplay();
    }

    @Override
    public void onFavClicked() {
        // state is netural
        if (myMediaPlayer.playlistIsEmpty()) {
            Log.d(TAG, "Neutral State");
            return;
        }

        int favorite = myMediaPlayer.getSongFavoriteStatus();
        favorite = (favorite + 1) % 3;

        myMediaPlayer.setSongFavoriteStatus(favorite);
        uiManager.updateFavoriteButton(favorite);
        dataManager.setFavoriteStatus(myMediaPlayer.getSongTitle(), favorite);

        // automatically skip user just disliked it
        if (favorite == Dictionary.DISLIKED) {
            onSkipClicked();
        }
    }

    @Override
    public void onRepeatClicked() {
        myMediaPlayer.toggleRepeat();
    }

    @Override
    public void onSeekChanged(int value) {
        myMediaPlayer.seekTo(value);
    }

    @Override
    public void onPlaylistLoaded(List<Song> songs, String location, String time) {
        IPlaylist vibeModePlaylist = PlaylistFactory.getVibePlaylist(songs, location, time);
        List<String> toDownload = new ArrayList<>();
        for (Song s : vibeModePlaylist.getPlaylist()) {
            if (!allUserSongs.contains(s)) {
                toDownload.add(s.getURL());
            }
        }
        musicDownloadManager.downloadMusic(toDownload);
        myMediaPlayer.updatePlaylist(vibeModePlaylist);
    }

}
