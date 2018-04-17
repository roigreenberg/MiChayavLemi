package com.roi.greenberg.michayavlemi.models;

import com.google.firebase.database.Exclude;

/**
 * Created by moti5321 on 15/03/2018.
 */

public class Item {
    private String name;
    private User creator;
    private User user;
    private float price;
    private boolean type;
    private boolean isBought;

    public Item() {

    }

    public Item(String name, User creator, User user, float price, boolean type, boolean isBought) {
        this.name = name;
        this.creator = creator;
        this.user = user;
        this.price = price;
        this.type = type;
        this.isBought = isBought;
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

    public User getCreator() { return creator; }

    public void setCreator(User creator) { this.creator = creator; }

    public boolean isType() { return type; }

    public void setType(boolean type) { this.type = type; }

    public boolean isBought() { return isBought; }

    public void setBought(boolean isBought) { this.isBought = isBought; }

    @Exclude
    public String getBuyerName() {
        return user.toString();
    }
}
