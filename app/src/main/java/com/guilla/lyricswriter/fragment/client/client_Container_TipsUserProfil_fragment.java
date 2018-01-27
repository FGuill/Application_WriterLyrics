package com.guilla.lyricswriter.fragment.client;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.CommentaryActivity;
import com.guilla.lyricswriter.activity.DetailGroupActivity;
import com.guilla.lyricswriter.activity.HashTagActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.activity.TipsUserProfilActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.guilla.lyricswriter.activity.ProfilActivity.user;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Container_TipsUserProfil_fragment extends Fragment implements Updateable, View.OnClickListener, AdapterTipsMultipleRow.OnItemDonateClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private AdapterTipsMultipleRow.OnItemDonateClickListener onItemDonateClickListener;
    private RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Group> listGroup = new ArrayList<>();
    private DatabaseHandler db;
    private AdapterTipsMultipleRow adapter2;
    public static client_Container_TipsUserProfil_fragment login_activity;

    private TextView nbabonnement;
    private TextView nbabonnes;
    private TextView username;
    private TextView nbpublication;
    private   CircleImageView icon;
    private String idkey;
    private  TextView abonneBT;
    private LinearLayout buttonLayout;

    private Group group;
    private  boolean Currently_followingUser = false;
    private User currentUser= ProfilActivity.user;

    public static client_Container_TipsUserProfil_fragment newInstance(Group group) {
        client_Container_TipsUserProfil_fragment myFragment = new client_Container_TipsUserProfil_fragment();
        Bundle args = new Bundle();
        args.putSerializable("group",group);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_userprofiltips, container, false);

        if (savedInstanceState == null) {
            if (getArguments().getSerializable("group")!=null){
                group=(Group)getArguments().getSerializable("group");
            }
            mDatabase = FirebaseDatabase.getInstance().getReference();
            nbabonnement = (TextView) inflate.findViewById(R.id.nbabonnement);
            nbpublication = (TextView) inflate.findViewById(R.id.nbpublication);
            nbabonnes = (TextView) inflate.findViewById(R.id.nbabonnes);
            username = (TextView) inflate.findViewById(R.id.username);
            icon=(CircleImageView)inflate.findViewById(R.id.logo);
            abonneBT = (TextView) inflate.findViewById(R.id.button);
            buttonLayout = (LinearLayout) inflate.findViewById(R.id.buttonLayout);
            recyclerview = (RecyclerView) inflate.findViewById(R.id.recyclerview);
            swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
            swipeContainer.setOnRefreshListener(this);
            abonneBT.setOnClickListener(this);
            onItemDonateClickListener = this;

            db = new DatabaseHandler(getActivity());
            login_activity = this;

            Bitmap PictureProfile=null;
            String images = group.getUser_picurl();
            byte[] image = DbBitmapUtility.StringToByteArray(images);
            PictureProfile = DbBitmapUtility.getImage(image);
            icon.setImageBitmap(PictureProfile);

                Check_ABBONNEMENT(group);
                InitRecyclerView();
                ShowPublication(group.getCreatorid());
                username.setText(group.getCreatorname());


        }
        return inflate;
    }


    // Callback open map when user click open map.


    private void InitRecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        adapter2 = new AdapterTipsMultipleRow(getActivity(), listGroup, onItemDonateClickListener);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerview.setAdapter(adapter2);
    }

    public void ShowPublication(final String user) {
        mDatabase.child("Tips").addValueEventListener(new ValueEventListener() {
         //   mDatabase.child("user").child(user.get_id()).addValueEventListener(new ValueEventListener() {

                @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The PUSH ID OP WANTED
                listGroup.clear();
                    int number_publication=0;

                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    boolean loveTips=false;
                    if (datasnapshot.getChildrenCount()>0) {
                        String like = Long.toString(datasnapshot.child("like").getChildrenCount());
                        if (datasnapshot.child("like").child(user).exists()) {
                            Log.d("existlove", "love");
                            loveTips = true;
                        } else {
                            loveTips = false;
                        }

                        String elapsedTimeTips="";
                        if ( datasnapshot.child("timestamp").exists()) {
                            Long Tipstimestamp = (Long) datasnapshot.child("timestamp").getValue();
                            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            sfd.format(new Date(Tipstimestamp));
                            String sCertDate = sfd.format(new Date());
                            long now = new Date().getTime();
                            elapsedTimeTips = (String) DateUtils.getRelativeTimeSpanString(Tipstimestamp, now, 0);
                            Log.d("DATE", elapsedTimeTips);
                        }


                        int commentaire = Integer.parseInt(Long.toString(datasnapshot.child("commentary").getChildrenCount()));
                        if (commentaire==0){
                            commentaire = 0;
                        }
                        String group_categorie = datasnapshot.child("group_categorie").getValue().toString();
                        String creatorId = datasnapshot.child("creatorid").getValue().toString();
                        String creatorname = datasnapshot.child("creatorname").getValue().toString();
                        String textTips = datasnapshot.child("textTips").getValue().toString();
                        String privateorpublic = datasnapshot.child("privateorpublic").getValue().toString();
                        int type= Integer.parseInt(datasnapshot.child("type").getValue().toString());

                       if (datasnapshot.child("Repost").child(group.getCreatorid()).exists()){
                           String userRepost = datasnapshot.child("Repost").child(group.getCreatorid()).child("repost_userusername").getValue().toString();
                           String elapsedTimeRepost="";

                           if ( datasnapshot.child("Repost").child(group.getCreatorid()).child("timestamp").exists()) {
                               Long Tipstimestamp = (Long) datasnapshot.child("Repost").child(group.getCreatorid()).child("timestamp").getValue();
                               SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                               sfd.format(new Date(Tipstimestamp));
                               String sCertDate = sfd.format(new Date());
                               long now = new Date().getTime();
                               elapsedTimeRepost = (String) DateUtils.getRelativeTimeSpanString(Tipstimestamp, now, 0);
                               Log.d("DATE", elapsedTimeRepost);
                           }

                           number_publication = number_publication + 1;
                           listGroup.add(new Group(datasnapshot.getKey(), creatorId, creatorname,"", textTips, privateorpublic, like, loveTips, String.valueOf(commentaire), group_categorie,elapsedTimeRepost,userRepost,1));
                       }else if (user.equalsIgnoreCase(datasnapshot.child("creatorid").getValue().toString())) {
                           number_publication = number_publication + 1;
                           listGroup.add(new Group(datasnapshot.getKey(), creatorId, creatorname,"", textTips, privateorpublic, like, loveTips, String.valueOf(commentaire), group_categorie,elapsedTimeTips,"",type));
                    }
                    }
                }

                    adapter2.notifyDataSetChanged();
                    nbpublication.setText(String.valueOf(number_publication));
                    swipeContainer.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.button:
            if (Currently_followingUser){
                UNFOLLOW(group);
                Currently_followingUser=false;
            }else {
                SABONNER(group);
                Currently_followingUser=true;
            }
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
    public void onItemLike(int item, Group bo, boolean likedTips, ImageView likebt) {
        if (Utils.isConnected(getActivity())) {
            if (likedTips) {
                likebt.getTag(item);
                likebt.setImageResource(R.drawable.ic_like);
                RemoveLike(bo.getIdkey());
                Log.d("existlove", "remove");
            } else {
                likebt.getTag(item);
                recyclerview.getLayoutManager().scrollToPosition(item);
                likebt.setImageResource(R.drawable.ic_like_touch);
                ItemLike(bo.getIdkey());
            }
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
                    if (getActivity() instanceof TipsUserProfilActivity){
                        ShowPublication(group.getCreatorid());
                    }else if (getActivity() instanceof ProfilActivity){
                        ShowPublication(user.get_id());
                    }
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
                            if (getActivity() instanceof TipsUserProfilActivity){
                                ShowPublication(group.getCreatorid());
                            }else if (getActivity() instanceof ProfilActivity){
                                ShowPublication(user.get_id());
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
    public void onCommentary(int item, Group bo) {
        Intent intent=new Intent(getActivity(), CommentaryActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("keyid",bo.getIdkey());
        bundle.putSerializable("group",bo);
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent,1);
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())) {
            swipeContainer.setRefreshing(true);
            if (getActivity() instanceof TipsUserProfilActivity){
                ShowPublication(group.getCreatorid());
            }else if (getActivity() instanceof ProfilActivity){
                ShowPublication(user.get_id());
            }
        }
    }


    @Override
    public void onCreatorNameTouch(int item, Group bo) {

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
        Intent intent=new Intent(getActivity(), DetailGroupActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("BOGroup",bo);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onShareTips(int item, Group bo) {
        if (!bo.getCreatorid().equalsIgnoreCase(user.get_id())) {
            ItemShareTips(bo.getIdkey());
        }else {
            Toast.makeText(getActivity(),"Vous ne pouvez pas repartager un post",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFollowButtonClick(int item, Group bo, LinearLayout button, TextView textButton) {

    }

    private void ItemShareTips(String ref) {
            Repost UserSession = new Repost(ref, user.get_id(), user.get_name());
            DatabaseReference c1v2 = mDatabase.child("Tips").child(ref).child("Repost").child(user.get_id());
            c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                    } else {
                        ShowPublication(user.get_id());
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
                                ShowPublication(group.getCreatorid());
                            }else if (getActivity() instanceof ProfilActivity){
                                ShowPublication(user.get_id());
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
    private void Check_ABBONNEMENT(Group abonnerTo) {
        FirebaseDatabase.getInstance().getReference().child("user").child(abonnerTo.getCreatorid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 Currently_followingUser = false;

                if (dataSnapshot.child("Followers").exists()){
                    String FollowersCount = Long.toString(dataSnapshot.child("Followers").getChildrenCount());
                    if(getActivity() != null && isAdded()){
                        nbabonnes.setText(FollowersCount);
                    }

                    if (dataSnapshot.child("Followers").getChildrenCount()>0){
                            if (dataSnapshot.child("Followers").child(user.get_id()).exists()) {
                                Log.d("Followers","True");
                                Currently_followingUser = true;
                                if(getActivity() != null && isAdded()){
                                    abonneBT.setText(Application.getContext().getString(R.string.unfollow));
                                    setButtonUnFollow();
                                }
                            }else {
                                Log.d("Followers","False");
                                Currently_followingUser = false;
                             if(getActivity() != null && isAdded()){
                                    abonneBT.setText(Application.getContext().getString(R.string.follow));
                                 setButtonFollow();
                             }
                    }
                }else {
                    Log.d("Followers","False");
                    Currently_followingUser = false;
                        if(getActivity() != null && isAdded()){
                            abonneBT.setText(Application.getContext().getString(R.string.follow));
                            setButtonFollow();
                        }
                }
            }else {
                    Log.d("Followers","False");
                    Currently_followingUser = false;
                    if(getActivity() != null && isAdded()){
                        abonneBT.setText(Application.getContext().getString(R.string.follow));
                        setButtonFollow();
                        nbabonnes.setText("0");
                    }
                }

                if (dataSnapshot.child("Following").exists()){
                    String FollowersCount = Long.toString(dataSnapshot.child("Following").getChildrenCount());
                    if (dataSnapshot.child("Following").getChildrenCount()>0){
                        if(getActivity() != null && isAdded()){
                            nbabonnement.setText(FollowersCount);
                        }
                    }else {
                        if(getActivity() != null && isAdded()){
                            nbabonnement.setText("0");
                        }
                    }
                }else {
                    if(getActivity() != null && isAdded()){
                        nbabonnement.setText("0");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void SABONNER(final Group abonnerTo){
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

public void  setButtonFollow(){
    final int sdk = Build.VERSION.SDK_INT;
    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {

        buttonLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.roundedbt) );
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            buttonLayout.setBackground( getResources().getDrawable(R.drawable.roundedbt));
        }
    }
}
    public void  setButtonUnFollow(){
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            buttonLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.roundedbtunfollow) );
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                buttonLayout.setBackground( getResources().getDrawable(R.drawable.roundedbtunfollow));
            }
        }
    }

}