package com.roi.greenberg.michayavlemi;

import java.util.ArrayList;

/**
 * Created by moti5321 on 15/03/2018.
 */

public class Item {
    private String name;
    private String buyer;
    private Long cost;

    public Item() {

    }

    public Item(String name, String user, Long cost) {
        this.name = name;
        this.buyer = user;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
