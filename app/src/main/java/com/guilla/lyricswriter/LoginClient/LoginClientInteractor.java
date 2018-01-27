package com.guilla.lyricswriter.LoginClient;

import android.app.Activity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public interface LoginClientInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onSuccess();
    }

    void loginFacebook(Activity context, OnLoginFinishedListener listener, LoginButton loginButton,CallbackManager manager);
     void loginTwitter(final Activity context, TwitterLoginButton loginButton, final OnLoginFinishedListener listener);
    }