package com.roi.greenberg.michayavlemi.models;

/**
 * Created by moti5321 on 15/03/2018.
 */

public class Transaction {
    private String from;
    private String to;
    private float amount;

    public Transaction() {

    }

    public Transaction(String from, String to, float amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
