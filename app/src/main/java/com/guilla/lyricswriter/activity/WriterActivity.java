package com.guilla.lyricswriter.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.fragment.client.client_Commentary_fragment;
import com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment;
import com.guilla.lyricswriter.fragment.client.client_Writer_Single_fragment;
import com.guilla.lyricswriter.fragment.client.client_Writer_fragment;

import static com.guilla.lyricswriter.activity.ProfilActivity.db;
import static com.guilla.lyricswriter.activity.ProfilActivity.permissionRuntime;
import static com.guilla.lyricswriter.fragment.client.client_Commentary_fragment.hascomment;


/**
 * Created by Moi on 07/05/2016.
 */
public class WriterActivity extends AppCompatActivity implements View.OnClickListener {


    public static WriterActivity login_activity;

    Toolbar toolBar;
    public String idkey;
    public String matchRow;
    public Group group;
    public LinearLayout save;
    public OnClickSaveListener onClickSaveListener;
    public static String typeWriter="";
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
        setContentView(R.layout.activity_hashtag);

        if (savedInstanceState == null) {
            final Bundle data = this.getIntent().getExtras();
            if (data != null) {
                idkey = data.getString("keyid");

                if (data.getSerializable("group")!= null) {
                    group = (Group) data.getSerializable("group");
                }

                if (data.getString("fragment")!=null){
                    typeWriter = (String) data.getString("fragment");
                }

            }

            db = new DatabaseHandler(this);
            login_activity = this;

            toolBar =(Toolbar) findViewById(R.id.toolbar);
            save =(LinearLayout) findViewById(R.id.save);
            save.setOnClickListener(this);
            save.setVisibility(View.VISIBLE);
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

            if (typeWriter.equalsIgnoreCase("collaboration")) {
                if (group.getGroupname()!=null) {
                    commentary_ET.setText("Collaboration" + " " + group.getGroupname());
                }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, client_Writer_fragment.newInstance(idkey,group), "client_Writer_fragment")
                        .commit();
            }else {
                if (group.getGroupname()!=null) {
                    commentary_ET.setText("Note "+group.getGroupname());
                }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, client_Writer_Single_fragment.newInstance(idkey), "client_Writer_Single_fragment")
                        .commit();
            }

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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 103: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    client_Writer_fragment titleFragmentByTag = (client_Writer_fragment) getSupportFragmentManager().findFragmentByTag("client_Writer_fragment");

                    titleFragmentByTag.generateNoteOnSD(WriterActivity.this,group.getGroupname()+".txt");

                    Log.d("Network granted", "passed");

                }else {
                    // permission was not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(WriterActivity.this, permissionRuntime.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)) {
                        Utils.showDialogPermission(WriterActivity.this,"L'application  recquiert votre autorisation pour écrire les notes","Permission",false, new Utils.Click() {
                            @Override
                            public void Ok() {
                                ActivityCompat.requestPermissions(WriterActivity.this,
                                        new String[]{permissionRuntime.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE},
                                        permissionRuntime.Code_WRITE_EXTERNAL_STORAGE);
                            }

                            @Override
                            public void Cancel() {

                            }
                        });
                    } else {

                    }
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static WriterActivity getInstance() {
        return login_activity;
    }


}



