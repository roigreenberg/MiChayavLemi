package com.roi.greenberg.michayavlemi.models;

/**
 * Created by greenberg on 16/03/2018.
 */

public class UserInList {
    private String type;
    private int expenses;

    public UserInList() {
    }

    public UserInList(String type, int expenses) {
        this.type = type;
        this.expenses = expenses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getExpenses() {
        return expenses;
    }

    public void setExpenses(int expenses) {
        this.expenses = expenses;
    }

//    @Override
//    public String toString() {
//        return getStringname();
//    }
}
