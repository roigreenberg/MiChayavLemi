package com.roi.greenberg.michayavlemi;


public class EventDetails {

    private String name;
    private Long date;
    private String location;


    public EventDetails() {}

    public EventDetails(String name, Long date, String location) {
        this.name = name;

        this.date = date;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
