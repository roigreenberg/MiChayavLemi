package com.roi.greenberg.michayavlemi.utils;

import com.roi.greenberg.michayavlemi.models.User;

/**
 * Created by Roi on 03/08/2018.
 */
public class UserHandler {
    private static final UserHandler ourInstance = new UserHandler();

    public static UserHandler getInstance() {
        return ourInstance;
    }

    private UserHandler() {
    }

    private User mUser;

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }
}
