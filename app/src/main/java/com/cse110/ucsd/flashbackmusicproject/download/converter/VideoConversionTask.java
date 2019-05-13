package com.cse110.ucsd.flashbackmusicproject.download.converter;

import android.content.Context;
import android.os.AsyncTask;

import com.cse110.ucsd.flashbackmusicproject.download.converter.converters.ConvertioConverter;
import com.cse110.ucsd.flashbackmusicproject.download.converter.converters.VideoConversionListener;
import com.cse110.ucsd.flashbackmusicproject.download.converter.converters.VideoConverter;
import com.cse110.ucsd.flashbackmusicproject.download.MusicDownloadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 3/29/18.
 */

// TODO Pass the correct url and path when this task ends
    // TODO Add the new Converter and use it in the ctor here
public class VideoConversionTask extends AsyncTask<String, Integer, Integer> {
    Context context;
    VideoConverter videoConverter;
    String videoLink, downloadLink;
    List<VideoConversionListener> videoConversionListeners;

    public VideoConversionTask(Context context){
        this.context = context;
        videoConverter = new ConvertioConverter(context);
        downloadLink = null;
        videoLink = null;
        videoConversionListeners = new ArrayList<>();
    }

    public void setVideoConverter(VideoConverter videoConverter){
        this.videoConverter = videoConverter;
    }

    public void addVideoConversionListener(VideoConversionListener videoConversionListener){
        videoConversionListeners.add(videoConversionListener);
    }

    @Override
    protected Integer doInBackground(String... strings) {
        videoLink = strings[0];
        String filepath = context.getExternalFilesDir(MusicDownloadManager.musicDir).getPath();
        downloadLink = videoConverter.convert(videoLink, filepath);

        if(downloadLink == null){
            return -1;
        }else{
            return 1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if(integer == 1){
            notifyListenersOfSuccess();
        }else{
            notifyListenersOfFailure();
        }
    }

    private void notifyListenersOfSuccess(){
        for(VideoConversionListener videoConversionListener : videoConversionListeners){
            videoConversionListener.onVideoConversionSuccess(videoLink, downloadLink);
        }
    }

    private void notifyListenersOfFailure(){
        for(VideoConversionListener videoConversionListener : videoConversionListeners){
            videoConversionListener.onVideoConversionFailure(videoLink);
        }
    }
}
