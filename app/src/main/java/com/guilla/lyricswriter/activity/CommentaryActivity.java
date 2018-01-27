package com.guilla.lyricswriter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.fragment.client.client_Commentary_fragment;

import static com.guilla.lyricswriter.activity.ProfilActivity.db;
import static com.guilla.lyricswriter.fragment.client.client_Commentary_fragment.hascomment;


/**
 * Created by Moi on 07/05/2016.
 */
public class CommentaryActivity extends AppCompatActivity{


    public static CommentaryActivity login_activity;

    Toolbar toolBar;
    public String idkey;
    public String matchRow;
    public Group group;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag);

        if (savedInstanceState == null) {
            final Bundle data = this.getIntent().getExtras();
            if (data != null) {
                idkey = data.getString("keyid");

                if (data.getSerializable("group")!= null) {
                    group = (Group) data.getSerializable("group");
                }
                if (data.getString("matchRow")!= null) {
                    matchRow =  data.getString("matchRow");
                }

            }

            db = new DatabaseHandler(this);
            login_activity = this;

            toolBar =(Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolBar);
            TextView commentary_ET=(TextView)findViewById(R.id.toolbar_title);
            commentary_ET.setText(getString(R.string.comments));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left_arrow));
            toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            if (matchRow!=null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, client_Commentary_fragment.newInstanceMatchRow(idkey,group,matchRow),"client_Commentary_fragment")
                        .commit();
            }else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, client_Commentary_fragment.newInstance(idkey,group),"client_Commentary_fragment")
                        .commit();
            }

        }



    }

    @Override
    public boolean onCreateOptionsMenu(final Menu pMenu) {
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

    }





    /*
* Get group list
**/


    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent(this,ProfilActivity.class);
        returnIntent.putExtra("result",hascomment);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
        } else {
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

    public static CommentaryActivity getInstance() {
        return login_activity;
    }


}



