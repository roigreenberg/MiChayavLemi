package com.roi.greenberg.michayavlemi;

import java.util.ArrayList;

/**
 * Created by moti5321 on 15/03/2018.
 */

public class Item {
    private String mProductName;
    private String mUser;
    private String mProductPrice;

    public Item() {

    }

    public Item(String productName, String user, String productPrice) {
        mProductName = productName;
        mUser = user;
        mProductPrice = productPrice;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String mUser) {
        this.mUser = mUser;
    }

    public String getProductPrice() {
        return mProductPrice;
    }

    public void setProductPrice(String mProductPrice) {
        this.mProductPrice = mProductPrice;
    }

    public static ArrayList<Item> generateDummyProductList() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Beer", "Moti", "50"));
        items.add(new Item("Ice", "Amit", "20"));
        items.add(new Item("Meet", "Roei", "250"));
        items.add(new Item("Vegetables", "Inbal", "70"));
        items.add(new Item("Soft drinks", "Ana", "120"));
        items.add(new Item("Beer", "Moti", "50"));
        items.add(new Item("Ice", "Amit", "20"));
        items.add(new Item("Meet", "Roei", "250"));
        items.add(new Item("Vegetables", "Inbal", "70"));
        items.add(new Item("Soft drinks", "Ana", "120"));
        items.add(new Item("Beer", "Moti", "50"));
        items.add(new Item("Ice", "Amit", "20"));
        items.add(new Item("Meet", "Roei", "250"));
        items.add(new Item("Vegetables", "Inbal", "70"));
        items.add(new Item("Soft drinks", "Ana", "120"));

        return items;
    }
}
