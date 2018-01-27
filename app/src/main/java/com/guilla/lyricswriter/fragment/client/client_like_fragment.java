package com.guilla.lyricswriter.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.CreateGroupBO;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.Like;
import com.guilla.lyricswriter.BO.Repost;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterTipsMultipleRow;
import com.guilla.lyricswriter.Utils.SortDate;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.CommentaryActivity;
import com.guilla.lyricswriter.activity.DetailPostActivity;
import com.guilla.lyricswriter.activity.HashTagActivity;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.activity.TipsUserProfilActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.guilla.lyricswriter.R.string.follow;
import static com.guilla.lyricswriter.R.string.following;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.vinflater;


/**
 * Created by Moi on 14/11/15.
 */
public class client_like_fragment extends Fragment implements Updateable,ProfilActivity.OnNotificationUpdateProfil, SwipeRefreshLayout.OnRefreshListener, AdapterTipsMultipleRow.OnItemDonateClickListener, View.OnClickListener {

    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private AdapterTipsMultipleRow.OnItemDonateClickListener onItemDonateClickListener;
    private RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Group> listFollow=new ArrayList<>();


    private DatabaseHandler db;
    private String objectId;
    public static User user;
    public AdapterTipsMultipleRow adapter2;
    public static Map<String,String> timestamp;
    private String type="";
    private LottieAnimationView animation_nonetwork;
    private   RelativeLayout frame_nonetwork;
    private  LinearLayout reconnect;
    private RelativeLayout frame_expand;
    private LinearLayout no_item;
    public static client_like_fragment newInstance(String type) {
        client_like_fragment myFragment = new client_like_fragment();
        Bundle args = new Bundle();
        args.putString("type",type);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        inflate = inflater.inflate(R.layout.fragment_profile_client, container, false);
        type=getArguments().getString("type");
        db = new DatabaseHandler(getActivity());
        objectId = getUserObjectId(getActivity());
        user = db.getUser(objectId);
        ((ProfilActivity) getActivity()).setProfilListener(client_like_fragment.this);
       // ((ProfilActivity) getActivity()).setUpdateTipsListener(client_Profil_fragment.this);

        frame_nonetwork = (RelativeLayout) inflate.findViewById(R.id.frame_nonetwork);
        frame_nonetwork.setOnClickListener(this);
        frame_expand = (RelativeLayout) inflate.findViewById(R.id.frame_expand);
        reconnect = (LinearLayout) inflate.findViewById(R.id.reconnect);
        animation_nonetwork = (LottieAnimationView) inflate.findViewById(R.id.animation_nonetwork);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        no_item =(LinearLayout) inflate.findViewById(R.id.no_item);
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);

        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        onItemDonateClickListener = this;
        Initalize_RecyclerView();

