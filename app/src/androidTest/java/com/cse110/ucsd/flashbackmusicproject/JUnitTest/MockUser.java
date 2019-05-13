package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.util.Log;

import com.cse110.ucsd.flashbackmusicproject.database.IUserLoader;
import com.cse110.ucsd.flashbackmusicproject.user.User;
import com.cse110.ucsd.flashbackmusicproject.user.UserBuilder;
import com.cse110.ucsd.flashbackmusicproject.user.FriendManager;
import com.cse110.ucsd.flashbackmusicproject.user.FriendListener;
import com.google.api.services.people.v1.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by jungwon on 3/14/18.
 */

public class MockUser extends User {

    private List<String> connections = new ArrayList<>();
    private HashMap<String, List<String>> friendMap = new HashMap<>();
    private HashMap<String, List<String>> pseudoMap = new HashMap<>();
    private FriendManager friendManager;

    private String name;
    IUserLoader loader;

    public MockUser() {
        friendManager = new FriendManager( this );
    }
    @Override
    public void setName( String name ) {
        this.name = name;
    }

    public List<String> getConnections() { return this.connections; }

    public HashMap<String, List<String>> getFriendMap(){ return friendMap; }
    public HashMap<String, List<String>> getPseudoMap(){ return pseudoMap; }

    public FriendManager getFriendManager() { return friendManager; }


    @Override
    public void onFriendListComplete(List<Person> connections) {
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
    }

    @Override
    public void insertIfFriends(String title, String user, String pseudoName) {

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

    @Override
    public void updateFriends(IUserLoader loader) {
        this.loader = loader;
        friendManager.getConnections(this);
    }




}
