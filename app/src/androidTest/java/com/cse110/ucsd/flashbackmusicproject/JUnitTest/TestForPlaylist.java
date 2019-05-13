
package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.cse110.ucsd.flashbackmusicproject.R;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.playlist.Playlist;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.AlbumPlaylistDecorator;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.ArtistPlaylistDecorator;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.LikedPlaylistDecorator;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.LocationPlaylistDecorator;
import com.cse110.ucsd.flashbackmusicproject.playlist.decorators.TimePlaylistDecorator;
import com.cse110.ucsd.flashbackmusicproject.song.RawSong;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.database.MyLocalDatabase;
import com.cse110.ucsd.flashbackmusicproject.loaders.AlbumLoader;
import com.cse110.ucsd.flashbackmusicproject.loaders.FlashbackLoader;
import com.cse110.ucsd.flashbackmusicproject.song.TestSong;
import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



public class TestForPlaylist {
    private Context context;
    IPlaylist playlist = new Playlist();

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();

        Song song1 = new TestSong("Test1", "Art1", "Alb1", Dictionary.NEUTRAL);
        Song song2 = new TestSong("Test2", "Art1", "Alb2", Dictionary.LIKED);
        Song song3 = new TestSong("Test3", "Art2", "Alb1", Dictionary.DISLIKED);
        Song song4 = new TestSong("Test4", "Art1", "Alb3", Dictionary.LIKED);

        song1.addLocation( "San Diego");
        song1.addTime( "Afternoon");

        song2.addLocation( "San Diego");
        song2.addTime( "Afternoon");

        song3.addLocation( "LA");
        song3.addTime( "Evening");

        playlist.addSong( song1 );
        playlist.addSong( song2 );
        playlist.addSong( song3 );
        playlist.addSong( song4 );


    }
    @Test
    public void testIPlaylist() {

        assertEquals( "Test1", playlist.get(0).getTitle());
        assertEquals( "Test2", playlist.get(1).getTitle());
        assertEquals( "Test3", playlist.get(2).getTitle());

        assertEquals( "Art1", playlist.get(0).getArtist());
        assertEquals( "Art1", playlist.get(1).getArtist());
        assertEquals( "Art2", playlist.get(2).getArtist());

        assertEquals( 4, playlist.size());

    }

    @Test
    public void testAlbumDecorator() {
        IPlaylist albumDecorator = new AlbumPlaylistDecorator(playlist, "Alb1");

        assertEquals( "Test1", albumDecorator.get(0).getTitle());
        assertEquals( "Test3", albumDecorator.get(1).getTitle());

        for(int i = 0; i < albumDecorator.size(); i++){
            assertEquals( "Alb1", albumDecorator.get(i).getAlbum());
        }


        assertEquals( 2, albumDecorator.size());
    }

    @Test
    public void testArtistDecorator() {
        IPlaylist artistDecorator = new ArtistPlaylistDecorator(playlist, "Art1");

        assertEquals( "Test1", artistDecorator.get(0).getTitle());
        assertEquals( "Test2", artistDecorator.get(1).getTitle());
        assertEquals( "Test4", artistDecorator.get(2).getTitle());

        for(int i = 0; i < artistDecorator.size(); i++){
            assertEquals( "Art1", artistDecorator.get(i).getArtist());

        }


        assertEquals( 3, artistDecorator.size());
    }

    @Test
    public void testLocationDecorator() {
        IPlaylist locationDecorator = new LocationPlaylistDecorator(playlist, "San Diego");

        assertEquals( "Test1", locationDecorator.get(0).getTitle());
        assertEquals( "Test2", locationDecorator.get(1).getTitle());

        assertEquals( 2, locationDecorator.size());
    }

    @Test
    public void testTimeDecorator() {
        IPlaylist timeDecorator = new TimePlaylistDecorator(playlist, "Evening");

        assertEquals( "Test3", timeDecorator.get(0).getTitle());
        assertEquals( 1, timeDecorator.size());
    }

    @Test
    public void testLikedDecorator() {
        IPlaylist likedDecorator = new LikedPlaylistDecorator(playlist);

        assertEquals( "Test2", likedDecorator.get(0).getTitle());
        assertEquals( "Test4", likedDecorator.get(1).getTitle());

        for(int i = 0; i < likedDecorator.size(); i++){
            assertTrue(likedDecorator.get(i).isLiked());
        }

        assertEquals( 2, likedDecorator.size());
    }

}