package com.guilla.lyricswriter.SignupPro;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moi on 07/04/2017.
 */

public class SignUpCharityInteractorImpl implements SignUpProInteractor {

    private String _type;
    private String _username;
    private String _password;
    private String _Siret;
    private String _Businesname;
    private String _OwnerName;
    private String _Phone;
    private String descrition;
    private String _address;
    private String _addressfield;
    private String _RIB;
    private String _email;
    private Object[] _tempfield;
    private View[] _finalfield;

    private byte[] picture;
    private OnLoginFinishedListener _listener;
    private Double latitude;
    private Double longitude;

    @Override
    public void login(Activity activity,String type, final View[] fields, Object[] addresse, OnLoginFinishedListener listener) {
    }

    @Override
    public void loginSharity(String type, final Object[] fields, Object[] addresse, OnLoginFinishedListener listener) {

        //View[] fields={_username,_password,business_name,chief_name,description,Siret,phone,address,RIB,email};
        this._type = type;
        this._username = ((EditText) fields[0]).getText().toString();
        this._password = ((EditText) fields[1]).getText().toString();
        this.picture = ((byte[]) fields[2]);
        this._Businesname = ((EditText) fields[3]).getText().toString();
        this._OwnerName = ((EditText) fields[4]).getText().toString();
        this.descrition=((EditText) fields[5]).getText().toString();
        this._Siret = ((EditText) fields[6]).getText().toString();
        this._Phone = ((EditText) fields[7]).getText().toString();
        this._addressfield = ((AutoCompleteTextView) fields[8]).getText().toString();
        this._RIB = ((EditText) fields[9]).getText().toString();
        this._email = ((EditText) fields[10]).getText().toString();

        if ((Double) addresse[0] != null) {
            this.latitude = (Double) addresse[0];
            this.longitude = (Double) addresse[1];
            this._address = (String) addresse[2];
        }
        this._listener = listener;

        _finalfield = new View[fields.length];
        List<View> numlist = new ArrayList<View>();
        for(int i= 0;i<fields.length;i++)
        {
            if(i !=2)
            {
                numlist.add((View) fields[i]);
            }
            else
            {
            }
        }
        _finalfield = numlist .toArray(new View[numlist.size()]);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                boolean fieldsOK = validate((_finalfield));
                if (fieldsOK) {
                    CreateUser(_username, _password, _email);
                } else {
                    if (TextUtils.isEmpty(_username)) {
                        _listener.onUsernameError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_password)) {
                        _listener.onPasswordError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_Siret)) {
                        _listener.onRC3Error();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_Businesname)) {
                        _listener.onBusinessNameError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_OwnerName)) {
                        _listener.onOwnerNameError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_Phone)) {
                        _listener.onPhoneError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_RIB)) {
                        _listener.onRIBError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_addressfield)) {
                        _listener.onAddressError();
                        error = true;
                        return;
                    }

                    if (TextUtils.isEmpty(_email)) {
                        _listener.onEmailError();
                        error = true;
                        return;
                    }
                }


            }
        }, 400);
    }

    private void CreateUser(String username, String password, String email) {

    }

    private void CreateBusiness() {

    }

    private void LoginPro() {

    }


    private void SaveLocationUser( String objectid) {

    }



    private boolean validate(View[] fields) {
        for (int i = 0; i < fields.length; i++) {
            View currentField = fields[i];
            if (currentField instanceof EditText) {
                EditText editText = (EditText) currentField;
                if (editText.getText().toString().length() <= 0) {
                    return false;
                }
            } else if (currentField instanceof AutoCompleteTextView) {
                AutoCompleteTextView editText = (AutoCompleteTextView) currentField;
                if (editText.getText().toString().length() <= 0) {
                    return false;
                }
            }

        }
        return true;
    }
}