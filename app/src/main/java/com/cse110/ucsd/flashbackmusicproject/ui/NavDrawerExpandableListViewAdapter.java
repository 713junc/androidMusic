package com.cse110.ucsd.flashbackmusicproject.ui;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cse110.ucsd.flashbackmusicproject.R;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.playlist.Playlist;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.AlbumPlaylistDecorator;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.utility.DownloadedSongsUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Adapter for the Navigation drawer
 */

public class NavDrawerExpandableListViewAdapter implements ExpandableListAdapter {

    NavDrawerListener listener;
    List<AlbumPlaylistDecorator> albums;
    AppCompatActivity activity;

    public NavDrawerExpandableListViewAdapter(AppCompatActivity activity, NavDrawerListener listener){
        this.activity = activity;
        this.listener = listener;
        albums = new ArrayList<>();

        /*
        List<Song> downloadedSongs = DownloadedSongsUtility.getAllDownloadedSongs(activity);
        IPlaylist playlist = new Playlist(downloadedSongs);

        Set<String> albumTitles = new TreeSet<>();
        for(Song song : downloadedSongs){
            albumTitles.add(song.getAlbum());
        }

        for(String albumTitle : albumTitles){
            albums.add(new AlbumPlaylistDecorator(playlist, albumTitle));
        }
        */
    }


    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return albums.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if(albums.size() > i){
            return albums.get(i).size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getGroup(int i) {
        if(albums.size() > i){
            return albums.get(i);
        }else{
            return null;
        }
    }

    @Override
    public Object getChild(int i, int i1) {
        AlbumPlaylistDecorator album = (AlbumPlaylistDecorator) getGroup(i);
        if(album == null){
            return 0;
        }else{
            return album.get(i1);
        }
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return (i * 10) + i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final AlbumPlaylistDecorator album = (AlbumPlaylistDecorator) getGroup(i);

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        ViewGroup v = (ViewGroup) layoutInflater.inflate(R.layout.album_group, viewGroup, false);
        TextView titleTextView = v.findViewById(R.id.group_text_view);
        titleTextView.setText(album.getAlbumTitle());

        ImageButton playAlbumButton = (ImageButton) v.findViewById(R.id.group_play);
        playAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAlbumClicked(album.getAlbumTitle());
            }
        });

        return v;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        AlbumPlaylistDecorator album = (AlbumPlaylistDecorator) getGroup(i);
        final Song song = album.get(i1);


        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        ViewGroup v = (ViewGroup) layoutInflater.inflate(R.layout.album_song, viewGroup, false);
        TextView titleTextView = v.findViewById(R.id.song_title);

        titleTextView.setText(song.getTitle());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSongClicked(song.getTitle(), song.getAlbum());
            }
        });

        return v;

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return albums.size() == 0;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }

    public void addAlbum(AlbumPlaylistDecorator album){
        albums.add(album);
    }

    public void addSong(Song song){
        boolean added = false;
        for(AlbumPlaylistDecorator album : albums){
            if(album.getAlbumTitle().equals(song.getAlbum())){
                album.addSong(song);
                added = true;
                break;
            }
        }

        if(! added){
            Playlist newPlaylist = new Playlist();
            newPlaylist.addSong(song);
            addAlbum(new AlbumPlaylistDecorator(newPlaylist, song.getAlbum()));
        }
    }
}
