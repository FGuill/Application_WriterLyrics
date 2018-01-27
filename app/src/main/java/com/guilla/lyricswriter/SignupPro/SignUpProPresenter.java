package com.guilla.lyricswriter.SignupPro;

import android.app.Activity;
import android.view.View;

public interface SignUpProPresenter {
    void validateCredentials(Activity activity,String type, View[] fields, Object[] addresse);
    void validateCredentialsSharity(String type, Object[] fields, Object[] addresse);

    void onDestroy();
}