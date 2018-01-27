package com.guilla.lyricswriter.LoginClient;

import android.app.Activity;
import android.content.Context;

import com.facebook.CallbackManager;

public interface LoginClientPresenter {
    void Login_Client(Context context, Activity activity, Object loginButton, String type, CallbackManager callbackManager);

    void onDestroy();
}