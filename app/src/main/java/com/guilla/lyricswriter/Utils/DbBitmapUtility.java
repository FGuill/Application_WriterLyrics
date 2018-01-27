package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DbBitmapUtility {


    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    public static Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }

    public static Bitmap ResizedBitmap(Context context, Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newWidth = 5;
        int newHeight = 5;

        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                newWidth = 112;
                newHeight = 86;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                newWidth = 150;
                newHeight = 115;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                newWidth = 225;
                newHeight = 172;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                newWidth = 300;
                newHeight = 230;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                newWidth = 450;
                newHeight = 345;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                newWidth = 600;
                newHeight = 460;
                break;
        }
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    public static byte[] StringToByteArray(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return encodeByte;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}