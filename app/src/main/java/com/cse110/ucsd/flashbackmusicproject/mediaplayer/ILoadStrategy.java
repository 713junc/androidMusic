package com.cse110.ucsd.flashbackmusicproject.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
/**
 * Created by Derek on 3/13/2018.
 */
public interface ILoadStrategy {
    public void loadSong(MediaPlayer mediaPlayer, Context context, Song song);
}
