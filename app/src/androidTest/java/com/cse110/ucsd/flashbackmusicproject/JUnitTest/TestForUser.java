package com.cse110.ucsd.flashbackmusicproject.JUnitTest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;

import com.cse110.ucsd.flashbackmusicproject.database.IUserLoader;
import com.cse110.ucsd.flashbackmusicproject.user.User;
import com.cse110.ucsd.flashbackmusicproject.user.UserBuilder;
import com.cse110.ucsd.flashbackmusicproject.user.FriendManager;
import com.cse110.ucsd.flashbackmusicproject.user.FriendListener;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by jungwon on 3/14/18.
 */

public class TestForUser {

    private List<Person> connections;
    private MockUser user;

    private HashMap<String, List<String>> friendMap = new HashMap<>();
    private HashMap<String, List<String>> pseudoMap = new HashMap<>();

    private FriendManager friendManager;
    IUserLoader userLoader;


    String title1, title2, title3;
    String user1, user2, user3;
    String PseudoName1, PseudoName2, PseudoName3;




    @Before
    public void setup() {

        user = new MockUser();
        user.setName( "user1" );

        Person person1 = new Person();
        Person person2 = new Person();
        Person person3 = new Person();

        Name name1 = new Name();
        Name name2 = new Name();
        Name name3 = new Name();
        name1.setDisplayName( "name1" );
        name2.setDisplayName( "name2" );
        name3.setDisplayName( "name3" );

        List<Name> names1 = new ArrayList<>();
        List<Name> names2 = new ArrayList<>();
        List<Name> names3 = new ArrayList<>();

        names1.add( name1 );
        names2.add( name2 );
        names3.add( name3 );

        person1.setNames( names1 );
        person2.setNames( names2 );
        person3.setNames( names3 );

        connections = new ArrayList<>();
        connections.add( person1 );
        connections.add( person2 );
        connections.add( person3 );



    }


    @Test
    public void testOnFriendListComplete(){

        user.onFriendListComplete( connections );
        assertEquals( "name1" , user.getConnections().get(0) );
        assertEquals( "name2" , user.getConnections().get(1) );
        assertEquals( "name3" , user.getConnections().get(2) );

    }


    @Test
    public void testInsertIfFriends(){

        user1 = "friend1";
        user2 = "friend2";
        user3 = "NotFriend1";


        user.insertIfFriends(title1, user1, PseudoName1 );
        user.insertIfFriends(title2, user2, PseudoName2 );
        user.insertIfFriends(title3, user3, PseudoName3 );
        assertEquals( 3, user.getPseudoMap().get(title1).size());


        user.onFriendListComplete( connections );

        user1 = "name1";
        user.insertIfFriends(title1, user1, PseudoName1 );
        assertEquals( 1, user.getFriendMap().get(title1).size());


        user2 = "name2";
        user.insertIfFriends(title1, user1, PseudoName1 );
        assertEquals( 2, user.getFriendMap().get(title1).size());


        user.insertIfFriends(title3, user3, PseudoName3);
        assertEquals(4, user.getPseudoMap().get(title3).size());

        user3 = "name3";
        user.insertIfFriends(title3, user3, PseudoName3);
        assertEquals(3, user.getFriendMap().get(title3).size());

    }



}
