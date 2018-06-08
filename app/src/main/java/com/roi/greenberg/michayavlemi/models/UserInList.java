package com.roi.greenberg.michayavlemi.models;

/**
 * Created by greenberg on 16/03/2018.
 */

public class UserInList {
    private String type;
    private double expenses;

    public UserInList() {
    }

    public UserInList(String type, double expenses) {
        this.type = type;
        this.expenses = expenses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

//    @Override
//    public String toString() {
//        return getStringname();
//    }
}
