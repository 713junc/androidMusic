package com.cse110.ucsd.flashbackmusicproject.ui;

/**
 * Created by Derek on 3/7/2018.
 */

public interface NavDrawerListener {

    public void onDownloadedHistoryClicked();
    public void onLogOutClicked();
    public void onAlbumClicked(String album);
    public void onSongClicked(String title, String album);
    public void onNavDrawerLoaded(NavDrawerExpandableListViewAdapter adapter);
}