        return inflate;
    }



    @Override
    public void update() {

    }



    private void getProfilFromParse() {

        //if user connected via Facebook, get picture profil


        try{
        if (db.getUserCount() > 0 && Utils.isConnected(getContext())) {
            if (!swipeContainer.isRefreshing()){
                swipeContainer.setRefreshing(true);
            }
            ShowFollowers(false);
        }else {
            ShowNetworkView();
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
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        adapter2 = new AdapterTipsMultipleRow(getActivity(), listFollow, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
    }

    private interface Tips{
        public void OnShowTips();
    }

    private void ShowFollowers(final Boolean isFirstSearch){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("InvitationCollaboration");
                mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listFollow.clear();
                //The PUSH ID OP WANTED
                String safe_userid=null;
                String safe_username=null;
                String safe_userpicprofil=null;
                if (type.equalsIgnoreCase("theylike")) {
                    if (user.get_id()!=null) {
                        if (client_like_fragment.this.isAdded()) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                for (DataSnapshot followerssnap : dataSnapshot.getChildren()) {
                                    Date time = null;
                                    String ProjectName = null;
                                    String _UserInvitedid = null;
                                    String _UserInvitedname = null;
                                    String _status = null;
                                    String idCreatorProject = null;
                                    String idCreatorProjectName = null;
                                    String idProject = null;
                                    String _UserInvite = null;
                                    String _UserInviteid = null;


                                    if (followerssnap.child("InUserInvitedid").exists()) {
                                        _UserInvitedid = followerssnap.child("InUserInvitedid").getValue().toString();

                                        if (user.get_id().equalsIgnoreCase(_UserInvitedid)) {
                                            if (followerssnap.child("timestamp").exists()) {
                                                time = getTimeStamp((Long) followerssnap.child("timestamp").getValue());
                                            }

                                            if (followerssnap.child("nameProject").exists()) {
                                                ProjectName = followerssnap.child("nameProject").getValue().toString();
                                            }

                                            if (followerssnap.child("idProject").exists()) {
                                                idProject = followerssnap.child("idProject").getValue().toString();
                                            }

                                            if (followerssnap.child("InUserInvitedname").exists()) {
                                                _UserInvitedname = followerssnap.child("InUserInvitedname").getValue().toString();
                                            }

                                            if (followerssnap.child("InUserInvite").exists()) {
                                                _UserInvite = followerssnap.child("InUserInvite").getValue().toString();
                                            }
                                            if (followerssnap.child("InUserInviteid").exists()) {
                                                _UserInviteid = followerssnap.child("InUserInviteid").getValue().toString();
                                            }

                                            if (followerssnap.child("Instatus").exists()) {
                                                _status = followerssnap.child("Instatus").getValue().toString();
                                            }
                                            if (followerssnap.child("idCreatorProject").exists()) {
                                                idCreatorProject = followerssnap.child("idCreatorProject").getValue().toString();
                                            }

                                            if (followerssnap.child("idCreatorProjectName").exists()) {
                                                idCreatorProjectName = followerssnap.child("idCreatorProjectName").getValue().toString();
                                            }

                                            listFollow.add(new Group(followerssnap.getKey(), idProject, ProjectName, idCreatorProject, idCreatorProjectName, _UserInvitedid, _UserInvitedname, _UserInviteid, _UserInvite, _status, time, 4));
                                        }
                                    }
                                }
                            }

                        }


                        try {
                            Collections.sort(listFollow, new SortDate());
                        } catch (NullPointerException e) {
                        }
                        adapter2.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                        HideNetworkView();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }






    /*
  * Get group list
  **/

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
    public void onRefresh() {
        if (Utils.isConnected(getContext())) {
            swipeContainer.setRefreshing(true);
            getProfilFromParse();
        }else {
            swipeContainer.setRefreshing(false);
            ShowNetworkView();
        }
    }



    @Override
    public void TaskOnNotification(String business, String sharepoints) {
     /*   Popup_onNotification onNotification=new Popup_onNotification();
        if (dummyanchor!=null){
            onNotification.displayPopupWindow(dummyanchor,getActivity(),"","");
        }*/
    }

    @Override
    public void onItemClick(int item, Group bo) {

    }


    @Override
    public void onItemLike(int item, Group bo, boolean likedTips, ImageView likebt) {
        if (likedTips){
            likebt.getTag(item);
            likebt.setImageResource(R.drawable.ic_like);
            RemoveLike(bo.getIdkey());
            Log.d("existlove","remove");
        }else {
            ItemLike(bo.getIdkey());
            likebt.getTag(item);
            recyclerview.getLayoutManager().scrollToPosition(item);
            likebt.setImageResource(R.drawable.ic_like_touch);
        }
    }

    private void ItemLike(String ref) {
        Like UserSession = new Like(ref,user.get_id(), user.get_name(),user.getPicurl());
        DatabaseReference c1v2 = mDatabase.child("Tips").child(ref).child("like").child(user.get_id());
        c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                } else {
                    ShowFollowers(false);
                }
            }
        });
    }

    private void RemoveLike(String key) {
        FirebaseDatabase.getInstance().getReference().child("Tips").child(key).child("like").child(user.get_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                        } else {
                            ShowFollowers(false);
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
    public void onCommentary(int item, Group bo) {
        Intent intent=new Intent(getActivity(), CommentaryActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("keyid",bo.getIdkey());
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent,1);
    }

    @Override
    public void onCreatorNameTouch(int item, Group bo) {
        Intent intent=new Intent(getActivity(), TipsUserProfilActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("BOGroup",bo);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onHashTagTouch(int item, Group bo, String hashTag) {
        Intent intent=new Intent(getActivity(), HashTagActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("hashTag",hashTag);
        bundle.putSerializable("BOGroup",bo);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onImageCategoryTouch(int item, Group bo) {
        Log.d("groupbb",bo.getIdkey());
        Log.d("groupbb",bo.getGroup_categorie());
        Intent intent=new Intent(getActivity(), DetailPostActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("BOGroup",bo);
        bundle.putString("matchRow","true");
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }


    @Override
    public void onShareTips(int item, Group bo) {
        if (!bo.getCreatorid().equalsIgnoreCase(user.get_id())){
            final DatabaseReference newRef2 = mDatabase.child("Tips").push();
        newRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ItemShareTips(bo.getIdkey());
    }else {
            Toast.makeText(getActivity(),"Vous ne pouvez pas repartager un post",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFollowButtonClick(int item, Group bo, LinearLayout button, TextView textButton) {
        if (Utils.isConnected(getActivity())) {
            textButton.getTag(item);
            if (textButton.getText().toString().equalsIgnoreCase(Application.getContext().getResources().getString(R.string.join))) {
                SABONNER(bo, textButton.getText().toString(), button, textButton);
            } else if ((textButton.getText().toString().equalsIgnoreCase(Application.getContext().getResources().getString(R.string.quit)))) {
                UNFOLLOW(bo, button, textButton);
            }
        }
        else {
            Toast.makeText(getActivity(),Application.getContext().getString(R.string.dialog_network),Toast.LENGTH_LONG).show();
        }
    }


    public void SABONNER(final Group abonnerTo, final String followType, final LinearLayout followbutton, final TextView followText){
        if (followText.getText().toString().equalsIgnoreCase(Application.getContext().getResources().getString(R.string.join))){
            followText.setText(Application.getContext().getResources().getString(R.string.quit));
            abonnerTo.setFollow(Application.getContext().getResources().getString(R.string.quit));
            setButtonUnFollow(followbutton);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(abonnerTo.get_idProject());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(abonnerTo.get_idProject()).child("UsersParticiant").child(user.get_id());
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String uripicture = user.getPicurl();
                            CreateGroupBO UserSession = new CreateGroupBO(user.get_id(), user.get_name(), "", uripicture);
                            mDatabase.setValue(UserSession, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                    } else {
                                        mDatabase.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("InvitationCollaboration").child(abonnerTo.getIdkey());
                                        mDatabase2.child("Instatus").setValue("invitationaccepted");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }else {
                    Toast.makeText(getActivity(),"This group doesn't exist anymore",Toast.LENGTH_LONG).show();
                    if (followText.getText().toString().equalsIgnoreCase(Application.getContext().getResources().getString(R.string.quit))) {
                        followText.setText(Application.getContext().getResources().getString(R.string.join));
                        abonnerTo.setFollow(Application.getContext().getResources().getString(R.string.join));
                        setButtonFollow(followbutton);
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("InvitationCollaboration").child(abonnerTo.getIdkey());
                        mDatabase.setValue(null);
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(abonnerTo.get_idProject());
                        mDatabase.setValue(null);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }

    private void UNFOLLOW(final Group abonnerTo, final LinearLayout button, final TextView textViewFollow) {
        if (textViewFollow.getText().toString().equalsIgnoreCase(Application.getContext().getResources().getString(R.string.quit))) {
            textViewFollow.setText(Application.getContext().getResources().getString(R.string.join));
            abonnerTo.setFollow(Application.getContext().getResources().getString(R.string.join));
            setButtonFollow(button);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(abonnerTo.get_idProject());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(abonnerTo.get_idProject()).child("UsersParticiant").child(user.get_id());
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mDatabase.setValue(null, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                    } else {
                                        if (textViewFollow.getText().toString().equalsIgnoreCase(Application.getContext().getResources().getString(R.string.join))) {

                                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("InvitationCollaboration").child(abonnerTo.getIdkey());
                                            mDatabase.child("Instatus").setValue("pending");
                                            mDatabase.child("UserInvitNotifiedBackkAccepted").setValue("unotified");
                                      //      mDatabase.child("UserInvitedNotified").setValue("unotified");
                                        }
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(getActivity(),"error, group not exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }

            public void  setButtonFollow(LinearLayout buttonFollow){
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {

            buttonFollow.setBackgroundDrawable( getResources().getDrawable(R.drawable.roundedbt) );
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                buttonFollow.setBackground( getResources().getDrawable(R.drawable.roundedbt));
            }
        }
    }

    public void  setButtonUnFollow(LinearLayout buttonFollow){
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            buttonFollow.setBackgroundDrawable( getResources().getDrawable(R.drawable.roundedbtunfollow) );
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                buttonFollow.setBackground( getResources().getDrawable(R.drawable.roundedbtunfollow));
            }
        }
    }


    private void ItemShareTips(String ref) {
        final DatabaseReference c1v2;
        Repost UserSession = new Repost(ref,user.get_id(), user.get_name());
        c1v2 = mDatabase.child("Tips").child(ref).child("Repost").child(user.get_id());
        c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                } else {
                    c1v2.child("timestamp").setValue(ServerValue.TIMESTAMP);
                    ShowFollowers(false);
                }
            }
        });
    }


    private Date getTimeStamp(Long timestamp){
        Date elapsedTimeTipsAnswer = null;
        if (timestamp!=null) {
            Long Tipstimestamp = (Long) timestamp;
            elapsedTimeTipsAnswer = new Date(Tipstimestamp);
        }
        return elapsedTimeTipsAnswer;
    }


}