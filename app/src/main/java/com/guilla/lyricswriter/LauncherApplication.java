package com.guilla.lyricswriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.activity.ActivityTutorial.MainTutorialLoginActivity;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.activity.ProfilProActivity;


/**
 * Created by Moi on 23/06/2016.
 */
public class LauncherApplication extends Activity {

  DatabaseHandler db = new DatabaseHandler(this);

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_launcher);


        if (savedInstanceState == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean previouslyStarted = prefs.getBoolean("first", false);
            if (!previouslyStarted) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean("first", Boolean.TRUE);
                edit.commit();

                startActivity(new Intent(getBaseContext(), MainTutorialLoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            } else {
                if (Application.FirebaseUser!=null){
                    if (Status(getApplicationContext()).equals("Business")) {
                        Intent intent = new Intent(getBaseContext(), ProfilProActivity.class);
                        startActivity(intent);
                        db.close();
                        finish();
                    } else if (Status(getApplicationContext()).equals("User")) {
                        Intent intent = new Intent(getBaseContext(), ProfilActivity.class);
                        startActivity(intent);
                        db.close();
                        finish();
                    }
                    else if (Status(getApplicationContext()).equals("Sharity")) {
                        Intent intent = new Intent(getBaseContext(), ProfilProActivity.class);
                        startActivity(intent);
                        db.close();
                        finish();
                    }
                } else {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                        db.close();
                        finish();
                }
            }
        }
    }

    private String Status(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("status", "");         // getting String
        return accountDisconnect;
    }
}
