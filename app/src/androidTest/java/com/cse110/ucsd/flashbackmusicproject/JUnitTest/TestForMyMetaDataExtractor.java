package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;

import com.cse110.ucsd.flashbackmusicproject.MyMetaDataExtractor;
import com.cse110.ucsd.flashbackmusicproject.R;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
/**
 * Created by sseung385 on 2/15/18.
 */

public class TestForMyMetaDataExtractor {
    private Context context;
    boolean hasRes;
    int whenIGoID, whenYouGoID, wildernessID;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        whenIGoID = context.getResources().getIdentifier("when_i_go", "raw", context.getPackageName());
        whenYouGoID = context.getResources().getIdentifier("when_you_go", "raw", context.getPackageName());
        wildernessID = context.getResources().getIdentifier("wilderness", "raw", context.getPackageName());

        hasRes = whenIGoID != 0 && whenYouGoID != 0 && wildernessID != 0;
    }


    @Test
    public void testgetSongTitle() {
        if(!hasRes){
            return;
        }


        String whenIGo = MyMetaDataExtractor.getSongTitle( whenIGoID, context);
        String whenYouGo = MyMetaDataExtractor.getSongTitle( whenIGoID, context);
        String wilderness = MyMetaDataExtractor.getSongTitle( wildernessID, context);

        assertEquals( "When I Go", whenIGo );
        assertEquals( "When You Go", whenYouGo );
        assertEquals( "Wilderness", wilderness );

    }

    @Test
    public void testgetSongAlbum() {
        if(!hasRes){
            return;
        }

        String whenIGo = MyMetaDataExtractor.getSongAlbum( whenIGoID, context);
        String whenYouGo = MyMetaDataExtractor.getSongAlbum( whenYouGoID, context);
        String wilderness = MyMetaDataExtractor.getSongAlbum( wildernessID, context);

        assertEquals( "New & Best of Keaton Simons", whenIGo );
        assertEquals( "I Will Not Be Afraid (A Sampler)", whenYouGo );
        assertEquals( "Origins - The Best of Terry Oldfield", wilderness );
    }

    @Test
    public void testgetSongArtist() {
        if(!hasRes){
            return;
        }

        String whenIGo = MyMetaDataExtractor.getSongArtist( whenIGoID, context);
        String whenYouGo = MyMetaDataExtractor.getSongArtist( whenYouGoID, context);
        String wilderness = MyMetaDataExtractor.getSongArtist( wildernessID, context);

        assertEquals( "Keaton Simons", whenIGo );
        assertEquals( "Unknown", whenYouGo );
        assertEquals( "Terry Oldfield", wilderness );

    }
    @Test
    public void testgetSongArt() {
        if(!hasRes){
            return;
        }

        Bitmap whenIGo = MyMetaDataExtractor.getSongArt( whenIGoID, context);
        Bitmap whenYouGo = MyMetaDataExtractor.getSongArt( whenYouGoID, context);
        Bitmap wilderness = MyMetaDataExtractor.getSongArt( wildernessID, context);
        assertTrue( whenIGo != null );
        assertTrue( whenYouGo != null );
        assertTrue( wilderness != null );

    }
}

