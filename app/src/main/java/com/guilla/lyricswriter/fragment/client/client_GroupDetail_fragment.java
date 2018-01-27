package com.guilla.lyricswriter.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.Like;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterTipsMultipleRow;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.CommentaryActivity;
import com.guilla.lyricswriter.activity.HashTagActivity;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.activity.TipsUserProfilActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.vinflater;


/**
 * Created by Moi on 14/11/15.
 */
public class client_GroupDetail_fragment extends Fragment implements Updateable,ProfilActivity.OnNotificationUpdateProfil, SwipeRefreshLayout.OnRefreshListener, AdapterTipsMultipleRow.OnItemDonateClickListener, View.OnClickListener {

    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private EditText createGroup_ET;
    private Button createGroup_BT;
    private AdapterTipsMultipleRow.OnItemDonateClickListener onItemDonateClickListener;
    private RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Group> listGroup=new ArrayList<>();
    private DatabaseHandler db;
    private String objectId;
    public static User user;
    public AdapterTipsMultipleRow adapter2;
    private Group group;
    public static Map<String,String> timestamp;
    private LottieAnimationView animation_nonetwork;
    private   RelativeLayout frame_nonetwork;
    private  LinearLayout reconnect;
    private RelativeLayout frame_expand;
    private LinearLayout no_item;

    public static client_GroupDetail_fragment newInstance(Group group) {
        client_GroupDetail_fragment myFragment = new client_GroupDetail_fragment();
        Bundle args = new Bundle();
        args.putSerializable("group",group);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        inflate = inflater.inflate(R.layout.fragment_profile_client, container, false);

        if (getArguments().getSerializable("group")!=null){
            group=(Group)getArguments().getSerializable("group");
        }
         db = new DatabaseHandler(getActivity());
        objectId = getUserObjectId(getActivity());
        user = db.getUser(objectId);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
        no_item =(LinearLayout) inflate.findViewById(R.id.no_item);
        frame_expand = (RelativeLayout) inflate.findViewById(R.id.frame_expand);
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
        frame_nonetwork = (RelativeLayout) inflate.findViewById(R.id.frame_nonetwork);
        reconnect = (LinearLayout) inflate.findViewById(R.id.reconnect);
        animation_nonetwork = (LottieAnimationView) inflate.findViewById(R.id.animation_nonetwork);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        onItemDonateClickListener = this;
        getProfilFromParse();

        return inflate;
    }



    @Override
    public void update() {

    }

    private void getProfilFromParse() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        //if user connected via Facebook, get picture profil
        Log.d("TipsChildren", "getProfilFromParse");

        Initalize_RecyclerView();

            try {
                if (db.getUserCount() > 0){
                    if (Utils.isConnected(getContext())){
                        ShowTips();

                    }else{
                        ShowNetworkView();
                    }
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
        adapter2 = new AdapterTipsMultipleRow(getActivity(), listGroup, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
    }

    private void ShowTips(){
        Log.d("TipsChildren", "ShowTips");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Tips");
        Query query = mDatabase;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The PUSH ID OP WANTED
                int item=0;
                int numberitem=(int) dataSnapshot.getChildrenCount();
                listGroup.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    item=item+1;

                    String categorie = postSnapshot.child("group_categorie").getValue().toString();

                    if (group.getGroup_categorie().equalsIgnoreCase(categorie)) {
                        boolean loveTips=false;
                        String creatorId = postSnapshot.child("creatorid").getValue().toString();
                        String creatorname = postSnapshot.child("creatorname").getValue().toString();
                        String textTips = postSnapshot.child("textTips").getValue().toString();
                        String privateorpublic = postSnapshot.child("privateorpublic").getValue().toString();
                        int type= Integer.parseInt(postSnapshot.child("type").getValue().toString());
                        String user_picurl = postSnapshot.child("user_picurl").getValue().toString();

                        String like = Long.toString(postSnapshot.child("like").getChildrenCount());
                        String commentaire = Long.toString(postSnapshot.child("commentary").getChildrenCount());

                        if (commentaire.equalsIgnoreCase("null")) {
                            commentaire = "0";
                        }
                        if (postSnapshot.child("like").exists()) {
                            if (postSnapshot.child("like").child(user.get_id()).exists()) {
                                Log.d("existlove", "love");
                                loveTips = true;
                            } else {
                                loveTips = false;
                            }
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

                        if (numberitem>2) {
                            if (item == numberitem / 2) {
                                listGroup.add(new Group("", "", "","", "", "", "", false, "", "","","",3));
                            }
                        }

                        listGroup.add(new Group(postSnapshot.getKey(), creatorId, creatorname,user_picurl, textTips, privateorpublic, like, loveTips, commentaire, categorie, elapsedTimeTips,"",type));

                    }
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



    /*
  * Get group list
  **/




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
           case R.id.myFAB:

            break;
        }
    }


    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())) {
            HideNetworkView();
            swipeContainer.setRefreshing(true);
            ShowTips();
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
        DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("Tips").child(ref).child("like").child(user.get_id());
        c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                } else {
                    ShowTips();
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
                            ShowTips();
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

    }

    @Override
    public void onShareTips(int item, Group bo) {

    }

    @Override
    public void onFollowButtonClick(int item, Group bo, LinearLayout button, TextView textButton) {

    }
}