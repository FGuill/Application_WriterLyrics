package com.guilla.lyricswriter.LoginClient;

public interface LoginClientView {
    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateToHome();

    void noNetworkConnectivity();
}