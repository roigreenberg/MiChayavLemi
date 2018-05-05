package com.roi.greenberg.michayavlemi.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by moti5321 on 15/03/2018.
 */

public class Item {
    private String name;
    private String creator;
    private String assignTo;
    private float price;
    private boolean type;
    private boolean isBought;
    private Date timestamp;

    public Item() {

    }

    public Item(String name, String creator, String assignTo, float price, boolean type, boolean isBought) {
        this.name = name;
        this.creator = creator;
        this.assignTo = assignTo;
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

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCreator() { return creator; }

    public void setCreator(String creator) { this.creator = creator; }

    public boolean isType() { return type; }

    public void setType(boolean type) { this.type = type; }

    public boolean isBought() { return isBought; }

    public void setBought(boolean isBought) { this.isBought = isBought; }

    @ServerTimestamp
    public Date getTimestamp() { return timestamp; }

    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

}
