package com.guilla.lyricswriter.LoginPro;

import android.app.Activity;

public interface LoginPresenter {
    void validateCredentials(Activity context, String type, String username, String password);

    void onDestroy();
}