package com.cse110.ucsd.flashbackmusicproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

/**
 * MyMetaDataExtractor retrieves information like artist name, image and other stuff
 * from a song.
 */

public class MyMetaDataExtractor {

    // logging key
    public static String TAG = "Metadata Retriever";

    // instance of metadata retriever used by all methods
    private static final MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();

    private MyMetaDataExtractor () {}

    public static String getSongTitle(int resourceID, Context context) {

        // get resource path
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceID);
        metadataRetriever.setDataSource(context, uri);

        String value = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        Log.d(TAG, resourceID + " Title: " + value);
        return value == null ? "Unknown" : value;
    }

    public static String getSongTitle(String songPath) {
        try {
            metadataRetriever.setDataSource(songPath);
            return metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSongArtist(int resourceID, Context context) {

        // get resource path
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceID);
        metadataRetriever.setDataSource(context, uri);

        String value = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        Log.d(TAG, resourceID + " Artist: " + value);
        return value == null ? "Unknown" : value;
    }

    public static String getSongArtist(String songPath) {
        try {
            metadataRetriever.setDataSource(songPath);
            return metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSongAlbum(int resourceID, Context context) {

        // get resource path
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceID);
        metadataRetriever.setDataSource(context, uri);

        String value = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        Log.d(TAG, resourceID + " Album: " + value);
        return value == null ? "Unknown" : value;
    }

    public static String getSongAlbum(String songPath) {
        try {
            metadataRetriever.setDataSource(songPath);
            return metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getSongArt(int resourceID, Context context) {

        // get resource path
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceID);
        metadataRetriever.setDataSource(context, uri);

        byte[] raw = metadataRetriever.getEmbeddedPicture();

        if (raw != null) {
            Log.d(TAG, resourceID + " Album Art Found");
            return BitmapFactory.decodeByteArray(raw, 0, raw.length);
        } else {
            Log.d(TAG, resourceID + " Album Art Not Found");
            return null;
        }
    }

    public static Bitmap getSongArt(String songPath) {
        try {
            metadataRetriever.setDataSource(songPath);
            byte[] raw = metadataRetriever.getEmbeddedPicture();
            if (raw != null) {
                Log.d(TAG, songPath + " Album Art Found");
                return BitmapFactory.decodeByteArray(raw, 0, raw.length);
            } else {
                Log.d(TAG, songPath + " Album Art Not Found");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}