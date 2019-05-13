package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.cse110.ucsd.flashbackmusicproject.database.DataManager;
import com.cse110.ucsd.flashbackmusicproject.database.IUserLoader;
import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.playlist.Playlist;
import com.cse110.ucsd.flashbackmusicproject.playlist.PlaylistFactory;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.song.TestSong;
import com.cse110.ucsd.flashbackmusicproject.timer.RealTimeKeeper;
import com.cse110.ucsd.flashbackmusicproject.user.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sseung385 on 3/13/18.
 */

public class TestForDatamanager {
    DataManager dm;
    Song song1, song2, song3;
    IPlaylist playlist;

    @Before
    public void setup() {
        IUserLoader loader;
        Context context = InstrumentationRegistry.getTargetContext();
        User user = new User();
        dm = new DataManager( user, context );
        song1 = new TestSong( "Song1", "C", "A", 0);
        song2 = new TestSong( "Song2", "B", "B", 1);
        song3 = new TestSong( "Song3", "A", "C", 2);

    }

    @Test
    public void testUpdateSongInfo() {
        song1.setFavoriteStatus( 1000 );
        dm.updateSongInfo( song1 );
        try {
            Thread.sleep( 1000 );
        }
        catch ( InterruptedException e ) {

        }
        //assertEquals( 1000, dm.getFavoriteStatus( "Song1") );



        song1.setFavoriteStatus( 0 );
        dm.updateSongInfo( song2 );
        //assertEquals( 0, dm.getFavoriteStatus( "Song2") );
    }

    @Test
    public void testSortByTitle() {
        playlist = PlaylistFactory.getEmptyPlaylist();
        playlist.addSong( song1 );
        playlist.addSong( song2 );

        IPlaylist sortedList = dm.sortSongsByTitle( playlist );
        assertEquals( "Song1", sortedList.get(0).getTitle() );
        assertEquals( "Song2", sortedList.get(1).getTitle() );

    }

    @Test
    public void testSortByAlbum() {
        playlist = PlaylistFactory.getEmptyPlaylist();
        playlist.addSong( song1 );
        playlist.addSong( song2 );

        IPlaylist sortedList = dm.sortSongsByAlbum( playlist );
        assertEquals( "Song1", sortedList.get(0).getTitle() );
        assertEquals( "Song2", sortedList.get(1).getTitle() );
    }


    @Test
    public void testSortByArtist() {
        playlist = PlaylistFactory.getEmptyPlaylist();
        playlist.addSong( song1 );
        playlist.addSong( song2 );
        playlist.addSong( song3 );
        IPlaylist sortedList = dm.sortSongsByArtist( playlist );
        assertEquals( "Song3", sortedList.get(0).getTitle() );
        assertEquals( "Song2", sortedList.get(1).getTitle() );
        assertEquals( "Song1", sortedList.get(2).getTitle() );

    }

    @Test
    public void testSortByFavoriteStatus() {
        playlist = PlaylistFactory.getEmptyPlaylist();
        playlist.addSong( song1 );
        playlist.addSong( song2 );
        playlist.addSong( song3 );
        IPlaylist sortedList = dm.sortSongsByFavoriteStatus( playlist );
        assertEquals( "Song1", sortedList.get(0).getTitle() );
        assertEquals( "Song2", sortedList.get(1).getTitle() );
        assertEquals( "Song3", sortedList.get(2).getTitle() );

    }

    @Test
    public void testSortByScore() {
        playlist = PlaylistFactory.getEmptyPlaylist();
        List<String> locations1 = new ArrayList<>();
        locations1.add( "location1");

        List<String> locations2 = new ArrayList<>();
        locations2.add( "Los Angelos");

        song1.setLocations( locations1 );
        song2.setLocations( locations2 );

        List<String> friends = new ArrayList<>();
        friends.add( "friend1" );
        song1.setListOfFriends( friends );


        friends.add( "friend2" );
        song2.setListOfFriends( friends );

        locations1.add( "location2");

        friends.add( "friend1" );
        friends.add( "friend2" );
        song1.setListOfFriends( friends );

        song1.setLocations( locations1 );
        song2.setLocations( locations2 );

        List<String> players = new ArrayList<>();
        players.add( "player1" );
        players.add( "player2" );
        song2.setListOfFriends( players );

        song1.setMostRecentlyPlayedTime( 0 );
        song2.setMostRecentlyPlayedTime( Calendar.getInstance().getTimeInMillis() );

        playlist.addSong( song1 );
        playlist.addSong( song2 );

        IPlaylist sortedList = dm.sortByScore( playlist, "location1" );
        assertEquals( "Song1", sortedList.get(0).getTitle() );
        assertEquals( "Song2", sortedList.get(1).getTitle() );
    }
}
