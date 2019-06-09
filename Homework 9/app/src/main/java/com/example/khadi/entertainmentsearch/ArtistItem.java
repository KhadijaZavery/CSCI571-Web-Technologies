package com.example.khadi.entertainmentsearch;

import android.util.Log;

public class ArtistItem {
    public String heading;
    public String image1, image2, image3, image4, image5, image6, image7, image8;
    public String name, popularity, followers, checkAt;

    public ArtistItem() {

    }

    public String getImage1() {
        //Log.d("From Image 1", image1);
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public String getImage4() {
        return image4;
    }

    public String getImage5() {
        return image5;
    }

    public String getImage6() {
        return image6;
    }

    public String getImage7() {
        return image7;
    }

    public String getImage8() {
        return image8;
    }

    public String getHeading(){ return heading;}

    public String getName() { return name;}

    public String getPopularity() { return popularity; }

    public String getFollowers(){ return followers;}

    public String getCheckAt() {return checkAt;}


    public void setName(String name) {
        this.name = name;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public void setPopularity(String popularity){
        this.popularity = popularity;
    }

    public void setCheckAt(String checkAt){
        this.checkAt = checkAt;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public void setImage6(String image6) {
        this.image6 = image6;
    }

    public void setImage7(String image7) {
        this.image7 = image7;
    }

    public void setImage8(String image8) {
        this.image8 = image8;
    }

    public void setHeading(String heading){
        this.heading = heading;
    }
}
