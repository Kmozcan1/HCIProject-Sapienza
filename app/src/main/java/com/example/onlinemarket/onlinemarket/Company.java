package com.example.onlinemarket.onlinemarket;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.sql.Time;

public class Company {

    private String companyKey;
    private String companyName;
    private String image;
    private String openTime;
    private String closeTime;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Company(String name, Drawable newImage, String openTime, String closeTime){
        companyName= name;
        image= imageTransform.DrawabletoString(newImage);
        this.openTime= openTime;
        this.closeTime= closeTime;
    }
    public Company(String name, String newImage, String openTime, String closeTime){
        companyName= name;
        image=newImage;
        this.openTime= openTime;
        this.closeTime= closeTime;
    }
    public Company(String key, String name, String newImage, String openTime, String closeTime){
        this.companyKey = key;
        this.companyName = name;
        this.image = newImage;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
    public Company(){}

    public String getCompanyName() {
        return companyName;
    }

    public String getImage() {
        return image;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setImage(Drawable newImage) {
        this.image = imageTransform.DrawabletoString(newImage);
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
    public String calculateOpenOrClosed(){
        int hour1 = Integer.parseInt(getOpenTime().substring(0, 2));
        int minute1 = Integer.parseInt(getOpenTime().substring(3));

        int hour2 = Integer.parseInt(getCloseTime().substring(0, 2));
        int minute2 = Integer.parseInt(getCloseTime().substring(3));

        int currenthour = new Time(System.currentTimeMillis()).getHours();
        int currentminute = new Time(System.currentTimeMillis()).getMinutes();

        if(currenthour> hour1 && currenthour<hour2){
            return "Open";
        }
        else if(currenthour== hour1 && currentminute>= minute1){
            return "Open";
        }
        else if(currenthour==hour2 && currentminute<=minute2){
            return "Open";
        }
        else{
            return "Closed";
        }
    }

    @Override
    public String toString() {
        return this.companyName;
    }

}
