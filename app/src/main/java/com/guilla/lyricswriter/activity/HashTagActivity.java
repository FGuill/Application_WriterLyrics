package com.guilla.lyricswriter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.guilla.lyricswriter.fragment.client.client_HashTag_fragment;


/**
 * Created by Moi on 07/05/2016.
 */
public class HashTagActivity extends AppCompatActivity implements  View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private DatabaseHandler db;
    private Group group;
    private String hashtag;
    private Toolbar toolBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag);

            if (savedInstanceState == null) {
                db = new DatabaseHandler(this);
                final Bundle data = this.getIntent().getExtras();
                if (data != null) {
                    group = (Group) data.getSerializable("BOGroup");
                    hashtag = data.getString("hashTag");
                }
                toolBar =(Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolBar);
                TextView commentary_ET=(TextView)findViewById(R.id.toolbar_title);
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
                commentary_ET=(TextView)findViewById(R.id.toolbar_title);
                commentary_ET.setText(hashtag);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, client_HashTag_fragment.newInstance(group,hashtag),"client_HashTag_fragment")
                        .commit();
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
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }




    @Override
    public void onRefresh() {

    }


}





