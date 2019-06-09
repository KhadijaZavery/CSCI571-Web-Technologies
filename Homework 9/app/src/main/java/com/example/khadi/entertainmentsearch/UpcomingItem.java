package com.example.khadi.entertainmentsearch;

import java.util.Date;

public class UpcomingItem {
    private String eventName;
    private String artistName;
    private String eventType;
    private String dateTime;
    private String url;
    private Date date;

    public UpcomingItem(){
    }

    public String getArtistName(){
        return artistName;
    }

    public String getEventName(){
        return eventName;
    }

    public String getEventType(){
        return eventType;
    }

    public String getDateTime(){
        return dateTime;
    }

    public String getUrl(){ return url; }

    public Date getDate(){ return date; }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public void setArtistName(String artistName){
        this.artistName = artistName;
    }

    public void setEventType(String eventType){
        this.eventType = eventType;
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

    public void setUrl(String url) { this.url = url; }

    public void setDate(Date date) { this.date = date; }
}
