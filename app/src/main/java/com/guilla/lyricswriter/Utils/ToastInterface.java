package com.guilla.lyricswriter.Utils;

import com.guilla.lyricswriter.BO.UserLocation;

public interface ToastInterface {


        void onNotificationError();

        void onNotificationSuccess(UserLocation location, String price);

    }