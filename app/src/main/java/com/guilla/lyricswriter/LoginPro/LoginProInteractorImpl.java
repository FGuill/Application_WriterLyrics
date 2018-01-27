package com.guilla.lyricswriter.LoginPro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.BO.Business;


import static com.guilla.lyricswriter.Application.mAuth;
import static com.guilla.lyricswriter.activity.ProfilProActivity.db;


public class LoginProInteractorImpl implements LoginInteractor {

    private boolean emailVerified;
    Context context;
    private  OnLoginFinishedListener listener;
    @Override
    public void login(final Activity context, final String type, final String username, final String password, final OnLoginFinishedListener listener) {
        this.context=context;
        this.listener=listener;
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                mAuth.getInstance().signOut();

                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                   final FirebaseUser Loggeduser = mAuth.getCurrentUser();
                                    Log.d("TAG", "LoginUserWithEmail:success"+" "+ Loggeduser.getEmail());

                                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference ref = database.child("business");

                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                               Business user = singleSnapshot.getValue(Business.class);
                                                if (user.get_id().equals(Loggeduser.getUid())){
                                                    Log.d("TAG", "LoginUserWithEmail:success"+" "+ Loggeduser.getUid());

                                                    String RIB = user.get_RIB();
                                                    String owner = user.get_ower();
                                                    String Siret = user.get_Siret();
                                                    String _Businesname = user.get_businessName();
                                                    String officerName = user.get_officerName();
                                                    String address = user.get_address();
                                                    String telephoneNumber = user.get_telephoneNumber();
                                                    String email = user.get_email();
                                                    String emailVerified = user.getEmailveried();

                                                    String latitude = user.getLatitude();
                                                    String longitude = user.get_longitude();
                                                    final Business business = new Business(user.get_id(), user.get_username(), owner, officerName, _Businesname, RIB, Siret, telephoneNumber, address, latitude, longitude,email, emailVerified);
                                                    if (db.getBusinessCount() <= 0) {
                                                        db.addProProfil(business);
                                                        listener.onSuccess();
                                                    }else if (db.getBusinessCount()>=1){
                                                        db.deleteAllBusiness();
                                                        db.addProProfil(business);
                                                        listener.onSuccess();
                                                    }
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("TAG", "onCancelled", databaseError.toException());
                                        }
                                    });
                                    listener.onSuccess();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "LoginUserWithEmail:failure", task.getException());
                                }

                                // ...
                            }
                        });

                if (TextUtils.isEmpty(username)) {
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    listener.onPasswordError();
                    error = true;
                    return;
                }
            }
        }, 800);
    }


    private void ObjectId_ToPref(String ObjectId){
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("User_ObjectId", ObjectId);  // Saving objectid in preference
        editor.commit();
    }
}