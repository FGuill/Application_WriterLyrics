package com.guilla.lyricswriter.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.util.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.EditorWriter;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.BO.UserJoined;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterMultipleRow;
import com.guilla.lyricswriter.Utils.AdapterTipsMultipleRow;
import com.guilla.lyricswriter.Utils.GroupAdapter;
import com.guilla.lyricswriter.Utils.LinedEditText;
import com.guilla.lyricswriter.Utils.PermissionRuntime;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.Utils.WriterAdapter;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.activity.WriterActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.guilla.lyricswriter.R.id.illustrator_collaboration;
import static com.guilla.lyricswriter.R.id.map;
import static com.guilla.lyricswriter.R.id.swipeContainer;
import static com.guilla.lyricswriter.Utils.PermissionRuntime.Code_WRITE_EXTERNAL_STORAGE;
import static com.guilla.lyricswriter.Utils.PermissionRuntime.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.vinflater;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Writer_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, WriterAdapter.OnItemDonateClickListener, WriterActivity.OnClickSaveListener {

    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private WriterAdapter.OnItemDonateClickListener onItemDonateClickListener;
    public  RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Group> listGroup=new ArrayList<>();
    private DatabaseHandler db;
    private String objectId;
    public static User user;
    public static WriterAdapter adapter2;
    private ImageView write;
    public static String key;
    public static boolean hasUpdate=false;
    public static String mylyrics=null;
    public ArrayList<Group> getAllNote=new ArrayList<>();
    public Group group;
    public static client_Writer_fragment newInstance(String key, Group group) {
        client_Writer_fragment myFragment = new client_Writer_fragment();
        Bundle args = new Bundle();
        args.putString("key",key);
        args.putSerializable("group",group);
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

        RelativeLayout illustrator_collaboration = (RelativeLayout) inflate.findViewById(R.id.illustrator_collaboration);


        if (this==client_Writer_fragment.this){
            illustrator_collaboration.setVisibility(View.GONE);
        }
        ((WriterActivity) getActivity()).setOnClickSaveListener(client_Writer_fragment.this);
        group=(Group) getArguments().getSerializable("group");
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
        //if user connected via Facebook, get picture profil

        try {
            if (db.getUserCount() > 0 && Utils.isConnected(getContext())) {
                getGroupList();
            }else {
            } }catch (CursorIndexOutOfBoundsException e) {
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
        adapter2 = new WriterAdapter(getActivity(), listGroup, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
    }



    /*
  * Get group list
  **/

    public void getGroupList(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(key);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The PUSH ID OP WANTED
                listGroup.clear();
                int totalusers= (int) dataSnapshot.child("UsersParticiant").getChildrenCount();
                Log.d("totaluserbb",String.valueOf(totalusers));
                for (DataSnapshot snapshot : dataSnapshot.child("UsersParticiant").getChildren()) {
                     //   String id = snapshot.child("iduser").getValue().toString();
                        String name = snapshot.child("_username").getValue().toString();
                        Log.d("totaluserbb",String.valueOf(name));


                        if (snapshot.child("text").exists()) {
                            String text = snapshot.child("text").getValue().toString();
                            listGroup.add(new Group(snapshot.getKey(), text, null, name, "", "", "",1));
                        } else {
                            listGroup.add(new Group(snapshot.getKey(), null, null, name, "", "", "",1));
                        }
            }
             //   listGroup.add(new Group("", "", "","", "", "", "", false, "", "","","",3));

                hasUpdate=true;
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
    public void onSendNote(int item, final Group bo) {
        PermissionRuntime permissionRuntime = new PermissionRuntime(getActivity());
        permissionRuntime.Requestpermission(MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE, Code_WRITE_EXTERNAL_STORAGE, new PermissionRuntime.Permission() {
            @Override
            public void permissionGranted() {
                generateNoteOnSD(getActivity(),group.getGroupname()+".txt");
            }
        });
    }

    public void generateNoteOnSD(Context context, String sFileName) {
        getDataFromRecycler();
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "CollabNotes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            String newLine = System.getProperty("line.separator");
            if (getAllNote.size()>0) {
                for (int i = 0; i <getAllNote.size(); i++) {
                    if ((i % 2) == 0) {
                        writer.write(getAllNote.get(i).getCreatorname() + " : " + newLine);
                    } else {
                        writer.write(adapter2.getListItemRecycler().get(i).getGroupname() + ".  " + newLine);
                    }
                }
                writer.flush();
                writer.close();
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(key).child("UsersParticiant").child(user.get_id()).child("text");
        c1v2.setValue(mylyrics, new DatabaseReference.CompletionListener() {
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


    public void getDataFromRecycler(){
        getAllNote.clear();
        if (recyclerview.getChildCount()>0){
        for (int i = 0; i < recyclerview.getChildCount(); i++) {
            TextView creator = (TextView) recyclerview.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.name_writer);
            LinedEditText editor = (LinedEditText) recyclerview.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.editor);
            getAllNote.add(new Group(creator.getText().toString(),editor.getText().toString(),""));

        }
    }
    }

}