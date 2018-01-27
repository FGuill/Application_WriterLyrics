package com.guilla.lyricswriter;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.LocalDatabase.DbBitmapUtility;
import com.guilla.lyricswriter.Utils.Utils;


import static com.guilla.lyricswriter.Application.mAuth;
import static com.guilla.lyricswriter.fragment.Login_fragment.progressBar;

/**
 * Created by Moi on 23/02/2017.
 */

public class FacebookConnectivity {
    private DatabaseHandler db;
    private User UserSession;
    private AccessToken accessToken;
    private String email;
    private String name;
    private String id;
    private byte [] byte_pictureFB;
    private Bitmap pictureFB;
    private boolean isnewUser;
    private Activity context;
    OnFBUserCreated onFBUserCreated;
    LoginButton loginButton;
    com.kinvey.java.User user;

    public interface OnFBUserCreated{
        public void OnFBUserCreated(User user);
    }
    public FacebookConnectivity(Activity context, AccessToken accessToken, com.kinvey.java.User user, LoginButton loginButton, boolean isNewUser){
        db = new DatabaseHandler(context);
        this.accessToken=accessToken;
        this.isnewUser=isNewUser;
        this.context=context;
        this.loginButton=loginButton;
        this.user=user;

    }

    public void getProfil(OnFBUserCreated onFBUserCreated) {
        this.onFBUserCreated=onFBUserCreated;
        FacebookRequest();
    }


    class ProfilePhotoAsync extends AsyncTask<String, String, String> {
        String url;

        public ProfilePhotoAsync(String url) {
            this.url = url;
        }
        @Override
        protected String doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap
            pictureFB = DownloadImageBitmap(url);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            byte_pictureFB = DbBitmapUtility.getBytes(pictureFB);
            if (getEmail()==null){
                email="";
            }
            saveUser_ParseServer(id,byte_pictureFB,name,email,isnewUser);
        }
    }

    private void saveUser_ParseServer(String FBid, byte[] profil, final String username, String email, boolean isnewUser) {
        if (progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
        Bitmap imageBit= DbBitmapUtility.getImage(profil);
        final String image = Utils.BitmapToString(imageBit);
        Log.d("TAG", image);

        Log.d("TokenSession",accessToken.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("TokenSession",user.getDisplayName());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getDisplayName())
                                    .setPhotoUri(user.getPhotoUrl())
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Write a message to the database
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                ref.child("user").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists()){
                                                            UserSession = new User(user.getUid(),id, user.getDisplayName(),user.getEmail(),image,getFCMToken(context));
                                                            ObjectId_ToPref(user.getUid());
                                                            Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                                                            hopperUpdates.put(getFCMToken(context),getFCMToken(context));
                                                            final DatabaseReference c1v1=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("notificationTokens");
                                                            c1v1.setValue(hopperUpdates);
                                                            final DatabaseReference c1v2=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("token");
                                                            c1v2.setValue(getFCMToken(context));
                                                            final DatabaseReference c1v3=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("_name");
                                                            c1v3.setValue(user.getDisplayName());
                                                            final DatabaseReference c1v4=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("_id");
                                                            c1v4.setValue(user.getUid());
                                                            db.deleteAllUser();
                                                            db.addUserProfil(UserSession);
                                                            onFBUserCreated.OnFBUserCreated(UserSession);
                                                            setIsFacebook(context,true);

                                                            Log.d("DB", "User updated");

                                                            // use "username" already exists
                                                            // Let the user know he needs to pick another username.
                                                        } else {
                                                            ObjectId_ToPref(user.getUid());
                                                            UserSession = new User(user.getUid(), id,user.getDisplayName(),user.getEmail(),image,getFCMToken(context));
                                                            DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                                                            c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                    Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                                                                    hopperUpdates.put(getFCMToken(context),getFCMToken(context));
                                                                    final DatabaseReference c1v2=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("notificationTokens");
                                                                    c1v2.setValue(hopperUpdates);

                                                                    setIsFacebook(context,true);

                                                                    if (db.getUserCount()<=0){
                                                                        db.addUserProfil(UserSession);
                                                                        onFBUserCreated.OnFBUserCreated(UserSession);

                                                                        Log.d("DB", "User Added");

                                                                    }else {
                                                                        db.deleteAllUser();
                                                                        db.addUserProfil(UserSession);
                                                                        onFBUserCreated.OnFBUserCreated(UserSession);
                                                                        Log.d("DB", "User updated");
                                                                    }
                                                                }
                                                            });
                                                            //Add user to localDB
                                                            // User does not exist. NOW call createUserWithEmailAndPassword
                                                            // Your previous code here.

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        if (progressBar!=null){
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                    }
                                                });

                                                Log.d("TAG", "User profile updated.");
                                            }
                                        }
                                    });
                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //  updateUI(null);
                        }

                    }
                });

    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id==null?"":id;
    }

    public byte[] getByte_pictureFB() {
        return byte_pictureFB;
    }

    public Bitmap getPictureFB() {
        return pictureFB;
    }

    private String getFCMToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("TokenFireBase", "novalue");         // getting String
        Log.d("RTOKE", accountDisconnect);
        return accountDisconnect;
    }


    public static Bitmap DownloadImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("IMAGE", "Error getting bitmap", e);
        }
        return bm;
    }

    private void FacebookRequest(){
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,name,picture.type(large)");
        new GraphRequest(accessToken,
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
         /* handle the result */
                        try {
                            id = response.getJSONObject().getString("id");
                            // email = response.getJSONObject().getString("email");
                            name = response.getJSONObject().getString("name");

                            JSONObject picture = response.getJSONObject().getJSONObject("picture");
                            JSONObject data = picture.getJSONObject("data");
                            String pictureUrl = data.getString("url");
                            new ProfilePhotoAsync(pictureUrl).execute();
                            Log.d("pictureUrl", pictureUrl);
                        } catch (JSONException e) {
                            Log.d("FacebookConnectivity", "Facebook error fetch data json");
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void ObjectId_ToPref(String ObjectId){
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("User_ObjectId", ObjectId);  // Saving objectid in preference
        editor.commit();
    }

    private void setIsFacebook(Context context,Boolean ObjectId){
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isFacebook", ObjectId);  // Saving objectid in preference
        editor.commit();
    }


}

