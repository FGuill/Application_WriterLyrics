package com.guilla.lyricswriter.LoginClient;

import android.app.Activity;
import android.content.Context;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.guilla.lyricswriter.Utils.Utils;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginClientPresenterImpl implements LoginClientPresenter, LoginClientInteractor.OnLoginFinishedListener {

    private LoginClientView loginView;
    private LoginClientInteractor loginInteractor;

    public LoginClientPresenterImpl(LoginClientView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginClientInteractorImpl();
    }

    @Override public void Login_Client(Context context, Activity activity, Object loginButton, String type, CallbackManager callbackManager) {
       /* if (loginView != null) {
            loginView.showProgress();
        }*/

        if (Utils.isConnected(context)){
            if (type.equals("twitter")){
                loginInteractor.loginTwitter(activity,(TwitterLoginButton)loginButton,this);

            }else if(type.equals("facebook")){
                loginInteractor.loginFacebook(activity,this,(LoginButton)loginButton,callbackManager);

            }
        }else {
            loginView.noNetworkConnectivity();
        }


    }

    @Override public void onDestroy() {
        loginView = null;
    }

    @Override public void onUsernameError() {
        if (loginView != null) {
            loginView.setUsernameError();
            loginView.hideProgress();
        }
    }

    @Override public void onPasswordError() {
        if (loginView != null) {
            loginView.setPasswordError();
            loginView.hideProgress();
        }
    }

    @Override public void onSuccess() {
        if (loginView != null) {
            loginView.navigateToHome();
        }
    }
}