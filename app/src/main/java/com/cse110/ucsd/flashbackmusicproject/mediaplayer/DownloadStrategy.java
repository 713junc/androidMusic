package com.cse110.ucsd.flashbackmusicproject.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.download.MusicDownloadManager;
import com.cse110.ucsd.flashbackmusicproject.download.SongDownloadListener;
import com.cse110.ucsd.flashbackmusicproject.song.DownloadedSong;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.utility.DownloadedSongsUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by Derek on 3/13/2018.
 */

public class DownloadStrategy implements ILoadStrategy {

    private static final String TAG = "downloaded-song";

    @Override
    public void loadSong(final MediaPlayer mediaPlayer, final Context context, final Song song) {
        Log.d(TAG, song.getTitle());

        MusicDownloadManager musicDownloadManager = new MusicDownloadManager(context);

        if (song.getSongPath() == null) {
            song.setSongPath(musicDownloadManager.getSongFilePath(song.getTitle()));
        }
        if (song.getArt() == null && song.getSongPath() != null) {
            song.extractArt();
        }

        if (!song.isDownloading()) {
            if (song.getSongPath() == null) {
                Log.v("taggy", "Can't find file path for " + song.getTitle());


                musicDownloadManager.addSongDownloadListener(new SongDownloadListener() {
                    @Override
                    public void onSongDownloaded(List<String> url, List<String> localFilePath) {

                        song.setSongPath(localFilePath.get(0));
                        song.setDownloading(false);
                        song.extractArt();
                        loadSong(mediaPlayer, context, song);
                    }
                });

                String url = "";
                try {
                    url = URLDecoder.decode(song.getURL(), StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                song.setDownloading(true);
                musicDownloadManager.downloadMusic(url);

            } else {
                try {
                    Log.v("taggy", "Trying to load " + song.getTitle() + " from " + song.getSongPath());
                    mediaPlayer.setDataSource(song.getSongPath());
                    mediaPlayer.prepareAsync();
                } catch (Exception e) {
                    Log.v("taggy", "Failed to load song " + song.getTitle());
                    Log.v("taggy", "Error: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }

        }
    }
}