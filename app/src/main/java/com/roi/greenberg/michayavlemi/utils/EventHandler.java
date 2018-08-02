package com.roi.greenberg.michayavlemi.utils;

/**
 * Created by Roi on 03/08/2018.
 */
public class EventHandler {

    private String mEventId;
    private String mEventName, mUserId, mUserName;
    private double mTotal, mAverage;
    private long numOfUsers;

    private static final EventHandler ourInstance = new EventHandler();

    public static EventHandler getInstance() {
        return ourInstance;
    }

    private EventHandler() {
    }

    public String getEventId() {
        return mEventId;
    }

    public void setEventId(String mEventId) {
        this.mEventId = mEventId;
    }

    public String getEventName() {
        return mEventName;
    }

    public void setEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public double getTotal() {
        return mTotal;
    }

    public void setTotal(double mTotal) {
        this.mTotal = mTotal;
    }

    public double getAverage() {
        return mAverage;
    }

    public void setAverage(double mAverage) {
        this.mAverage = mAverage;
    }

    public long getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(long numOfUsers) {
        this.numOfUsers = numOfUsers;
    }
}
