package com.cse110.ucsd.flashbackmusicproject.download.converter.converters;

/**
 * Created by trevor on 3/29/18.
 */

public interface VideoConversionListener {
    public void onVideoConversionSuccess(String downloadLink, String localPath);
    public void onVideoConversionFailure(String failedURL);
}
