package com.guilla.lyricswriter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.fragment.client.client_Writer_fragment;
import com.guilla.lyricswriter.fragment.client.client_invitecollaboration_fragment;

import static com.guilla.lyricswriter.activity.ProfilActivity.db;


/**
 * Created by Moi on 07/05/2016.
 */
public class InviteCollabActivity extends AppCompatActivity implements View.OnClickListener {


    public static InviteCollabActivity login_activity;

    Toolbar toolBar;
    public String idkey;
    public String matchRow;
    public Group group;
    public LinearLayout save;
    public OnClickSaveListener onClickSaveListener;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save:
                onClickSaveListener.saveWriteClick();
                break;
        }
    }

    public interface OnClickSaveListener{
        public void saveWriteClick();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab);

        if (savedInstanceState == null) {
            final Bundle data = this.getIntent().getExtras();
            if (data != null) {
                idkey = data.getString("keyid");

                if (data.getSerializable("group")!= null) {
                    group = (Group) data.getSerializable("group");
                }


            }

            db = new DatabaseHandler(this);
            login_activity = this;

            toolBar =(Toolbar) findViewById(R.id.toolbar);
            save =(LinearLayout) findViewById(R.id.save);
            save.setOnClickListener(this);
            save.setVisibility(View.VISIBLE);
            save.setClickable(true);
            setSupportActionBar(toolBar);
            TextView commentary_ET=(TextView)findViewById(R.id.toolbar_title);
            commentary_ET.setText("Invite Collaboration");
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


                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, client_invitecollaboration_fragment.newInstance(idkey,group),"client_invitecollaboration_fragment")
                        .commit();

        }



    }

    public void setOnClickSaveListener(OnClickSaveListener onClickSaveListener){
        this.onClickSaveListener=onClickSaveListener;
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

    public static InviteCollabActivity getInstance() {
        return login_activity;
    }


}



