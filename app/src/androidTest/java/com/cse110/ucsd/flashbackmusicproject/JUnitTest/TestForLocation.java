package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.location.ILocation;
import com.cse110.ucsd.flashbackmusicproject.location.MockLocation;
import com.cse110.ucsd.flashbackmusicproject.location.MockLocationProvider;
import com.cse110.ucsd.flashbackmusicproject.location.ILocation;
import com.cse110.ucsd.flashbackmusicproject.location.ILocationProvider;
import com.cse110.ucsd.flashbackmusicproject.location.MockLocation;



import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Created by sseung385 on 2/18/18.
 */

public class TestForLocation {
    private Context context;
    private MockLocationProvider provider1;
    private MockLocationProvider provider2;


    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();

        ILocation sanDiego = new MockLocation(32.881172957516185, -117.2374677658081);
        // LA
        ILocation LA = new MockLocation(34.0609876, -118.3023579);

        provider1 = new MockLocationProvider( sanDiego, context );
        provider2 = new MockLocationProvider( LA, context );
    }

    @Test
    public void testgetLocation() {
        assertEquals( "San Diego", provider1.getLocation() );
        assertEquals( "Los Angeles", provider2.getLocation() );
    }
}



