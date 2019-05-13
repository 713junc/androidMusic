package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import com.cse110.ucsd.flashbackmusicproject.song.TestSong;

/**
 * Created by sseung385 on 3/16/18.
 */

public class MockSong extends TestSong {
    public MockSong( String title, String artist, String album, int status) {
        super( title, artist, album, status);
    }

    public void changeTitle( String newTitle) {
        this.title = newTitle;
    }
}
