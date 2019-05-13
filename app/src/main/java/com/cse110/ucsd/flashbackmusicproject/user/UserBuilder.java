package com.cse110.ucsd.flashbackmusicproject.user;

import com.cse110.ucsd.flashbackmusicproject.database.IUserLoader;

/**
 * Created by gSong on 2018-03-11.
 */

public class UserBuilder {

    private boolean buildFriends;
    private User user;
    private IUserLoader loader;

    public UserBuilder() {
        user = new User();
    }

    public UserBuilder setName(String name) {
        user.setName(name);
        return this;
    }
    public UserBuilder setEmail(String mail) {
        user.setEmail(mail);
        return this;
    }
    public UserBuilder setCode(String code) {
        user.setCode(code);
        return this;
    }
    public UserBuilder setGoogleLogin(boolean GoogleLogin) {
        user.setGoogleLogin(GoogleLogin);
        return this;
    }

    public UserBuilder buildPseudoName(String pseudoName) {
        user.setPseudoName(pseudoName);
        return this;
    }

    public UserBuilder buildFriends(IUserLoader loader) {
        this.loader = loader;
        buildFriends = true;
        return this;
    }

    public User build() {
        if (buildFriends) {
            user.updateFriends(loader);
        }
       return user;
    }
}
