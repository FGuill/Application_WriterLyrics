package com.guilla.lyricswriter.SignupPro;

import android.app.Activity;
import android.view.View;

public interface SignUpProInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();
        void onPasswordError();
        void onRC3Error();
        void onBusinessNameError();
        void onOwnerNameError();
        void onPhoneError();
        void onAddressError();
        void onRIBError();
        void onEmailError();

        void onSuccess();
    }

    void login(Activity activity,String type, View[] fields, Object[] addresse, OnLoginFinishedListener listener);
    void loginSharity(String type, Object[] fields, Object[] addresse, OnLoginFinishedListener listener);

}