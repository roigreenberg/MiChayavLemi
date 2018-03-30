package com.roi.greenberg.michayavlemi.models;

import com.google.firebase.database.Exclude;

/**
 * Created by moti5321 on 15/03/2018.
 */

public class Item {
    private String name;
    private User user;
    private float price;

    public Item() {

    }

    public Item(String name, User user, float price) {
        this.name = name;
        this.user = user;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Exclude
    public String getBuyerName() {
        return user.toString();
    }
}
