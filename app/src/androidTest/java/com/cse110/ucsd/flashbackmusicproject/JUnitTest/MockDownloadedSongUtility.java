package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.song.DownloadedSong;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.utility.DownloadedSongsUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jungwon on 3/16/18.
 */

public class MockDownloadedSongUtility extends DownloadedSongsUtility {

    private static List<Song> songs;

    private static void updateAllDownloadedSongs(Context context) {
        songs = new ArrayList<Song>();
        File musicDir = context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
        File[] files = musicDir.listFiles();
        if(files != null) {
            for (File file : files) {
                if (file.getPath().endsWith(".mp3") && new File(file.getPath()).exists()) {
                    Song downloadedSong = new DownloadedSong(context, file.getPath(), null);
                    songs.add(downloadedSong);
                }
            }
        }
    }

    public static void add(Song song) {
        if(! songs.contains(song)){
            Log.v("taggy", "Adding " + song.getTitle());
            songs.add(song);
        }else{
            Log.v("taggy", "Not adding " + song.getTitle());
        }
    }

    public static List<Song> getAllDownloadedSongs(Context context){
        if (songs == null) {
            updateAllDownloadedSongs(context);
        }
        return songs;
    }

    public static int size() {
        return songs.size();

    }

}
