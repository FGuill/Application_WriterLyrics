package com.guilla.lyricswriter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.guilla.lyricswriter.BO.UserLocation;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.fragment.pro.Pro_LocationUserList_fragment;

import java.util.ArrayList;


/**
 * Created by Moi on 07/05/2016.
 */
public class LocationUserActivity extends AppCompatActivity {
    public static Activity Pro_Location;
    public static DatabaseHandler db;
    ArrayList<UserLocation> userLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        if (savedInstanceState == null) {
            db = new DatabaseHandler(this);
            Pro_Location=this;
            userLocations =  (ArrayList<UserLocation>)getIntent().getExtras().getBundle("Bundle").getSerializable("listuser");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, Pro_LocationUserList_fragment.newInstance(userLocations), "LocationUserList_Pro_fragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}



