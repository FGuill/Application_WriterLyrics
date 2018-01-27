package com.guilla.lyricswriter.LoginPro;

import android.app.Activity;

public interface LoginInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onUserError();

        void onSuccess();
    }

    void login(Activity context, String type, String username, String password, OnLoginFinishedListener listener);

}