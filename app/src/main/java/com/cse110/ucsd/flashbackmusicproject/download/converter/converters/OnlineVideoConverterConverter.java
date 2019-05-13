package com.cse110.ucsd.flashbackmusicproject.download.converter.converters;

import android.content.Context;

import com.cse110.ucsd.flashbackmusicproject.download.converter.utility.CustomHTTPConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.*;

public class OnlineVideoConverterConverter extends VideoConverter{
    private static String CONVERT_URL = "https://www2.onlinevideoconverter.com/webservice";
    private static String SUCCESS_PAGE = "https://www.onlinevideoconverter.com/success";

    public OnlineVideoConverterConverter(Context context){
        this.context = context;
    }

    @Override
    public String convert(String urlString, String downloadDirectory){
        try{
            CustomHTTPConnection customHTTPConnection = new CustomHTTPConnection(CONVERT_URL);

            customHTTPConnection.setRequestType("POST");
            addBasicPostParameters(customHTTPConnection);

            customHTTPConnection.addPostParam("function", "validate");
            customHTTPConnection.addPostParam("args[urlEntryUser]", urlString);


            String res = customHTTPConnection.makeRequest();


            res = res.substring(res.indexOf("{"));
            res = res.substring(0,res.indexOf("\n"));


            Gson gson = new GsonBuilder().create();
            JsonObject jsonObject = gson.fromJson(res, JsonObject.class);
            jsonObject = (JsonObject) jsonObject.get("result");


            String pageID = jsonObject.get("dPageId").toString().replace("\"", "");
            if(! pageID.equals("0")){
                String downloadLink =  getDownloadLinkFromSuccessPage(pageID);
                if(downloadLink == null){
                    return null;
                }

                URL downloadURL = new URL(downloadLink);
                HttpURLConnection con = (HttpURLConnection) downloadURL.openConnection();
                return downloadVideoAsMP3(con, "./", "song");
            }


            String idProcess = jsonObject.get("id_process").toString().replace("\"", "");
            String serverId = jsonObject.get("serverId").toString().replace("\"", "");
            String title = jsonObject.get("title").toString().replace("\"", "").replace(" ", "+");
            String keyHash = jsonObject.get("keyHash").toString().replace("\"", "");
            String serverUrl = jsonObject.get("serverUrl").toString().replace("\"", "");

            customHTTPConnection = new CustomHTTPConnection(CONVERT_URL);

            customHTTPConnection.setRequestType("POST");
            addBasicPostParameters(customHTTPConnection);

            customHTTPConnection.addPostParam("function", "processVideo");
            customHTTPConnection.addPostParam("args[urlEntryUser]", urlString);
            customHTTPConnection.addPostParam("args[serverId]", serverId);
            customHTTPConnection.addPostParam("args[title]", title);
            customHTTPConnection.addPostParam("args[keyHash]", keyHash);
            customHTTPConnection.addPostParam("args[serverUrl]", serverUrl);
            customHTTPConnection.addPostParam("args[id_process]", idProcess);


            res = customHTTPConnection.makeRequest();


            res = res.substring(res.indexOf("{"));
            res = res.substring(0,res.indexOf("\n"));

            jsonObject = gson.fromJson(res, JsonObject.class);
            jsonObject = (JsonObject) jsonObject.get("result");
            pageID = jsonObject.get("dPageId").toString().replace("\"", "");

            String downloadLink = getDownloadLinkFromSuccessPage(pageID);

            if(downloadLink == null){
                return null;
            }

            URL downloadURL = new URL(downloadLink);
            HttpURLConnection con = (HttpURLConnection) downloadURL.openConnection();
            return downloadVideoAsMP3(con, downloadDirectory, "download");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void addBasicPostParameters(CustomHTTPConnection customHTTPConnection){
        customHTTPConnection.addPostParam("args[dummy]", "1");
        customHTTPConnection.addPostParam("args[fromConvert]", "urlconverter");
        customHTTPConnection.addPostParam("args[requestExt]", "mp3");
        customHTTPConnection.addPostParam("args[nbRetry]", "0");
        customHTTPConnection.addPostParam("args[videoResolution]", "-1");
        customHTTPConnection.addPostParam("args[audioBitrate]", "0");
        customHTTPConnection.addPostParam("args[audioFrequency]", "0");
        customHTTPConnection.addPostParam("args[channel]", "stereo");
        customHTTPConnection.addPostParam("args[volume]", "0");
        customHTTPConnection.addPostParam("args[startFrom]", "-1");
        customHTTPConnection.addPostParam("args[endTo]", "-1");
        customHTTPConnection.addPostParam("args[custom_resx]", "-1");
        customHTTPConnection.addPostParam("args[custom_resy]", "-1");
        customHTTPConnection.addPostParam("args[advSettings]", "false");
        customHTTPConnection.addPostParam("args[aspectRatio]", "-1");


    }

    private String getDownloadLinkFromSuccessPage(String pageID) throws IOException, URISyntaxException{
        if(pageID.equals("0")){
            return null;
        }
        CustomHTTPConnection customHTTPConnection = new CustomHTTPConnection(SUCCESS_PAGE + "?id=" + pageID);
        customHTTPConnection.setRequestType("GET");

        String res = customHTTPConnection.makeRequest();


        String target = "class=\"download-button\" href=\"";
        String downloadURL = res.substring(res.indexOf(target) + target.length());
        downloadURL = downloadURL.substring(downloadURL.indexOf(target) + target.length());

        downloadURL =  downloadURL.substring(0, downloadURL.indexOf("\""));


        return downloadURL;
    }
}
