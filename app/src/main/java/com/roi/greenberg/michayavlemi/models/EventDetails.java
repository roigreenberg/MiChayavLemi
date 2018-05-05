package com.roi.greenberg.michayavlemi.models;


import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventDetails {

    private String name;
    private HashMap<String, Object> date;
    private String location;
    private Date timestamp;

    public EventDetails() {}

    public EventDetails(String name, HashMap<String, Object> date, String location) {
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

    public HashMap<String, Object> getDate() {
        return date;
    }

    public void setDate(HashMap<String, Object> date) {
        this.date = date;
    }

    @Exclude
    public long getTimeLong() {

        return (long) this.date.get("date");
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @ServerTimestamp
    public Date getTimestamp() { return timestamp; }

    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("date", date);
        result.put("location", location);

        return result;
    }
}
