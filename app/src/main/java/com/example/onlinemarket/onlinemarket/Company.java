package com.example.onlinemarket.onlinemarket;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class Company {

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


}
