package com.guilla.lyricswriter.ParsePushNotification;

import android.app.Activity;
import android.content.Context;

import com.google.common.net.MediaType;
import com.guilla.lyricswriter.BO.UserLocation;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.Utils.ToastInterface;


/**
 * Created by Moi on 25/03/2017.
 */

public class SendNotification {

    private Activity parentActivity;
    private UserLocation user;
    private Context context;
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private DatabaseHandler db;
    ToastInterface toastInterface;

    public SendNotification(Activity parentActivity, Context context, UserLocation userLocation, ToastInterface toastInterface){
        this.parentActivity=parentActivity;
        user=userLocation;
        this.toastInterface=toastInterface;
        this.context=context;
        db = new DatabaseHandler(context);

    }



}


