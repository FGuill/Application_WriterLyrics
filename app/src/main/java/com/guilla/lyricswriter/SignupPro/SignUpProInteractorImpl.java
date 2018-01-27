package com.guilla.lyricswriter.SignupPro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guilla.lyricswriter.BO.Business;


import static com.guilla.lyricswriter.Application.mAuth;
import static com.guilla.lyricswriter.activity.LoginActivity.db;

public class SignUpProInteractorImpl implements SignUpProInteractor {

    private Activity activity;
    private String _type;
    private String _username;
    private String _password;
    private String _Siret;
    private String _Businesname;
    private String _OwnerName;
    private String _Phone;
    private String _address;
    private String _addressfield;
    private String _RIB;
    private String _email;
    private View[] _field;
    private OnLoginFinishedListener _listener;
    private Double latitude;
    private Double longitude;

    @Override
    public void login(Activity activity,final String type, final View[] fields, Object[] addresse, final OnLoginFinishedListener listener) {

     //   View[] fields={username,password,Siret,business_name,chief_name,phone,address,RIB,email};

        this.activity=activity;
        this._type = type;
        this._username = ((EditText)fields[0]).getText().toString();
        this._password = ((EditText)fields[1]).getText().toString();
        this._Siret = ((EditText)fields[2]).getText().toString();
        this._Businesname = ((EditText)fields[3]).getText().toString();
        this._OwnerName = ((EditText)fields[4]).getText().toString();
        this._Phone = ((EditText)fields[5]).getText().toString();
        this._addressfield = ((AutoCompleteTextView)fields[6]).getText().toString();;
        this._RIB = ((EditText)fields[7]).getText().toString();
        this._email = ((EditText)fields[8]).getText().toString();

        if ((Double)addresse[0]!=null){
           this.latitude=(Double)addresse[0];
           this.longitude=(Double)addresse[1];
           this._address = (String)addresse[2];
        }
        this._listener = listener;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                _field = fields;

                boolean fieldsOK = validate(_field);
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

    @Override
    public void loginSharity(String type, Object[] fields, Object[] addresse, OnLoginFinishedListener listener) {

    }

    private void CreateUser(final String username, String password, final String email) {
        {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                ObjectId_ToPref(user.getUid());
                                DatabaseReference mDatabase;
                                final Business business=new Business(user.getUid(),username,user.getUid(),_OwnerName,_Businesname,_RIB,_Siret,_Phone,_address,String.valueOf(latitude),String.valueOf(longitude),email,"false");
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("business").child(user.getUid()).setValue(business);
                                if (db.getBusinessCount()>=1){
                                    db.deleteAllBusiness();
                                }
                                db.addProProfil(business);
                                _listener.onSuccess();
                                //Add user to localDB
                            } else {
                               // _listener.onUsernameError();
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });
        }
    }



    private void ObjectId_ToPref(String ObjectId){
        SharedPreferences pref = activity.getSharedPreferences("Pref", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("User_ObjectId", ObjectId);  // Saving objectid in preference
        editor.commit();
    }

    private String getFCMToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("TokenFireBase", "");         // getting String
        Log.d("RTOKE", accountDisconnect);
        return accountDisconnect;
    }

    private boolean validate(View[] fields) {
        for (int i = 0; i < fields.length; i++) {
            View currentField = fields[i];
            if (currentField instanceof EditText){
                EditText editText=(EditText)currentField;
                if (editText.getText().toString().length() <= 0) {
                    return false;
                }
            }else if (currentField instanceof AutoCompleteTextView){
                AutoCompleteTextView editText=(AutoCompleteTextView)currentField;
                if (editText.getText().toString().length() <= 0) {
                    return false;
                }
            }

        }
        return true;
    }
}