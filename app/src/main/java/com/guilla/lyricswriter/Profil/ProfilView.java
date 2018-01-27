package com.guilla.lyricswriter.Profil;

public interface ProfilView {
    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateToHome();

    void noNetworkConnectivity();
}