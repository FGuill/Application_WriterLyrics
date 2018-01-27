package com.guilla.lyricswriter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Category;
import com.guilla.lyricswriter.BO.CreateGroupBO;
import com.guilla.lyricswriter.BO.CreateTips;
import com.guilla.lyricswriter.BO.CreeateGroupBo;
import com.guilla.lyricswriter.BO.EditorWriter;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.BO.UserJoined;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterGridViewCategorie;
import com.guilla.lyricswriter.Utils.GroupAdapter;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.fragment.client.client_Profil_fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.guilla.lyricswriter.R.id.createGroup_BT;
import static com.guilla.lyricswriter.R.id.createGroup_ET;
import static com.guilla.lyricswriter.R.id.listusers;
import static com.guilla.lyricswriter.R.id.swipeContainer;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.gridViewCategorie;
import static com.guilla.lyricswriter.fragment.client.client_Profil_fragment.user;


/**
 * Created by Moi on 07/05/2016.
 */
public class TipsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private EditText createGroup_ET;
    private Button createGroup_BT;
    private GroupAdapter.OnItemDonateClickListener onItemDonateClickListener;
    private RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Group> listGroup=new ArrayList<>();
    private DatabaseHandler db;
    private String objectId;
    public static User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        if (savedInstanceState == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();

            db = new DatabaseHandler(this);
            objectId = getUserObjectId(this);
            user = db.getUser(objectId);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            createGroup_ET=(EditText)findViewById(R.id.createGroup_ET);
            createGroup_BT=(Button) findViewById(R.id.createGroup_BT);
            recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
            createGroup_BT.setOnClickListener(this);
            swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
            swipeContainer.setOnRefreshListener(this);

          //  swipeContainer.setOnRefreshListener(this);

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

    private void CreateGroupList() {
        final DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("GroupCreated").push();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = user.get_name();
                if (createGroup_ET.getText().toString().length()>0) {

                    CreeateGroupBo group = new CreeateGroupBo(newRef.getKey(),createGroup_ET.getText().toString(),user.get_id(),name,"Style","public",name);
                    newRef.setValue(group, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println("Data could not be saved " + databaseError.getMessage());
                            } else {
                                newRef.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                String uripicture=user.getPicurl();
                                CreateGroupBO UserSession = new CreateGroupBO(user.get_id(), user.get_name(),"",uripicture);
                                DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(newRef.getKey()).child("UsersParticiant").child(user.get_id());
                                c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                        }else {
                                            Intent returnIntent = new Intent(TipsActivity.this,ProfilActivity.class);
                                            returnIntent.putExtra("result",1);
                                            setResult(Activity.RESULT_OK,returnIntent);
                                            finish();
                                        }

                                    }
                                });
                             //   CreateEditor(newRef);
                            }
                        }
                    });
                }else {
                    Toast.makeText(TipsActivity.this,Application.getContext().getResources().getString(R.string.entergroup),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    private void CreateEditor(final DatabaseReference reference) {
        final DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(reference.getKey()).child("Editor");
        EditorWriter UserSession = new EditorWriter(user.get_id(), "cc","rr","rr","zz","20");
        c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                } else {
                    Intent returnIntent = new Intent(TipsActivity.this,ProfilActivity.class);
                    returnIntent.putExtra("result",1);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }

            }
        });
    }




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



    @Override
    public void onRefresh() {

    }


    public static final Pattern HASHTAG_PATTERN_OLD
            = Pattern.compile("#([\\p{L}\\p{InThai}$A-Za-z0-9_-]+)");


    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createGroup_BT:
                if (Utils.isConnected(TipsActivity.this)) {
                    CreateGroupList();
                }else {
                    Toast.makeText(TipsActivity.this,Application.getContext().getResources().getString(R.string.dialog_network),Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}



