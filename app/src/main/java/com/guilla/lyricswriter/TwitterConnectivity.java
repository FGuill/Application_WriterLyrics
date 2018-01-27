package com.guilla.lyricswriter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.AccessToken;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.LocalDatabase.DbBitmapUtility;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


import twitter4j.auth.RequestToken;

import static com.guilla.lyricswriter.FacebookConnectivity.DownloadImageBitmap;


/**
 * Created by Moi on 23/02/2017.
 */

public class TwitterConnectivity {


    private DatabaseHandler db;
    private User UserSession;
    private AccessToken accessToken;
    private String email;
    private String name;
    private String id;
    private byte [] byte_pictureFB;
    private Bitmap  pictureFB;
    private boolean isnewUser;
    private Context context;
    OnTwitterUserCreated OnTwitterUserCreated;
    private String pictureUrl;
    com.kinvey.java.User parseUser;
    private TwitterLoginButton twitterLoginButton;
    public static String consumerKey = null;
    public static String consumerSecret = null;
    public static String callbackUrl = null;
    public static String oAuthVerifier = null;
    protected static RequestToken requestToken;
    public static final int WEBVIEW_REQUEST_CODE = 100;

    public static final String Preference_Twitter = "Preference_twitter";
    public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    public static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
    public static final String PREF_USER_NAME = "twitter_user_name";
    public static final String PREF_TWITTER_USER_IMAGE = "twitter_user_image";

    private Activity activity;

    public interface OnTwitterUserCreated{
        public void UserCreated();
    }
    public TwitterConnectivity(Activity activity, com.kinvey.java.User user, TwitterLoginButton loginButton, boolean isNewUser){
        db = new DatabaseHandler(context);
        this.accessToken=accessToken;
        this.parseUser=user;
        this.isnewUser=isNewUser;
        this.activity=activity;
        this.twitterLoginButton=loginButton;
    }

    public void getProfil(OnTwitterUserCreated onFBUserCreated) {
        this.OnTwitterUserCreated=onFBUserCreated;
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                Log.d("TAG", "twitterLogin:success" + result);
             //   handleTwitterSession(result.data);
            }

            @Override
            public void failure(com.twitter.sdk.android.core.TwitterException exception) {
                Log.w("TAG", "twitterLogin:failure", exception);
             //   updateUI(null);
            }


        });
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
            saveUser_ParseServer(id,byte_pictureFB,name,email,isnewUser);
        }
    }

    private void saveUser_ParseServer(String FBid, byte[] profil,String username, String email,boolean isnewUser) {

    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }

    public byte[] getByte_pictureFB() {
        return byte_pictureFB;
    }

    public Bitmap getPictureFB() {
        return pictureFB;
    }

    private String getFCMToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("TokenFireBase", "");         // getting String
        Log.d("RTOKE", accountDisconnect);
        return accountDisconnect;
    }


    //*
    //
    //
    // /





}
