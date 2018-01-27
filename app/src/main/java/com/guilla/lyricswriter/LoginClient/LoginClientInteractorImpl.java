package com.guilla.lyricswriter.LoginClient;

import android.app.Activity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.FacebookConnectivity;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.TwitterConnectivity;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


import java.util.Arrays;
import java.util.List;



public class LoginClientInteractorImpl implements LoginClientInteractor {

    protected User UserSession;
    protected boolean error = false;
    protected boolean isnewUser=false;
    protected DatabaseHandler db;
    protected Activity mContext;
    OnLoginFinishedListener listener;

    @Override
    public void loginTwitter(final Activity context, TwitterLoginButton loginButton,final OnLoginFinishedListener listener) {


        TwitterConnectivity twitterConnectivity =new TwitterConnectivity(context, Application.mKinveyClient.user(),loginButton,false);
        twitterConnectivity.getProfil(new TwitterConnectivity.OnTwitterUserCreated() {
            @Override
            public void UserCreated() {

            }
        });
    }

    @Override
    public void loginFacebook(final Activity context, final OnLoginFinishedListener listener, final LoginButton loginButton, final CallbackManager callbackManager) {
        {
            this.listener=listener;
            final List<String> permissions = Arrays.asList("public_profile", "email");
            mContext = context;
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    FacebookConnectivity facebookConnectivity=new FacebookConnectivity(mContext,loginResult.getAccessToken(),null,loginButton,false);
                    facebookConnectivity.getProfil(new FacebookConnectivity.OnFBUserCreated() {
                        @Override
                        public void OnFBUserCreated(User user) {
                            listener.onSuccess();
                        }
                    });
                    Log.e("TAG", "success");

                }

                @Override
                public void onCancel() {
                    // App code
                    Log.e("TAG", "onCancel");
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Log.e("TAG", "error");
                }
            });

        }
    }

}

