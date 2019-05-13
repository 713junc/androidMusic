package com.cse110.ucsd.flashbackmusicproject.user;

import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.database.IUserLoader;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gSong on 2018-03-11.
 */

public class User implements FriendListener{

    private String name, email, code, pseudoName;

    private FriendManager friendManager;
    private List<String> connections = new ArrayList<>();
    private HashMap<String, List<String>> friendMap = new HashMap<>();
    private HashMap<String, List<String>> pseudoMap = new HashMap<>();

    private boolean isGoogleAccount;

    private IUserLoader loader;
    private final String TAG = "User";

    public User() {
        friendManager = new FriendManager(this);
    }

    public String getName() {
        return name;
    }

    @Exclude
    public String getEmail() {
        return email;
    }

    @Exclude
    public String getAuthCode() { return code;}

    @Override
    public void onFriendListComplete(List<Person> connections) {
        Log.d(TAG, "FriendListLoaded");
        if (connections != null) {
            for (Person p : connections) {
                if(p.getNames() != null){
                    String name = p.getNames().get(0).getDisplayName();
                    if (!name.equals(this.name)) {
                        this.connections.add(name);
                    }
                }
            }
        }

        loader.onFriendsLoaded();
    }

    public List<String> getPseudos(String title){
        List<String> randoms = pseudoMap.get(title);
        return randoms != null ? randoms : new ArrayList<String>();
    }

    @Exclude
    public List<String> getFriends(String title) {
        List<String> friends = friendMap.get(title);
        return friends != null ? friends : new ArrayList<String>();
    }

    @Exclude
    public void insertIfFriends(String title, String user, String pseudoName) {

        if(user.equals(name)){
            return;
        }

        // add to friend map
        if (connections.contains(user)) {
            if (!friendMap.containsKey(title)) {
                friendMap.put(title, new ArrayList<String>());
            }
            friendMap.get(title).add(user);

            // random map
        } else {
            if (!pseudoMap.containsKey(title)) {
                pseudoMap.put(title, new ArrayList<String>());
            }
            pseudoMap.get(title).add(pseudoName);
        }
    }

    public void updateFriends(IUserLoader loader) {
        this.loader = loader;
        friendManager.getConnections(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPseudoName(String pseudoName) {
        this.pseudoName = pseudoName;
    }

    public String getPseudoName() {
        return pseudoName;
    }

    @Exclude
    public boolean isGoogleUser() {
        return isGoogleAccount;
    }

    public void setGoogleLogin(boolean isGoogleAccount) {
        this.isGoogleAccount = isGoogleAccount;
    }

    public void printLists(){
        Log.v("taggy", friendMap.values().toString());
        Log.v("taggy", pseudoMap.values().toString());

    }

}

