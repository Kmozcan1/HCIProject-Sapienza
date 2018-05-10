package com.example.onlinemarket.onlinemarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class imageTransform {

    public imageTransform() {
    }
    public static Bitmap DrawableToBitmap(Drawable drawable){
        return ((BitmapDrawable)drawable).getBitmap();
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
    public static byte[] DrawabletoByteArray(Drawable drawable){
        return getBitmapAsByteArray(DrawableToBitmap(drawable));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String byteToString(byte[] imageByte){
        String image= Base64.getEncoder().encodeToString(imageByte);
        return image;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] StringToByte(String imageString){
        byte[] image= Base64.getDecoder().decode(imageString);
        return image;
    }
    public static Bitmap getImage(byte[] data){

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String DrawabletoString(Drawable drawable){
        return byteToString(DrawabletoByteArray(drawable));
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Bitmap StringToBitmap(String string){
        return getImage(StringToByte(string));
    }
}

