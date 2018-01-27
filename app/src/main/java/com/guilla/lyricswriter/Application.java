package com.guilla.lyricswriter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guilla.lyricswriter.BO.Group;
import com.kinvey.android.Client;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import twitter4j.Twitter;

import static com.guilla.lyricswriter.activity.LoginActivity.callbackManager;


public class Application extends android.app.Application {

    public static FirebaseAuth mAuth;
    public static FirebaseUser FirebaseUser;
    public static Twitter twitter;
    public static Client mKinveyClient;
    public static boolean isAdministrator=false;
    public static boolean isUserCreated=false;
    public static String TKFB;
    public static Application parseApplication;



    public static final String TAG = Application.class.getSimpleName();
    private static Context context;
    private static Application mInstance;
    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig("vr3RhPuuuYc2O9ICHJHypHNuJ", "vZpwlX3ny3bwkQmDfe8T1milnAmNVUINCYQuUEU52QsnBKy7WG");
        Fabric.with(this, new com.twitter.sdk.android.Twitter(authConfig));
        super.onCreate();
        context = getApplicationContext();
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        mInstance = this;
        parseApplication=this;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser = mAuth.getCurrentUser();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        MobileAds.initialize(this, "ca-app-pub-9709349127157066~8975821291");

    }




    public static synchronized Application getInstance() {
        return mInstance;
    }


    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            // new activity created; force its orientation to portrait
            activity.setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

}

