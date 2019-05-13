package com.cse110.ucsd.flashbackmusicproject.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cse110.ucsd.flashbackmusicproject.R;
import com.cse110.ucsd.flashbackmusicproject.mediaplayer.MediaPlayerStateChangeListener;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;
import com.cse110.ucsd.flashbackmusicproject.utility.TimeFormatter;

/**
 * Created by Derek on 3/7/2018.
 */

public class MainActivityUI implements View.OnClickListener, MediaPlayerStateChangeListener, SeekBar.OnSeekBarChangeListener {

    // displays album art
    private ImageView albumArt;

    // displays the title, artist, and album
    private TextView songDataTextView;

    // the title text at the top
    private TextView mainTextView, elapsedTime, totalTime;

    // controls the nav drawer layout and settings
    private ExpandableListView expandableListView;
    private ListView currentPlaylistListView;
    private NavDrawerExpandableListViewAdapter expandableListAdapter;

    // playButton and favorite change drawables on click
    private Button playButton, favoriteButton, viewDownloadedSongsButton, nextButton, prevButton, logOut, repeat;

    // for adding songs/albums to menu layout
    private Menu menu;
    private Toolbar toolbar;

    // for toggling the lock of the drawer
    private DrawerLayout drawer;

    private Drawable liked, disliked, neutral, play, pause, defaultImage, note, repeatOff, repeatOn;

    int primaryColor, flashColor;

    String title_vibe, title_home;

    UIListener uiListener;

    AppCompatActivity appCompatActivity;

    private boolean repeating;

    private SeekBar seekBar;

    public MainActivityUI(AppCompatActivity c, UIListener uiListener) {

        this.uiListener = uiListener;
        appCompatActivity = c;

        // initialize all the instance data
        songDataTextView = c.findViewById(R.id.songTitleTextView);
        songDataTextView.setSelected(true);
        albumArt = c.findViewById(R.id.albumArt);
        playButton = c.findViewById(R.id.playButton);
        prevButton = c.findViewById(R.id.previousButton);
        nextButton = c.findViewById(R.id.nextButton);
        mainTextView = c.findViewById(R.id.mainTextView);
        favoriteButton = c.findViewById(R.id.favoriteButton);
        repeat = c.findViewById(R.id.repeatButton);
        viewDownloadedSongsButton = c.findViewById(R.id.view_downloaded_songs_button);
        toolbar = c.findViewById(R.id.toolbar);
        logOut = c.findViewById(R.id.log_out);
        seekBar = c.findViewById(R.id.seekBar);
        elapsedTime = c.findViewById(R.id.elapsedTime);
        totalTime = c.findViewById(R.id.totalTime);

        playButton.setOnClickListener(this);
        favoriteButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        repeat.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        title_home = c.getResources().getString(R.string.homeTitle);
        title_vibe = c.getResources().getString(R.string.flashbackTitle);

        // set the toolbar
        c.setSupportActionBar(toolbar);

        // nav drawer
        drawer = c.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                c, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        neutral = c.getDrawable(R.drawable.ic_add_black_24dp);
        disliked = c.getDrawable(R.drawable.ic_remove_black_24dp);
        liked = c.getDrawable(R.drawable.ic_check_black_24dp);
        play = c.getDrawable(R.drawable.ic_play_arrow_black_24dp);
        pause = c.getDrawable(R.drawable.ic_pause_black_24dp);
        defaultImage = c.getDrawable(R.drawable.ic_music_note);
        note = c.getDrawable(R.drawable.ic_note);
        repeatOff = c.getDrawable(R.drawable.ic_repeat_black_24dp);
        repeatOn = c.getDrawable(R.drawable.ic_repeat_white_24dp);

        primaryColor = c.getColor(R.color.colorPrimary);
        flashColor = c.getColor(R.color.color_for_flashback);
    }

