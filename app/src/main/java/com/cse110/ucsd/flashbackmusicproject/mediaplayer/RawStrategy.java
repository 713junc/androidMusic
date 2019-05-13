package com.cse110.ucsd.flashbackmusicproject.mediaplayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.cse110.ucsd.flashbackmusicproject.song.Song;

/**
 * Created by Derek on 3/13/2018.
 */

public class RawStrategy implements ILoadStrategy{

    public void loadSong(MediaPlayer mediaPlayer, Context context, Song song) {
        AssetFileDescriptor assetFileDescriptor = context.getResources().openRawResourceFd(song.getResource());
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
            assetFileDescriptor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}