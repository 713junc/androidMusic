package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cse110.ucsd.flashbackmusicproject.R;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.song.TestSong;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


/**
 * Created by sseung385 on 2/15/18.
 */
@RunWith(AndroidJUnit4.class)
public class TestForLocalDatabase {
    private MockMyLocalDatabase database;
    private Song song1, song2, song3;
    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        database = new MockMyLocalDatabase( appContext );
        song1 = new TestSong( "Song1", "artist1", "album1", 0 );
        song2 = new TestSong( "Song2", "artist2", "album2", 1 );
        song3 = new TestSong( "Song3", "artist3", "album3", 2 );

    }


    @Test
    public void testAdd() {
        //Case 1: Song is not added, check if it exists in the database
        assertFalse( database.exists( "This is not added to the database"));

        //Case 2: Song is added to the database, check if it exists
        database.add( song1 );
        assertTrue( database.exists( song1.getTitle() ));

        database.add( song2);
        assertTrue( database.exists( song2.getTitle() ));

        database.add( song3);
        assertTrue( database.exists( song3.getTitle() ));

    }

    @Test
    public void testDelete() {
        // Add data to database
        database.add( song1 );
        database.add( song2 );
        database.add( song3 );


        // delete and test if they are removed from the database
        database.delete( song1.getTitle());
        assertFalse( database.exists( song1.getTitle()) );
        database.delete( song2.getTitle());
        assertFalse( database.exists( song2.getTitle()) );
        database.delete( song3.getTitle());
        assertFalse( database.exists( song3.getTitle()) );


    }

    @Test
    public void testsetFavoriteStatus() {
        // Case1: try to update favorite status to songs that are not added to database
        // should return -1 no matter what the favorite status is.
        database.setFavoriteStatus( "Not Added", 0);
        assertEquals( -1, database.getFavoriteStatus( "Not Added"));

        database.setFavoriteStatus( "Not Added2", 1);
        assertEquals( -1, database.getFavoriteStatus( "Not Added2"));

        // Case2: update favorite Status to songs that exist in the database

        // Add data to database
        database.add( song1 );
        database.add( song2 );
        database.add( song3 );

        //set Favorite Status
        database.setFavoriteStatus( song1.getTitle(), 0);
        database.setFavoriteStatus( song2.getTitle(), 1);
        database.setFavoriteStatus( song3.getTitle(), 2);

        assertEquals( 0, database.getFavoriteStatus( song1.getTitle() ));
        assertEquals( 1, database.getFavoriteStatus( song2.getTitle() ));
        assertEquals( 2, database.getFavoriteStatus( song3.getTitle() ));

    }

    @Test
    public void testsetExactTime() {
        // Case 1: Update exact time for songs not in database
        try {
            database.addExactTime("Not Added", "3:00");
        }
        catch( CursorIndexOutOfBoundsException e ) {
            assertTrue(true);
        }


        // Case 2: Update exact time for songs in database
        // Add data to database
        database.add( song1 );
        database.add( song2 );
        database.add( song3 );

        database.addExactTime("Song1", "3:00");
        database.addExactTime("Song2", "3:01");
        database.addExactTime("Song3", "3:02");

        assertEquals( "3:00", database.getExactTimes( "Song1" ).get(0));
        assertEquals( "3:01", database.getExactTimes( "Song2" ).get(0));
        assertEquals( "3:02", database.getExactTimes( "Song3" ).get(0));



    }
    @Test
    public void testsetGeneralTime() {
        // Case 1: Update general time for songs not in database
        try {
            database.addExactTime("Not Added", "3:00");
        }
        catch( CursorIndexOutOfBoundsException e ) {
            assertTrue(true);
        }


        // Case 2: Update general time for songs in database
        // Add data to database
        database.add( song1);
        database.add( song2 );
        database.add( song3);

        database.addGeneralTime("Song1", "3:00");
        database.addGeneralTime("Song2", "3:01");
        database.addGeneralTime("Song3", "3:02");

        assertEquals( "3:00", database.getGeneralTimes( "Song1" ).get(0));
        assertEquals( "3:01", database.getGeneralTimes( "Song2" ).get(0));
        assertEquals( "3:02", database.getGeneralTimes( "Song3" ).get(0));
    }

    @Test
    public void testExist() {
        database.add( song1 );
        database.add( song2 );
        assertTrue( database.exists( song1.getTitle() ));
    }

    @Test
    public void testUpdate() {
        Song song4 = new TestSong( "testUpdate", "", "", 1);
        database.load( song4 );
        assertEquals( 1, database.getFavoriteStatus( song4.getTitle() ));

    }
}
