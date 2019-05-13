package com.cse110.ucsd.flashbackmusicproject.user;

import com.google.api.services.people.v1.model.Person;

import java.util.List;

/**
 * Created by Derek on 3/9/2018.
 */

public interface FriendListener {

    public void onFriendListComplete(List<Person> connections);
}
