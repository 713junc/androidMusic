package com.cse110.ucsd.flashbackmusicproject.download.converter.converters;

import android.content.Context;
import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.download.converter.utility.CustomHTTPConnection;
import com.google.gson.*;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class ConvertioConverter extends VideoConverter{
    private static final String BASE_URL = "https://www.converto.io/ajax.php";
    //private static final String CREATE_URL = "http://s11.converto.io/en/create_url";
    //private static final String DOWNLOAD_PAGE = "http://s11.converto.io";
    //private static final String DOWNLOAD_BASE = "http://s11.converto.io/download-file/";
    private static final String DOWNLOAD_SUFFIX = "/file.mp3";

    private static String serverName;

    public ConvertioConverter(Context context){
        this.context = context;
    }

    @Override
    public String convert(String urlString, String downloadDirectory) {
        try{
            Log.v("taggy", "Starting conversion");
            CustomHTTPConnection httpConnection = new CustomHTTPConnection(getGeneralBaseUrl());
            httpConnection.setRequestType("POST");
            JsonObject dataObject = new JsonObject();
            dataObject.addProperty("url", urlString);
            dataObject.addProperty("action", "checkYoutubeUrl2");

            httpConnection.addPostParam("data", dataObject.toString());

            httpConnection.makeRequest();
            Log.v("taggy", "First post made");

            String res = httpConnection.getBody();

            Gson gson = new GsonBuilder().create();
            JsonObject jsonObject = gson.fromJson(res, JsonObject.class);

            String urlID = jsonObject.get("url_id").toString().replace("\"", "");
            String filename = jsonObject.get("filename").toString().replace("\"", "");


            httpConnection = new CustomHTTPConnection(getGeneralBaseUrl());
            httpConnection.setRequestType("POST");
            dataObject = new JsonObject();
            dataObject.addProperty("id", urlID);
            dataObject.addProperty("action", "getServerForConvert2");
            httpConnection.addPostParam("data", dataObject.toString());

            httpConnection.makeRequest();
            res = httpConnection.getBody();

            jsonObject = gson.fromJson(res, JsonObject.class);

            JsonObject infoObject = jsonObject.getAsJsonObject("info");

            serverName = jsonObject.get("server").toString().replace("\"", "");
            serverName = serverName.substring(0, serverName.indexOf("."));

            httpConnection = new CustomHTTPConnection(getCreateUrl());
            httpConnection.setRequestType("POST");

            Set<Map.Entry<String, JsonElement>> entries = infoObject.entrySet();
            for(Map.Entry entry : entries){
                String key = entry.getKey().toString();
                String value = entry.getValue().toString().replace("\"", "");
                httpConnection.addPostParam(key, value);
            }

            res = httpConnection.makeRequest();

            String location = httpConnection.getResponseHeader("location");
            String downloadPageID = location.substring(location.indexOf("id=") + 3);

            httpConnection = new CustomHTTPConnection(getSpecificBaseUrl());
            httpConnection.setRequestType("POST");
            dataObject = new JsonObject();
            dataObject.addProperty("url_id", downloadPageID);
            dataObject.addProperty("action", "checkExists");

            httpConnection.addPostParam("data", dataObject.toString());

            httpConnection.makeRequest();
            res = httpConnection.getBody();


            httpConnection = new CustomHTTPConnection(getDownloadPage() + location);
            httpConnection.setRequestType("GET");

            boolean isDownloaded = false;
            String prevPercent = "0";

            while(!isDownloaded){
                dataObject = new JsonObject();
                dataObject.addProperty("url_id", downloadPageID);
                dataObject.addProperty("prev_percent", prevPercent);
                dataObject.addProperty("action", "getPercent");

                httpConnection = new CustomHTTPConnection(getSpecificBaseUrl());
                httpConnection.setRequestType("POST");
                httpConnection.setHeader("Referer", getSpecificBaseUrl() + location);

                httpConnection.addPostParam("data", dataObject.toString());
                httpConnection.makeRequest();

                res = httpConnection.getBody();

                JsonObject responseObject = gson.fromJson(res, JsonObject.class);
                prevPercent = responseObject.get("percent").toString();

                isDownloaded = prevPercent.equals("100");
            }


            String downloadLink = getDownloadBase() + downloadPageID + DOWNLOAD_SUFFIX;
            URL downloadURL = new URL(downloadLink);
            HttpURLConnection con = (HttpURLConnection)downloadURL.openConnection();
            con.setRequestProperty("Referer", BASE_URL + location);

            Log.v("taggy", "About to download");
            return downloadVideoAsMP3(con, downloadDirectory, filename);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        return null;
    }



    private String getServerName(){
        return serverName;
    }

    private String getDownloadBase(){
        return "http://" + serverName + ".converto.io/download-file/";
    }

    private String getCreateUrl(){
        return "http://" + serverName + ".converto.io/en/create_url";
    }

    private String getGeneralBaseUrl(){
        return BASE_URL;
    }

    private String getSpecificBaseUrl(){
        return "http://" + getServerName() + ".converto.io/ajax.php";
    }

    private String getDownloadPage(){
        return "http://"+ getServerName() + ".converto.io";
    }
}
