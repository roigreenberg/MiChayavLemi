package com.roi.greenberg.michayavlemi;

import android.net.Uri;

/**
 * Created by greenberg on 16/03/2018.
 */

public class User {
    private String username;
    private String userEmail;
    private String photoUrl;
    private String uid;

    private EventDetails events;

    public User() {
    }

    User(String mUsername, String mUserEmail, Uri photoUrl, String uid) {
        this.username = mUsername;
        this.userEmail = mUserEmail;
        this.photoUrl = photoUrl.toString();
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String mUsername) {
        this.username = mUsername;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String mUserEmail) {
        this.userEmail = mUserEmail;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public EventDetails getEvents() {
        return events;
    }

    public void setEvents(EventDetails events) {
        this.events = events;
    }
}
