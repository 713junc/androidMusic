package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;

import com.cse110.ucsd.flashbackmusicproject.song.DownloadedSong;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.song.TestSong;
import com.cse110.ucsd.flashbackmusicproject.utility.DownloadedSongsUtility;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by sseung385 on 3/10/18.
 */

public class TestDownloadSongs {
    Song song1;
    @Before
    public void setup() {
        song1 = new TestSong( "Song1", "Artist1", "Album1", 0);

    }
    @Test
    public void testUpdateAllDownloadedSongs() {
        Context context = InstrumentationRegistry.getTargetContext();
        int numFiles = context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS).listFiles().length;
        assertEquals( numFiles, MockDownloadedSongUtility.getAllDownloadedSongs( context ).size());

    }

    @Test
    public void testgetAllDownloadSongs() {
        Context context = InstrumentationRegistry.getTargetContext();
        List<Song> downloadedSongs = MockDownloadedSongUtility.getAllDownloadedSongs( context );
        assertTrue( downloadedSongs.isEmpty() );


    }


}
