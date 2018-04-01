package com.roi.greenberg.michayavlemi.models;

import com.google.firebase.database.Exclude;

/**
 * Created by moti5321 on 15/03/2018.
 */

public class Transaction {
    private User from;
    private User to;
    private float amount;

    public Transaction() {

    }

    public Transaction(User from, User to, float amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Exclude
    public String getFromName() {
        return from.toString();
    }

    @Exclude
    public String getToName() {
        return to.toString();
    }
}
