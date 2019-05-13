package com.cse110.ucsd.flashbackmusicproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.cse110.ucsd.flashbackmusicproject.database.MyFirebaseDatabase;
import com.cse110.ucsd.flashbackmusicproject.download.MusicDownloadManager;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.utility.DownloadedSongsUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DownloadedSongsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView songListView;
    Spinner sortSpinner;
    ArrayAdapter<Song> arrayAdapter;

    List<String> sortOptions;
    List<Comparator<Song>> sortingComparators;
    List<Song> songs;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_downloaded_songs);

        songListView = (ListView) findViewById(R.id.song_list);
        sortSpinner = (Spinner) findViewById(R.id.sort_spinner);
        sortOptions = new ArrayList<String>();
        sortingComparators = new ArrayList<Comparator<Song>>();
        addSortOptions();
        addSortComparators();
        songs = DownloadedSongsUtility.getAllDownloadedSongs(this);

        arrayAdapter = new ArrayAdapter<Song>(this, R.layout.downloaded_song, R.id.song_info, songs);

        songListView.setAdapter(arrayAdapter);
        setupSpinner();
    }

    private void addSortOptions() {
        sortOptions.add("Title");
        sortOptions.add("Album");
        sortOptions.add("Artist");
        sortOptions.add("Favorite Status");
    }


    private void addSortComparators() {
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
                return  t1.getFavoriteStatus() - song.getFavoriteStatus();
            }
        });

    }

    private void setupSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sortOptions);
        sortSpinner.setAdapter(arrayAdapter);
        sortSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("teen", "" + songs.size());
        Collections.sort(songs, sortingComparators.get(i));
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
