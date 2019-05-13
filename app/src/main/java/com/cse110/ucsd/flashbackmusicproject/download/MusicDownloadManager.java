package com.cse110.ucsd.flashbackmusicproject.download;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.cse110.ucsd.flashbackmusicproject.R;
import com.cse110.ucsd.flashbackmusicproject.download.converter.VideoConversionTask;
import com.cse110.ucsd.flashbackmusicproject.download.converter.converters.VideoConversionListener;
import com.cse110.ucsd.flashbackmusicproject.song.DownloadedSong;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.utility.DownloadedSongsUtility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class MusicDownloadManager implements VideoConversionListener{
    public static String musicDir = Environment.DIRECTORY_MUSIC;


    private List<SongDownloadListener> songDownloadListeners;
    private DownloadManager downloadManager;
    private Context context;
    private BroadcastReceiver downloadedObserver;

    private long downloadReference;
    private String musicFileName;

    public String mostRecentURL;

    private String musicDirPath;
    public static final String TAG = "DownloadManager";

    private List<String> paths = new ArrayList<>();
    private List<String> recentUrls = new ArrayList<>();

    public MusicDownloadManager(Context context) {
        this.downloadManager = (DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
        this.context = context;
        this.mostRecentURL = null;
        songDownloadListeners = new ArrayList<>();
        final File musicDirFile = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        musicDirPath = musicDirFile.getPath();

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        downloadedObserver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                //long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                final Context threadContext = context;
                if(isZip(musicDirPath + "/" + musicFileName)) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(
                            context.getString(R.string.app_name), Context.MODE_PRIVATE);
                    final String zipURL= sharedPreferences.getString(musicDirPath + "/" + musicFileName, "wrong");
                    if(zipURL.equals("wrong"))
                        return;
                    Toast.makeText(context, "Zip downloaded, unpacking songs...", Toast.LENGTH_LONG).show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final List<String> songPaths;
                            if((songPaths = unpackSongs(musicDirPath, musicFileName)) == null) {
                                Activity uiActivity = (Activity) context;
                                uiActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(threadContext, "Zip unpacking failed!",
                                                Toast.LENGTH_LONG).show();
                                        boolean notAllDone = false;
                                        for (String path : paths) {
                                            File file = new File(path);
                                            if(file != null && file.exists())
                                                continue;
                                            notAllDone = true;
                                            break;

                                        }

                                        if(!notAllDone) {
                                            ArrayList<String> tempURLS = new ArrayList<>();
                                            ArrayList<String> tempPaths = new ArrayList<>();
                                            for (int i=0; i < paths.size(); i++) {
                                                if(isZip(paths.get(i))) {
                                                    tempPaths.add(paths.get(i));
                                                    tempURLS.add(recentUrls.get(i));
                                                }
                                            }
                                            paths.removeAll(tempPaths);
                                            recentUrls.removeAll(tempURLS);
                                            notifyListenersOfDownloads(recentUrls, paths);
                                        }
                                    }
                                });
                            }
                            else {
                                Activity uiActivity = (Activity) context;
                                uiActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (String songPath: songPaths) {
                                            paths.add(songPath);
                                            recentUrls.add(zipURL);
                                        }
                                        Toast.makeText(threadContext, "Zip successfully unpacked!",
                                                Toast.LENGTH_LONG).show();
                                        boolean notAllDone = false;
                                        for (String path : paths) {
                                            File file = new File(path);
                                            if(file != null && file.exists())
                                                continue;
                                            notAllDone = true;
                                            break;

                                        }

                                        if(!notAllDone) {
                                            ArrayList<String> tempURLS = new ArrayList<>();
                                            ArrayList<String> tempPaths = new ArrayList<>();
                                            for (int i=0; i < paths.size(); i++) {
                                                if(isZip(paths.get(i))) {
                                                    tempPaths.add(paths.get(i));
                                                    tempURLS.add(recentUrls.get(i));
                                                }
                                            }
                                            paths.removeAll(tempPaths);
                                            recentUrls.removeAll(tempURLS);
                                            notifyListenersOfDownloads(recentUrls, paths);
                                        }
                                    }
                                });
                            }
                        }
                    });
                    thread.start();
                }
                else {
                    boolean notAllDone = false;
                    for (String path : paths) {
                        File file = new File(path);
                        if(file != null && file.exists()) {
                            Log.v("pather", path);
                            continue;
                        }
                        notAllDone = true;
                        break;

                    }

                    if(!notAllDone) {
                        ArrayList<String> tempURLS = new ArrayList<>();
                        ArrayList<String> tempPaths = new ArrayList<>();
                        for (int i=0; i < paths.size(); i++) {
                            if(isZip(paths.get(i))) {
                                tempPaths.add(paths.get(i));
                                tempURLS.add(recentUrls.get(i));
                            }
                        }
                        paths.removeAll(tempPaths);
                        recentUrls.removeAll(tempURLS);
                        Toast.makeText(context, "Download finished!", Toast.LENGTH_LONG).show();
                        notifyListenersOfDownloads(recentUrls, paths);
                    }
                }
            }
        };
        context.registerReceiver(downloadedObserver, filter);
    }

    public void notifyListenersOfDownloads(List<String> urls, List<String> localPaths) {
        for(SongDownloadListener songDownloadListener : songDownloadListeners){
            songDownloadListener.onSongDownloaded(urls, localPaths);
        }
    }

    public BroadcastReceiver getReceiver() {
        return downloadedObserver;
    }

    public void downloadMusic(List<String> urls) {
        paths = new ArrayList<>();
        recentUrls = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            if(!URLUtil.isValidUrl(url)) {
                try {
                    urls.set(i, URLDecoder.decode(url, StandardCharsets.UTF_8.name()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (!URLUtil.isValidUrl(urls.get(i))) {
                    urls.remove(i);
                    i--;
                }
            }
        }
        new DownloadTask().execute(urls);
    }

    public void downloadMusic (String downloadURL){

        if(needsConversion(downloadURL)){
            Log.v("taggy", "Needs conversion");
            Toast.makeText(context,"Converting video to mp3", Toast.LENGTH_SHORT).show();
            VideoConversionTask videoConversionTask= new VideoConversionTask(context);
            videoConversionTask.addVideoConversionListener(this);
            videoConversionTask.execute(downloadURL);
            return;
        }
        Log.v("taggy", "No conversion needed");


        List<String> urls = new ArrayList<>();
        urls.add(downloadURL);
        downloadMusic(urls);
    }

    private boolean needsConversion(String downloadURL) {
        return downloadURL.toLowerCase().startsWith("https://www.youtube.com") ||
                downloadURL.toLowerCase().startsWith("http://www.youtube.com");
    }

    public String queueInfo() {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        long id = sharedPreferences.getLong("download_id", -1);
        
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);

        Cursor cursor = downloadManager.query(query);
        if(cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String filename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));

            if (status == DownloadManager.STATUS_RUNNING || status == DownloadManager.STATUS_PAUSED)
                return filename;
            else
                return "flashback_queue_empty";

        }
        else {
            return "flashback_queue_empty";
        }

    }

    private boolean isZip(String filePath) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(filePath);
            if (zipFile == null) {
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private List<String> unpackSongs(String filePath, String zipName) {
        List<String> songPaths = new ArrayList<>();

        InputStream inputStream;
        ZipInputStream zipInputStream;
        try {
            String musicFileName;
            inputStream = new FileInputStream(filePath + "/" + zipName);
            zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
            ZipEntry zipEntry;
            byte[] buffer = new byte[1024];
            int count;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                musicFileName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    File fmd = new File(filePath + "/" + musicFileName);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream outputStream = new FileOutputStream(filePath + "/" + musicFileName);
                while ((count = zipInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, count);
                }
                outputStream.close();
                zipInputStream.closeEntry();
                songPaths.add(filePath + "/" + musicFileName);
            }
            zipInputStream.close();
        }
        catch(IOException e) {
            Log.d("error", "error");
            return null;
        }
        return songPaths;
    }

    public String getSongFilePath(String title){
        List<Song> songs = DownloadedSongsUtility.getAllDownloadedSongs(context);
        for(Song song : songs){
            DownloadedSong dSong = (DownloadedSong) song;
            if(dSong.getTitle().equals(title)){
                return dSong.getSongPath();
            }
        }
        return null;
    }

    public void addSongDownloadListener(SongDownloadListener songDownloadListener){
        songDownloadListeners.add(songDownloadListener);
    }

    @Override
    public void onVideoConversionSuccess(String downloadLink, String localPath) {
        Toast.makeText(context,"Video was converted", Toast.LENGTH_SHORT).show();
        Log.v("taggy", downloadLink + ":" + localPath);
        List<String> urlList = new ArrayList<>();
        List<String> pathList = new ArrayList<>();

        urlList.add(downloadLink);
        pathList.add(localPath);

        notifyListenersOfDownloads(urlList, pathList);
    }

    @Override
    public void onVideoConversionFailure(String failedURL) {
        Toast.makeText(context,"Video could not be converted", Toast.LENGTH_SHORT).show();
    }

    private class DownloadTask extends AsyncTask<List<String>, String, String> {

        protected String doInBackground(List<String>... songs) {

            for (int j = 0; j < songs[0].size(); j++) {

                String downloadURL = songs[0].get(j);

                if (downloadURL.endsWith("/")) {
                    char[] urlDivide = downloadURL.toCharArray();
                    int illegalSlashIndex = urlDivide.length;
                    for (int i = urlDivide.length - 1; i >= 0; i--) {
                        if (urlDivide[i] != '/')
                            illegalSlashIndex = i;
                    }
                    downloadURL = downloadURL.substring(0, illegalSlashIndex);
                }

                try {
                    recentUrls.add(URLEncoder.encode(downloadURL, StandardCharsets.UTF_8.name()));

                    Uri downloadURI = Uri.parse(downloadURL);

                    DownloadManager.Request request = new DownloadManager.Request(downloadURI);
                    String[] urlComp = downloadURI.getPath().split("/");
                    request.setTitle(urlComp[urlComp.length - 1]);

                    musicFileName = URLUtil.guessFileName(downloadURL, null, null);
                    musicFileName = (musicFileName != null) ? musicFileName : urlComp[urlComp.length - 1];
                    request.setDestinationInExternalFilesDir(context, musicDir, musicFileName);

                    downloadReference = downloadManager.enqueue(request);

                    SharedPreferences sharedPreferences = context.getSharedPreferences(
                            context.getString(R.string.app_name), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("download_id", downloadReference);
                    editor.putString(musicDirPath + "/" + musicFileName, downloadURL);
                    editor.putBoolean(musicDirPath + "/" + musicFileName + "-bool", false);
                    editor.apply();

                    paths.add(musicDirPath + "/" + musicFileName);
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    mostRecentURL = null;
                }
            }

            return "";
        }

        protected void onPreExecute() {

        }

        protected void onPostExecute(String result) {

        }

        protected void onProgressUpdate(String... text) {}
    }
}