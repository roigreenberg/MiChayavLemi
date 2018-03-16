package com.roi.greenberg.michayavlemi;

import java.util.ArrayList;

/**
 * Created by moti5321 on 15/03/2018.
 */

public class Product {
    private String mProductName;
    private String mUser;
    private String mProductPrice;

    public Product() {

    }

    public Product(String productName, String user, String productPrice) {
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

    public static ArrayList<Product> generateDummyProductList() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Beer", "Moti", "50"));
        products.add(new Product("Ice", "Amit", "20"));
        products.add(new Product("Meet", "Roei", "250"));
        products.add(new Product("Vegetables", "Inbal", "70"));
        products.add(new Product("Soft drinks", "Ana", "120"));
        products.add(new Product("Beer", "Moti", "50"));
        products.add(new Product("Ice", "Amit", "20"));
        products.add(new Product("Meet", "Roei", "250"));
        products.add(new Product("Vegetables", "Inbal", "70"));
        products.add(new Product("Soft drinks", "Ana", "120"));
        products.add(new Product("Beer", "Moti", "50"));
        products.add(new Product("Ice", "Amit", "20"));
        products.add(new Product("Meet", "Roei", "250"));
        products.add(new Product("Vegetables", "Inbal", "70"));
        products.add(new Product("Soft drinks", "Ana", "120"));

        return products;
    }
}
