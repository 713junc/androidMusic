package com.cse110.ucsd.flashbackmusicproject.database;

import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.playlist.IPlaylist;
import com.cse110.ucsd.flashbackmusicproject.playlist.PlaylistFactory;
import com.cse110.ucsd.flashbackmusicproject.song.DownloadedSong;
import com.cse110.ucsd.flashbackmusicproject.song.Song;
import com.cse110.ucsd.flashbackmusicproject.user.User;
import com.cse110.ucsd.flashbackmusicproject.utility.Dictionary;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Derek on 3/5/2018.
 */

public class MyFirebaseDatabase implements IDatabase, ValueEventListener {

    private DatabaseReference database;

    private User user;

    private IUserLoader userLoader;

    private static final String TAG = "MyFirebaseDatabase";

    private ArrayList< Song > songs;

    public MyFirebaseDatabase(User user) {
        database = FirebaseDatabase.getInstance().getReference();
        this.user = user;
        songs = new ArrayList<>();
    }


    public MyFirebaseDatabase(User user, IUserLoader loader) {
        database = FirebaseDatabase.getInstance().getReference();
        this.user = user;
        this.userLoader = loader;

        database.child("users").addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (user.isGoogleUser() && !dataSnapshot.hasChild(user.getName())) {
            Log.d(TAG, "Creating First Time User");
            database.child("users").child(user.getName()).setValue(user);
        }
        userLoader.onUserLoaded();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void add(Song song) {
        if (user.isGoogleUser()) {
            database.child("users").child(user.getName()).child("songs").child(song.getTitle()).setValue(song);
        }
    }

    @Override
    public void delete(String title) {

    }

    @Override
    public int getFavoriteStatus(String title) {
        return 0;
    }

    @Override
    public List<String> getLocations(String title) {
        return null;
    }

    @Override
    public List<String> getGeneralTimes(String title) {
        return null;
    }

    @Override
    public void setFavoriteStatus(String title, int favorite) {
        if (user.isGoogleUser()) {
            database.child("users").child(user.getName()).child("songs").child(title).child("favStatus").setValue(favorite);
        }
    }

    @Override
    public void update(Song song) {
        if (user.isGoogleUser()) {
            database.child("users").child(user.getName()).child("songs").child(song.getTitle()).child("generalTimes").setValue(song.getGeneralTimes());
            database.child("users").child(user.getName()).child("songs").child(song.getTitle()).child("locations").setValue(song.getLocations());
            setFavoriteStatus(song.getTitle(), song.getFavoriteStatus());
        }
    }

    @Override
    public void load(final Song song) {
        if (user.isGoogleUser()) {
            database.child("users").child(user.getName()).child("songs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("title", song.getTitle());
                    if (!dataSnapshot.hasChild(song.getTitle())) {
                        Log.d(TAG, "Adding " + song.getTitle() + " for the first time");
                        add(song);
                    } else {
                        ArrayList<String> locations = new ArrayList<>();
                        if (dataSnapshot.child(song.getTitle()).hasChild("locations")) {
                            for (DataSnapshot data : dataSnapshot.child(song.getTitle()).child("locations").getChildren()) {
                                locations.add((String) data.getValue());
                            }
                        }
                        ArrayList<String> times = new ArrayList<>();
                        if (dataSnapshot.child(song.getTitle()).hasChild("generalTimes")) {
                            for (DataSnapshot data : dataSnapshot.child(song.getTitle()).child("generalTimes").getChildren()) {
                                times.add((String) data.getValue());
                            }
                        }
                        Log.d(TAG, song.getTitle() + " Loading Locations: " + locations.toString());
                        Log.d(TAG, song.getTitle() + " Loading times: " + times.toString());

                        try{
                            song.setFavoriteStatus(Math.toIntExact((long) dataSnapshot.child(song.getTitle()).child("favStatus").getValue()));
                        }catch (Exception e){

                        }
                        song.setGeneralTimes(times);
                        song.setLocations(locations);

                        if (times.size() > 0) {
                            song.setMostRecentTime(times.get(0));
                        } else {
                            song.setMostRecentLocation("Unknown");
                        }

                        if (locations.size() > 0) {
                            song.setMostRecentLocation(locations.get(0));
                        } else {
                            song.setMostRecentLocation("Unknown");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            song.setFavoriteStatus(Dictionary.NEUTRAL);
            song.setGeneralTimes(new ArrayList());
            song.setLocations(new ArrayList());
            song.setMostRecentLocation("Unknown");
            song.setMostRecentTime("Unknown");
        }
    }

    /*
    public static void updateSongWithDatabaseData(final Song song, Context context) {
        String username = context.getSharedPreferences(MainActivity.SHAREDPREF_NAME, context.MODE_PRIVATE).getString("name", null);
        if(username == null){
            return;
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(username).child("songs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(song.getTitle())) {
                        Log.d(TAG, "Adding " + song.getTitle() + " for the first time");
                    } else {
                        ArrayList<String> locations = new ArrayList<>();
                        if (dataSnapshot.child(song.getTitle()).hasChild("locations")) {
                            for (DataSnapshot data : dataSnapshot.child(song.getTitle()).child("locations").getChildren()) {
                                locations.add((String) data.getValue());
                            }
                        }
                        ArrayList<String> times = new ArrayList<>();
                        if (dataSnapshot.child(song.getTitle()).hasChild("generalTimes")) {
                            for (DataSnapshot data : dataSnapshot.child(song.getTitle()).child("generalTimes").getChildren()) {
                                times.add((String) data.getValue());
                            }
                        }
                        Log.d(TAG, song.getTitle() + " Loading Locations: " + locations.toString());
                        Log.d(TAG, song.getTitle() + " Loading times: " + times.toString());

                        song.setFavoriteStatus(Math.toIntExact((long) dataSnapshot.child(song.getTitle()).child("favStatus").getValue()));
                        song.setGeneralTimes(times);
                        song.setLocations(locations);

                        if (times.size() > 0) {
                            song.setMostRecentTime(times.get(0));
                        } else {
                            song.setMostRecentLocation("Unknown");
                        }

                        if (locations.size() > 0) {
                            song.setMostRecentLocation(locations.get(0));
                        } else {
                            song.setMostRecentLocation("Unknown");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }*/

    @Override
    public void getVibeSongs(final String time, final String location, final PlaylistLoadedListener playlistLoadedListener, final User profile) {
        Log.v(TAG, "Getting vibe songs");
        DatabaseReference users = database.child("users");

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Song> allSongs = new ArrayList<>();

                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    DataSnapshot userSongs = user.child("songs");

                    for (DataSnapshot songRef : userSongs.getChildren()) {

                        if (songDataSnapshotHasAllFields(songRef)) {

                            Song song = new DownloadedSong(songRef);
                            profile.insertIfFriends(song.getTitle(), user.getKey(), user.child("pseudoName").getValue().toString());

                            if (!user.getKey().equals(profile.getName())) {
                                song.setFavoriteStatus(Dictionary.NEUTRAL);
                            }

                            if(! allSongs.contains(song)){
                                allSongs.add(song);
                                song.setFriends(profile.getFriends(song.getTitle()));
                                song.setPseudos(profile.getPseudos(song.getTitle()));
                            }else{
                                // Merge list of times and locations
                                Song songInList = allSongs.get(allSongs.indexOf(song));
                                for(String location : song.getLocations()){
                                    songInList.addLocation(location);
                                }
                                for(String generalTime : song.getGeneralTimes()){
                                    songInList.addTime(generalTime);
                                }
                                if(user.getKey().equals(profile.getName())){
                                    songInList.setFavoriteStatus(song.getFavoriteStatus());
                                }else{
                                    songInList.setFavoriteStatus(Dictionary.NEUTRAL);
                                }

                                songInList.setFriends(profile.getFriends(song.getTitle()));
                                songInList.setPseudos(profile.getPseudos(song.getTitle()));
                            }


                        }
                    }
                }

                IPlaylist vibeModePlaylist = PlaylistFactory.getVibePlaylist(allSongs, location, time);

                for(Song song : vibeModePlaylist.getPlaylist()){
                    Log.v("taggy", song.getListOfFriends().toString());
                }

                Log.v("taggy", "There are " + allSongs.size() + " valid songs in DB");
                playlistLoadedListener.onPlaylistLoaded(allSongs, location, time);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean songDataSnapshotHasAllFields(DataSnapshot dataSnapshot) {
        boolean title = dataSnapshot.child("title").getValue() != null;
        boolean artist = dataSnapshot.child("artist").getValue() != null;
        boolean album = dataSnapshot.child("album").getValue() != null;
        boolean url = dataSnapshot.child("url").getValue() != null;
        boolean favoriteStatus = dataSnapshot.child("favStatus").getValue() != null;

        return title && artist && album && favoriteStatus && url;

    }

}
