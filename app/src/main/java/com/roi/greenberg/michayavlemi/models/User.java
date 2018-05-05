package com.roi.greenberg.michayavlemi.models;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by greenberg on 16/03/2018.
 */

public class User {
    private String username;
    private String userEmail;
    private String photoUrl;
    private String uid;

    public User() {
    }

    public User(@NonNull String mUsername, String mUserEmail, Uri photoUrl, String uid) {
        this.username = mUsername;
        this.userEmail = mUserEmail;
        if (photoUrl != null) {
            this.photoUrl = photoUrl.toString();
        }
        this.uid = uid;
    }

    @NonNull
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

    @Override
    public String toString() {
        return getUsername();
    }
}
