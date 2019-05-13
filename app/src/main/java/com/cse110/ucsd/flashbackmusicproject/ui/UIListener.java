package com.cse110.ucsd.flashbackmusicproject.ui;

/**
 * Created by Derek on 3/7/2018.
 */

public interface UIListener {

    public void onPlayClicked();
    public void onSkipClicked();
    public void onPrevClicked();
    public void onFavClicked();
    public void onRepeatClicked();
    public void onSeekChanged(int value);

}
