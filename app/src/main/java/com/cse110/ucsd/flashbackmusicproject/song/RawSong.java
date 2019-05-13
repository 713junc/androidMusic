package com.cse110.ucsd.flashbackmusicproject.song;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.MyMetaDataExtractor;
import com.cse110.ucsd.flashbackmusicproject.R;
import com.cse110.ucsd.flashbackmusicproject.mediaplayer.RawStrategy;
import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;

import java.util.ArrayList;

/**
 * A song must be loaded through the resource ID assigned by android at compile time.
 * The metadata of a song consists of the title, artist, album, and art. In addition,
 * a song also tracks whether or not it is liked by the user, and the last known locations it
 * was played at the last known time
 */
public class RawSong extends Song{
    public RawSong(int resource, Context c) {

        this.resource = resource;
        this.loadStrategy = new RawStrategy();
        this.title = validateTitle(MyMetaDataExtractor.getSongTitle(resource, c));
        this.artist = MyMetaDataExtractor.getSongArtist(resource, c);
        this.album = MyMetaDataExtractor.getSongAlbum(resource, c);
        this.url = null;

        // extract album art
        Bitmap map = MyMetaDataExtractor.getSongArt(resource, c);
        if (map != null) {
            this.image = map;
        } else {
            this.image = BitmapFactory.decodeResource(c.getResources(), R.drawable.ic_launcher_background);
        }

        setMostRecentLocation("Unknown");
        setMostRecentTime("Unknown");

        favStatus = Dictionary.NEUTRAL;

        locations = new ArrayList<>();
        generalTimes = new ArrayList<>();
    }
}
