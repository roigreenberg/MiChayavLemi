package com.roi.greenberg.michayavlemi;


public class EventDetails {

    private String mNameEvent;
    private Long mDate;
    private String mLocation;


    public EventDetails() {}

    public EventDetails(String nameEvent, Long date, String location) {
        mNameEvent = nameEvent;

        mDate = date;
        mLocation = location;
    }

    public String getNameEvent() {
        return mNameEvent;
    }

    public void setNameEvent(String nameEvent) {
        mNameEvent = nameEvent;
    }

    public Long getDate() {
        return mDate;
    }

    public void setDate(Long date) {
        mDate = date;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
