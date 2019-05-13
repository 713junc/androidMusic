package com.cse110.ucsd.flashbackmusicproject.user;

import android.os.AsyncTask;

import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.List;

/**
 * Created by gSong on 2018-03-08.
 */

public class FriendManager {

    private static final String TAG = "FriendManager";

    FriendListener listener;

    public FriendManager(FriendListener listener) {
        this.listener = listener;
    }

    public void getConnections(User user) {
        new ConnectionsASyncTask().execute(user);
    }

    private class ConnectionsASyncTask extends AsyncTask<User, String, List<Person>> {
        protected List<Person> doInBackground(User... users) {
            try {

                User user = users[0];

                HttpTransport httpTransport = new NetHttpTransport();
                JacksonFactory jsonFactory = new JacksonFactory();

                String clientId = Dictionary.CLIENT_ID;
                String clientSecret = Dictionary.CLIENT_SECRET;

                // Step 2: Exchange -->
                GoogleTokenResponse tokenResponse =
                        new GoogleAuthorizationCodeTokenRequest(
                                httpTransport, jsonFactory, clientId, clientSecret, user.getAuthCode(), "")
                                .execute();
                // End of Step 2 <--

                GoogleCredential credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(clientId, clientSecret)
                        .build()
                        .setFromTokenResponse(tokenResponse);

                PeopleService peopleService =
                        new PeopleService.Builder(httpTransport, jsonFactory, credential).build();

                ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                        .setPersonFields("names")
                        .execute();

                return response.getConnections();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPreExecute() {

        }

        protected void onPostExecute(List<Person> result) {
            listener.onFriendListComplete(result);
        }

        protected void onProgressUpdate(String... text) {}

    }

}
