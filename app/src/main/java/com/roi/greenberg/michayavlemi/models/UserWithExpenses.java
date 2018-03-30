package com.roi.greenberg.michayavlemi.models;

/**
 * Created by greenberg on 16/03/2018.
 */

public class UserWithExpenses {
    private User details;
    private int expenses;

    public UserWithExpenses() {
    }

    public UserWithExpenses(User details, int expenses) {
        this.details = details;
        this.expenses = expenses;
    }

    public User getDetails() {
        return details;
    }

    public void setDetails(User details) {
        this.details = details;
    }

    public int getExpenses() {
        return expenses;
    }

    public void setExpenses(int expenses) {
        this.expenses = expenses;
    }

//    @Override
//    public String toString() {
//        return getUsername();
//    }
}
