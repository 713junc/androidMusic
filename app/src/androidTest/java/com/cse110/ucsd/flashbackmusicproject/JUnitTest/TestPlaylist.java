package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.playlist.Playlist;
import com.cse110.ucsd.flashbackmusicproject.playlist.PlaylistFactory;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.song.TestSong;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Created by sseung385 on 3/12/18.
 */

public class TestPlaylist {
    IPlaylist playlist;
    Song song1, song2, song3;
    @Before
    public void setup() {
        playlist = new Playlist();
        song1 = new TestSong( "song1", "artist1", "album1", 0);
        song2 = new TestSong( "song2", "artist2", "album2", 1);
        song3 = new TestSong( "song3", "artist3", "album3", 2);

    }

    @Test
    public void testAdd() {
        playlist.addSong( song1 );
        playlist.addSong( song2 );
        playlist.addSong( song3 );
        assertEquals( 3, playlist.size());
    }

    @Test
    public void testSize() {
        assertEquals( 0, PlaylistFactory.getEmptyPlaylist().size() );

        playlist.addSong( song1 );
        assertEquals( 1, playlist.size());

        playlist.addSong( song2 );
        assertEquals( 2, playlist.size());

        playlist.addSong( song3 );
        assertEquals( 3, playlist.size());
    }


}
