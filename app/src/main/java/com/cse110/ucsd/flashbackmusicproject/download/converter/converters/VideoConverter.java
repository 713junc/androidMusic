package com.cse110.ucsd.flashbackmusicproject.download.converter.converters;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public abstract class VideoConverter {
    Context context;

    public abstract String convert(String urlString, String downloadDirectory);


    String downloadVideoAsMP3(HttpURLConnection con, String localFilePath, String songTitle){
        Log.v("taggy", "Local file path: " + localFilePath);
        try{
            int contentLength = Integer.parseInt(con.getHeaderField("Content-Length"));

            InputStream inputStream = con.getInputStream();
            Log.v("taggy", "About to open file");
            String filename = songTitle + ".mp3";
            File outputFile = new File(localFilePath,filename);
            Log.v("taggy", "File opened");
            int num = 1;
            while(outputFile.exists()){
                filename = songTitle + "(" + num + ")" + ".mp3";
                outputFile = new File(localFilePath, filename);
                num++;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            Log.v("taggy", "File output stream opened");


            int bufferSize = 4096;
            byte[] data = new byte[bufferSize];
            while(contentLength > 0){
                int bytesRead = 0;
                if(contentLength >= bufferSize){
                    bytesRead = inputStream.read(data,0, bufferSize);
                    fileOutputStream.write(data, 0 , bytesRead);
                    contentLength = contentLength - bytesRead;
                }else{
                    bytesRead = inputStream.read(data,0, contentLength);
                    fileOutputStream.write(data, 0 , bytesRead);
                    contentLength = contentLength - bytesRead;
                }
            }


            return localFilePath + "/" + filename;
        } catch (FileNotFoundException e) {
            Log.v("taggy", e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.v("taggy", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.v("taggy", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }



}
