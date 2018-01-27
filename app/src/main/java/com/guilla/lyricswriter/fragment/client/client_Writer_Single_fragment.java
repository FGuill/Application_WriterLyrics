package com.guilla.lyricswriter.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterMultipleRow;
import com.guilla.lyricswriter.Utils.AdapterTipsMultipleRow;
import com.guilla.lyricswriter.Utils.GroupAdapter;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.Utils.WriterAdapter;
import com.guilla.lyricswriter.Utils.WriterSingleAdapter;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.WriterActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.vinflater;
import static com.guilla.lyricswriter.fragment.client.client_Writer_fragment.mylyrics;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Writer_Single_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterMultipleRow.OnItemDonateClickListener, WriterActivity.OnClickSaveListener {

    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private AdapterMultipleRow.OnItemDonateClickListener onItemDonateClickListener;
    private  RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Group> listGroup=new ArrayList<>();
    private DatabaseHandler db;
    private String objectId;
    public static User user;
    public static AdapterMultipleRow adapter2;
    private ImageView write;
    public static String key;
    public static boolean hasUpdate=false;
    public static String mylyricsSingle=null;
    public static client_Writer_Single_fragment newInstance(String key) {
        client_Writer_Single_fragment myFragment = new client_Writer_Single_fragment();
        Bundle args = new Bundle();
        args.putString("key",key);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        inflate = inflater.inflate(R.layout.fragment_profile_client, container, false);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        ((WriterActivity) getActivity()).setOnClickSaveListener(client_Writer_Single_fragment.this);
        key=getArguments().getString("key");
        Log.d("totaluserbb",String.valueOf(key));

        db = new DatabaseHandler(getActivity());
        objectId = getUserObjectId(getActivity());
        user = db.getUser(objectId);
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
        onItemDonateClickListener = this;

        Initalize_RecyclerView();
        getProfilFromParse();
        return inflate;
    }



    private void getProfilFromParse() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        //if user connected via Facebook, get picture profil

        try {
        if (db.getUserCount() > 0 && Utils.isConnected(getContext())) {
            getGroupList();
        }else {
        }
            } catch (CursorIndexOutOfBoundsException e) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                swipeContainer.setRefreshing(false);
            }
    }


    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }


    /*
     * Get group list
     **/

    public void Initalize_RecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        recyclerview.addItemDecoration(dividerItemDecoration);
        adapter2 = new AdapterMultipleRow(getActivity(), listGroup, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
    }



    /*
  * Get group list
  **/

    public void getGroupList(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(user.get_id()).child("MyLyrics");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //The PUSH ID OP WANTED
                listGroup.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equalsIgnoreCase(key)) {
                        String elapsedTimeTips = "";
                        if (postSnapshot.child("timestamp").exists()) {
                            Long Tipstimestamp = (Long) postSnapshot.child("timestamp").getValue();
                            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            sfd.format(new Date(Tipstimestamp));
                            String sCertDate = sfd.format(new Date());
                            long now = new Date().getTime();
                            elapsedTimeTips = (String) DateUtils.getRelativeTimeSpanString(Tipstimestamp, now, 0);
                            Log.d("DATE", elapsedTimeTips);
                        }

                        String creatorname = null;
                        creatorname = postSnapshot.child("creatorname").getValue().toString();

                        String creatorId = postSnapshot.child("creatorid").getValue().toString();
                        String groupName = postSnapshot.child("groupname").getValue().toString();
                        String styleMusic = postSnapshot.child("styleMusic").getValue().toString();

                        if (postSnapshot.child("text").exists()) {
                            String text = postSnapshot.child("text").getValue().toString();
                            listGroup.add(new Group(postSnapshot.getKey(), text, null, creatorname, "", "", "",1));
                        } else {
                            listGroup.add(new Group(postSnapshot.getKey(), null, null, creatorname, "", "", "",1));
                        }
                   break;
                    }
                }
                listGroup.add(new Group("", "", "","", "", "", "", false, "", "","","",3));
                    adapter2.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())) {
            swipeContainer.setRefreshing(true);
            saveWriteClick();
        }else {
            swipeContainer.setRefreshing(false);
        }
    }





    @Override
    public void onItemClick(int item, Group bo) {

    }

    @Override
    public void onJoinGroupClick(int item, Group bo) {

    }

    @Override
    public void onInviteFriends(int item, Group bo) {

    }



    @Override
    public void saveWriteClick() {
        if (Utils.isConnected(getActivity())){
            if (mylyrics!=null){
                if (mylyrics.length()>0){
                    SavetoDB();
                }else {
                    Utils.showDialog3(getActivity(), Application.getContext().getResources().getString(R.string.refreshnote), Application.getContext().getResources().getString(R.string.refreshnotetitle), false, new Utils.Click() {
                        @Override
                        public void Ok() {
                            SavetoDB();
                        }

                        @Override
                        public void Cancel() {

                        }
                    });
                }
            }

        }else{
            Toast.makeText(getActivity(),getString(R.string.dialog_network),Toast.LENGTH_LONG).show();
        }
    }

    private void SavetoDB(){
        DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("user").child(user.get_id()).child("MyLyrics").child(key).child("text");
        c1v2.setValue(mylyricsSingle, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                } else {
                    Toast.makeText(getActivity(), Application.getContext().getResources().getString(R.string.savenote),Toast.LENGTH_LONG).show();
                    getProfilFromParse();
                }
            }

        });
    }
}