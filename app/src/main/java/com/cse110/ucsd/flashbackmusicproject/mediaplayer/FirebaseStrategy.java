package com.cse110.ucsd.flashbackmusicproject.mediaplayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.song.Song;

/**
 * Created by prgzz on 3/15/2018.
 */

public class FirebaseStrategy implements ILoadStrategy {

    @Override
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
