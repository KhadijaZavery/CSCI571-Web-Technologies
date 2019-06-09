package com.example.khadi.entertainmentsearch;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class TableItem {
    private String id;
    private Drawable image;
    private String name;
    private String venue;
    private String datetime;
    private Drawable favourite;
    private String category;
    private String url;
    private Date date;
    private String attractions;
   /* private String place_id;

    private boolean isFollowing;*/

    public TableItem() {
    }

    public TableItem(String name, Drawable image, String venue, String datetime, String id, Drawable favourite) {
        this.name = name;
        this.image = image;
        this.venue = venue;
        this.datetime = datetime;
        this.id = id;
        this.favourite = favourite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getVenue() {
        return venue;
    }

    public Drawable getImage() {
        return image;
    }

    public String getDatetime(){
        return datetime;
    }

  public Drawable getFavourite() {
        return favourite;
    }

    public String getUrl(){ return url; }

    public String getCategory() { return category; }

    public Date getDate(){ return date; }

    public String getAttractions(){ return attractions;}

    /* public String getPlace_id() {
        return place_id;
    }*/

    public void setName(String name) {
        this.name = name;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public void setDatetime(String  datetime){
        this.datetime = datetime;
    }

    public void setFavourite(Drawable favourite) {
        this.favourite = favourite;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCategory(String category){ this.category = category; }

    public void setDate(Date date){ this.date = date;}

    public void setAttractions(String attractions) { this.attractions = attractions; }

    /* public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setFavourite(Drawable favourite) {
        this.favourite = favourite;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }*/
}
