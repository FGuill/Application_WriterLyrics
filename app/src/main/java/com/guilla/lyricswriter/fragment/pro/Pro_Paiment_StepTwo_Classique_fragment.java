package com.guilla.lyricswriter.fragment.pro;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.WriterAdapter;
import com.guilla.lyricswriter.fragment.Updateable;

import java.util.ArrayList;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Paiment_StepTwo_Classique_fragment extends Fragment implements Updateable, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, WriterAdapter.OnItemDonateClickListener {
    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private WriterAdapter.OnItemDonateClickListener onItemDonateClickListener;
    public static RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Group> listGroup=new ArrayList<>();
    private DatabaseHandler db;
    private String objectId;
    private WriterAdapter adapter2;
    private ImageView write;
    public static String key;
    public static boolean hasUpdate=false;
    public static Pro_Paiment_StepTwo_Classique_fragment newInstance(String key) {
        Pro_Paiment_StepTwo_Classique_fragment myFragment = new Pro_Paiment_StepTwo_Classique_fragment();
        Bundle args = new Bundle();
        args.putString("key",key);
        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_paimentclassique_pro, container, false);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        key=getArguments().getString("key");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
        onItemDonateClickListener = this;

        if (this.isAdded()){
            Initalize_RecyclerView();
        }


        return inflate;
    }

    @Override
    public void update() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    @Override
    public void onRefresh() {

    }

    public void Initalize_RecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        recyclerview.addItemDecoration(dividerItemDecoration);
        final WriterAdapter adapter2 = new WriterAdapter(getActivity(), listGroup, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
        mDatabase.child("GroupCreated").child(key).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }

    @Override
    public void onItemClick(int item, Group bo) {

    }

    @Override
    public void onSendNote(int item, Group bo) {

    }


}