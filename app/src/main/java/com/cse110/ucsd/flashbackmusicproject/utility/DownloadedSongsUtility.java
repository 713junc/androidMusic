package com.cse110.ucsd.flashbackmusicproject.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.R;
import com.cse110.ucsd.flashbackmusicproject.song.DownloadedSong;
import com.cse110.ucsd.flashbackmusicproject.song.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Derek on 3/15/2018.
 */

public class DownloadedSongsUtility {

    private static List<Song> songs;

    private static Bitmap defaultBitMap;

    private static void updateAllDownloadedSongs(Context context) {
        songs = new ArrayList<Song>();
        File musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File[] files = musicDir.listFiles();
        if(files != null) {
            for (File file : files) {
                if (file.getPath().endsWith(".mp3") && new File(file.getPath()).exists()) {
                    Song downloadedSong = new DownloadedSong(file.getPath(), null);
                    songs.add(downloadedSong);
                }
            }
        }


        File rootDir = Environment.getExternalStorageDirectory();
        List<Song> allSongs = getAllMP3sOnDevice(rootDir);

        for(Song song : allSongs){
            if(!songs.contains(song)){
                songs.add(song);
            }
        }
    }

    private static List<Song> getAllMP3sOnDevice(File currentFile){
        List<Song> songs = new ArrayList<>();
        if(currentFile == null){
            return songs;
        }

        if (currentFile.getPath().endsWith(".mp3") && new File(currentFile.getPath()).exists()) {
            Song downloadedSong = new DownloadedSong(currentFile.getPath(), null);
            songs.add(downloadedSong);
        }

        for(File file : currentFile.listFiles()){
            if (file.getPath().endsWith(".mp3") && new File(file.getPath()).exists()) {
                Song downloadedSong = new DownloadedSong(file.getPath(), null);
                if(!songs.contains(downloadedSong)){
                    songs.add(downloadedSong);
                }
            }

            if(file.isDirectory()){
                List<Song> subdirectorySongs = getAllMP3sOnDevice(file);
                for(Song song : subdirectorySongs){
                    if(!songs.contains(song)){
                        songs.add(song);
                    }
                }
            }
        }

        return songs;
    }

    public static void loadBitmap(Context context) {
        /*Drawable drawable = context.getDrawable(R.drawable.ic_music_note);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);*/

        defaultBitMap = ((BitmapDrawable) context.getDrawable(R.drawable.ic_music_image)).getBitmap();
    }

    public static Bitmap getDefaultBitMap() {return defaultBitMap;}

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



}
