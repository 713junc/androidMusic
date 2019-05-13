package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cse110.ucsd.flashbackmusicproject.database.MyLocalDatabase;
import com.cse110.ucsd.flashbackmusicproject.song.RawSong;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.R;
import com.cse110.ucsd.flashbackmusicproject.song.TestSong;
import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;



@RunWith(AndroidJUnit4.class)
public class TestForSong{
    private MyLocalDatabase database;
    private Song song1, song2, song3;
    private Context appContext;
    private MockSong song4;
    @Before
    public void setup() {

        appContext = InstrumentationRegistry.getTargetContext();
        database = new MyLocalDatabase(appContext);
        song1 = new TestSong("song1",  "art1", "alb1", Dictionary.NEUTRAL);
        song2 = new TestSong("song2",  "art1", "alb2", Dictionary.LIKED);
        song3 = new TestSong("song3",  "art2", "alb3", Dictionary.DISLIKED);
    }



    @Test
    public void testgetTitle(){
        assertEquals("song1",song1.getTitle());
        assertEquals("song2",song2.getTitle() );
        assertEquals("song3",song3.getTitle());

    }

    @Test
    public void testgetArtist(){
        assertEquals("art1", song1.getArtist());
        assertEquals("art1", song2.getArtist());
        assertEquals("art2", song3.getArtist());
    }

    @Test
    public void testgetAlbum(){
        assertEquals("alb1", song1.getAlbum());
        assertEquals("alb2", song2.getAlbum());
        assertEquals("alb3", song3.getAlbum());
    }

    @Test
    public void testsetFavoriteStatus(){

        assertEquals(Dictionary.NEUTRAL, song1.getFavoriteStatus());
        assertEquals(Dictionary.LIKED, song2.getFavoriteStatus());
        assertEquals(Dictionary.DISLIKED, song3.getFavoriteStatus());

    }

    @Test
    public void testisLiked() {
        assertEquals( false, song1.isLiked());
        assertEquals( true, song2.isLiked());
        assertEquals( false, song3.isLiked());
    }

    @Test
    public void testisDisliked() {
        assertEquals( false, song1.isDisliked()); // false: state netrual
        assertEquals( false, song2.isDisliked()); // false: state liked
        assertEquals( true, song3.isDisliked()); //true: state disliked
    }

    @Test
    public void testgetFlashbackScore() {
        song1.setMostRecentlyPlayedTime( 0 );
        song2.setMostRecentlyPlayedTime( Calendar.getInstance().getTimeInMillis());

        List<String> locations = new ArrayList<>();
        locations.add( "location1");
        locations.add( "location2");
        locations.add( "location3");

        song1.setLocations( locations );
        song2.setLocations( locations );

        List<String> friends = new ArrayList<>();
        friends.add( "friend1");
        friends.add( "friend2");

        song1.setListOfFriends( friends );
        song2.setListOfFriends( friends );

        int score1 = song1.getFlashbackScore( "location1");
        assertEquals( 2, score1 );

        int score2 = song2.getFlashbackScore( "location1");
        assertEquals( 3, score2 );

        int score3 = song1.getFlashbackScore( "no location");
        assertEquals( 1, score3 );

        MockSong song4 = new MockSong( "different song", "", "", 0);
        song4.changeTitle( "different title");





    }

}










