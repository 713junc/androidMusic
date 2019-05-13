package com.cse110.ucsd.flashbackmusicproject.download;

import java.util.List;

/**
 * Created by trevor on 3/14/18.
 */

public interface SongDownloadListener {
    public void onSongDownloaded(List<String> urls, List<String> paths);
}
