package com.guilla.lyricswriter.fragment.client;


import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.Like;
import com.guilla.lyricswriter.BO.Repost;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterTipsMultipleRow;
import com.guilla.lyricswriter.Utils.DbBitmapUtility;
import com.guilla.lyricswriter.Utils.GroupAdapter;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.CommentaryActivity;
import com.guilla.lyricswriter.activity.DetailGroupActivity;
import com.guilla.lyricswriter.activity.HashTagActivity;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.activity.TipsUserProfilActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.guilla.lyricswriter.activity.ProfilActivity.user;
import static com.guilla.lyricswriter.fragment.client.client_Profil_fragment.listParticiant;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Container_Mission_fragment extends Fragment implements Updateable, View.OnClickListener, GroupAdapter.OnItemDonateClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private GroupAdapter.OnItemDonateClickListener onItemDonateClickListener;
    private RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Group> listGroup = new ArrayList<>();
    private DatabaseHandler db;
    private GroupAdapter adapter2;
    public static client_Container_Mission_fragment login_activity;

    private TextView nbabonnement;
    private TextView nbabonnes;
    private TextView username;
    private TextView nbpublication;
    private   CircleImageView icon;
    private String idkey;
    private   Button abonneBT;
    private Group group;
    private  boolean Currently_followingUser = false;
    private User currentUser= ProfilActivity.user;
    private LottieAnimationView animation_nonetwork;
    private   RelativeLayout frame_nonetwork;
    private  LinearLayout reconnect;
    private RelativeLayout frame_expand;
    private LinearLayout no_item;
    public Boolean  PagerDisasterSet=false;

    public static client_Container_Mission_fragment newInstance(Group group) {
        client_Container_Mission_fragment myFragment = new client_Container_Mission_fragment();
        Bundle args = new Bundle();
        args.putSerializable("group",group);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_mission, container, false);

        if (savedInstanceState == null) {
            if (getArguments().getSerializable("group")!=null){
                group=(Group)getArguments().getSerializable("group");
            }

            no_item =(LinearLayout) inflate.findViewById(R.id.no_item);
            frame_expand = (RelativeLayout) inflate.findViewById(R.id.frame_expand);
            recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
            frame_nonetwork = (RelativeLayout) inflate.findViewById(R.id.frame_nonetwork);
            frame_nonetwork.setOnClickListener(this);
            reconnect = (LinearLayout) inflate.findViewById(R.id.reconnect);
            animation_nonetwork = (LottieAnimationView) inflate.findViewById(R.id.animation_nonetwork);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            nbabonnement = (TextView) inflate.findViewById(R.id.nbabonnement);
            nbpublication = (TextView) inflate.findViewById(R.id.nbpublication);
            nbabonnes = (TextView) inflate.findViewById(R.id.nbabonnes);
            username = (TextView) inflate.findViewById(R.id.username);
            icon=(CircleImageView)inflate.findViewById(R.id.logo);
            recyclerview = (RecyclerView) inflate.findViewById(R.id.recyclerview);
            swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
            swipeContainer.setOnRefreshListener(this);
            onItemDonateClickListener = this;

            db = new DatabaseHandler(getActivity());
            login_activity = this;

            if (getActivity() instanceof ProfilActivity){
                Bitmap PictureProfile=null;
                String images = user.getPicurl();
                byte[] image = DbBitmapUtility.StringToByteArray(images);
                PictureProfile = DbBitmapUtility.getImage(image);
                icon.setImageBitmap(PictureProfile);
                username.setText(user.get_name());

                InitRecyclerView();
            }

        }
        return inflate;
    }


    // Callback open map when user click open map.


    public void getProfilFromParse() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        try {
        if (db.getUserCount() > 0){
            if (Utils.isConnected(getContext())){
                PagerDisasterSet=true;
                Check_ABBONNEMENT(user);
                ShowPublication();

            }else{
                ShowNetworkView();
            }
        }
        }catch (CursorIndexOutOfBoundsException e) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            swipeContainer.setRefreshing(false);
        }
    }

    private void InitRecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        adapter2 = new GroupAdapter(getActivity(), listGroup, onItemDonateClickListener);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerview.setAdapter(adapter2);
    }

    public void ShowPublication() {
        getGroupList();

    }

    private void getGroupList() {
        FirebaseDatabase.getInstance().getReference().child("GroupCreated").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The PUSH ID OP WANTED
                listGroup.clear();
                listParticiant.clear();
                if (dataSnapshot.getChildrenCount()>0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String creatorId = postSnapshot.child("creatorid").getValue().toString();

                        if (creatorId.equalsIgnoreCase(user.get_id()) || postSnapshot.child("UsersParticiant").child(user.get_id()).exists()) {
                            String coutuser = Long.toString(postSnapshot.child("UsersParticiant").getChildrenCount());

                            listParticiant = new ArrayList<Group>();
                            for (DataSnapshot snapshot : postSnapshot.child("UsersParticiant").getChildren()) {
                                String _iduser = snapshot.child("_iduser").getValue().toString();
                                String _picprofil = snapshot.child("_picprofil").getValue().toString();
                                String _username = snapshot.child("_username").getValue().toString();
                                listParticiant.add(new Group(snapshot.getKey(), _iduser, _username, _picprofil));
                            }

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
                            String groupName = postSnapshot.child("groupname").getValue().toString();
                            String creatorname = postSnapshot.child("creatorname").getValue().toString();
                            String styleMusic = postSnapshot.child("styleMusic").getValue().toString();
                            String privateorpublic = postSnapshot.child("privateorpublic").getValue().toString();
                            String listusers = postSnapshot.child("listusers").getValue().toString();
                            listGroup.add(new Group(postSnapshot.getKey(), groupName, creatorId, creatorname, styleMusic, privateorpublic, coutuser, elapsedTimeTips, listParticiant,0));
                        }
                    }
                }else{
                }
                adapter2.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
                HideNetworkView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void ShowScreenNoData(){
        frame_expand.setVisibility(View.INVISIBLE);
        no_item.setVisibility(View.VISIBLE);
    }
    public void HideSCreenNoData(){
        frame_expand.setVisibility(View.VISIBLE);
        no_item.setVisibility(View.INVISIBLE);
    }


    public void ShowLoading(){
        frame_expand.setVisibility(View.INVISIBLE);
        frame_nonetwork.setVisibility(View.VISIBLE);
        animation_nonetwork.setVisibility(View.VISIBLE);
        reconnect.setVisibility(View.INVISIBLE);
        animation_nonetwork.setAnimation("data2.json");
        animation_nonetwork.loop(true);
        animation_nonetwork.playAnimation();
    }


    public void ShowNetworkView(){
        frame_expand.setVisibility(View.INVISIBLE);
        frame_nonetwork.setVisibility(View.VISIBLE);
        animation_nonetwork.setVisibility(View.VISIBLE);
        reconnect.setVisibility(View.VISIBLE);
        animation_nonetwork.setAnimation("loading.json");
        animation_nonetwork.loop(true);
        animation_nonetwork.playAnimation();
    }

    public void HideNetworkView(){
        frame_expand.setVisibility(View.VISIBLE);
        frame_nonetwork.setVisibility(View.INVISIBLE);
        animation_nonetwork.setVisibility(View.INVISIBLE);
        reconnect.setVisibility(View.INVISIBLE);
        animation_nonetwork.loop(false);
        animation_nonetwork.cancelAnimation();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frame_nonetwork:
                onRefresh();
                break;
        }
    }


    @Override
    public void update() {

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




    private void ItemLike(String ref) {

        Like UserSession = new Like(ref,user.get_id(), user.get_name(),user.getPicurl());
        DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("Tips").child(ref).child("like").child(user.get_id());
        c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                } else {
                    if (getActivity() instanceof TipsUserProfilActivity){
                        ShowPublication();
                    }else if (getActivity() instanceof ProfilActivity){
                        ShowPublication();
                    }
                }
            }
        });
    }

    private void RemoveLike(String key) {
        DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("Tips").child(key).child("like").child(user.get_id());
        c1v2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                        } else {
                            if (getActivity() instanceof TipsUserProfilActivity){
                                ShowPublication();
                            }else if (getActivity() instanceof ProfilActivity){
                                ShowPublication();
                            }
                        }
                    }
                });
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
            if (getActivity() instanceof TipsUserProfilActivity){
            }else if (getActivity() instanceof ProfilActivity){
                HideNetworkView();
                getProfilFromParse();
            }
        }else {
            swipeContainer.setRefreshing(false);
            ShowNetworkView();
        }
    }




    private void ItemShareTips(String ref) {
            Repost UserSession = new Repost(ref, user.get_id(), user.get_name());
            DatabaseReference c1v2 = mDatabase.child("Tips").child(ref).child("Repost").child(user.get_id());
            c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                    } else {
                        ShowPublication();
                    }
                }
            });

    }

    private void RemoveItemShare(String key) {
        FirebaseDatabase.getInstance().getReference().child("Tips").child("Repost").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                        } else {
                            if (getActivity() instanceof TipsUserProfilActivity){
                                ShowPublication();
                            }else if (getActivity() instanceof ProfilActivity){
                                ShowPublication();
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /// User s'abonner
    private void Check_ABBONNEMENT(final User user) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.get_id()).child("FacebookFriends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    int FollowersCount =(int)dataSnapshot.getChildrenCount();
                    if (FollowersCount >0){
                        nbabonnes.setText(String.valueOf(FollowersCount));
                        nbabonnement.setText(String.valueOf(FollowersCount));

                    }else {
                        nbabonnes.setText("0");
                        nbabonnement.setText("0");

                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void SABONNER(final Group abonnerTo){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(abonnerTo.getCreatorid());

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User UserSession = new User(user.get_id(), user.get_name(),user.get_email(),user.getPicurl(),user.getToken());
                final DatabaseReference c1v2 = mDatabase.child("user").child(abonnerTo.getCreatorid()).child("Followers").child(user.get_id());
                c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                        } else {
                            c1v2.child("timestamp").setValue(ServerValue.TIMESTAMP);
                            Currently_followingUser = true;
                            abonneBT.setText(Application.getContext().getString(R.string.unfollow));
                            abonneBT.setBackgroundColor(getResources().getColor(R.color.bleuclair));
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    private void UNFOLLOW(Group abonnerTo) {
        FirebaseDatabase.getInstance().getReference().child("user").child(abonnerTo.getCreatorid()).child("Followers").child(user.get_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                        } else {
                            abonneBT.setText(Application.getContext().getString(R.string.follow));
                            abonneBT.setBackgroundColor(getResources().getColor(R.color.grey));
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Boolean getPagerDisasterSet() {
        return PagerDisasterSet;
    }
}