    public void loadNavDrawer(AppCompatActivity c, final NavDrawerListener navDrawerListener) {
        // nav drawer
        expandableListView = c.findViewById(R.id.navigationmenu);
        currentPlaylistListView = c.findViewById(R.id.current_playlist);

        expandableListAdapter = new NavDrawerExpandableListViewAdapter(c, navDrawerListener);

        expandableListView.setAdapter(expandableListAdapter);
        navDrawerListener.onNavDrawerLoaded(expandableListAdapter);

        viewDownloadedSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navDrawerListener.onDownloadedHistoryClicked();
            }
        });



        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawerListener.onLogOutClicked();
            }
        });
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void resetSongInformation() {
        //if (vibeMode) {
          //  songDataTextView.setText("No Matched Songs");
        //} else {
            songDataTextView.setText("Select an album");
        //}
        albumArt.setImageDrawable(defaultImage);
        updatePlayButton(true);
    }

    public void updateFavoriteButton(int newFavorite) {
        if (newFavorite == Dictionary.NEUTRAL) {
            favoriteButton.setBackground(neutral);
            Log.d("TAG", "Neutral State");

        } else if (newFavorite == Dictionary.LIKED) {
            favoriteButton.setBackground(liked);
            Log.d("TAG", "Liked State");

        } else {
            favoriteButton.setBackground(disliked);
            Log.d("TAG", "Disliked State");
        }
    }

    public void updatePlayButton(boolean displayPlay) {
        if (displayPlay) {
            playButton.setBackground(play);
        } else {
            playButton.setBackground(pause);
        }
    }

    public void setMainTextView() {
        //if (vibeMode) {
            //mainTextView.setText(title_vibe);
        //} else {
            mainTextView.setText(title_home);
        //}
    }

    public void updateToolbar() {
        /*
        MenuItem item = menu.findItem(R.id.music_note);

        //if (!vibeMode) {
            note.mutate().setColorFilter(
                    Color.argb(150, 0, 0, 0), PorterDuff.Mode.SRC_IN);
            item.setIcon(note);

            toolbar.setBackgroundColor(primaryColor);

            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        } else {
            note.mutate().setColorFilter(
                    Color.argb(255, 255, 255, 255), PorterDuff.Mode.SRC_IN);
            item.setIcon(note);

            toolbar.setBackgroundColor(flashColor);

            //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        }

        setMainTextView();
        */
    }

    public void updateSongInformation(String title, String artist, String location, String time, Bitmap bitmap, String upNext, String username) {
        songDataTextView.setText( title + " by " + artist + " played in " + location + " in the " + time +" Up Next: " + upNext);
        albumArt.setImageBitmap(bitmap);
    }

    public void play() {
        uiListener.onPlayClicked();
    }

    public void next() {
        uiListener.onSkipClicked();
    }

    public void prev() {
        uiListener.onPrevClicked();
    }

    public void enableVibeModeButton(){
        /*if(menu != null){
            menu.findItem(R.id.music_note).setEnabled(true);
        }*/
    }

    public void disableVibeModeButton(){
        //menu.findItem(R.id.music_note).setEnabled(false);
    }

    public void favoriteClicked() {
        uiListener.onFavClicked();
    }

    @Override
    public void onClick(View view) {
        if(view == playButton) {
            play();
        } else if (view == nextButton) {
            next();
        } else if (view == prevButton) {
            prev();
        } else if (view == favoriteButton) {
            favoriteClicked();
        } else if (view == repeat) {
            toggleRepeat();
        }
    }

    public void toggleRepeat() {
        if (repeating) {
            repeat.setBackground(repeatOff);
        } else {
            repeat.setBackground(repeatOn);
        }
        repeating = !repeating;
        uiListener.onRepeatClicked();
    }

    public void setSeekBarLength(int duration) {
        seekBar.setMax(duration);
        totalTime.setText(TimeFormatter.getTime(duration));
    }

    public void setSeekBarProgress(int progress) {
        seekBar.setProgress(progress);
    }

    @Override
    public void onSongChanged(Song newSong) {

    }

    @Override
    public void onSongComplete(Song song) {

    }

    @Override
    public void onPlaylistChanged(IPlaylist playlist) {
        currentPlaylistListView.setAdapter( new ArrayAdapter<Song>(appCompatActivity, R.layout.downloaded_song, R.id.song_info,playlist.getPlaylist()));
        updatePlayButton(true);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            uiListener.onSeekChanged(progress);
        }
        elapsedTime.setText(TimeFormatter.getTime(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
