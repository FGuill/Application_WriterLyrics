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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.BO.CreateGroupBO;
import com.guilla.lyricswriter.BO.CreeateGroupBo;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.Like;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.BO.UserJoined;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterMultipleRow;
import com.guilla.lyricswriter.Utils.AdapterTipsMultipleRow;
import com.guilla.lyricswriter.Utils.GroupAdapter;
import com.guilla.lyricswriter.Utils.GroupAdapterTipsPro;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.Utils.WriterAdapter;
import com.guilla.lyricswriter.activity.CommentaryActivity;
import com.guilla.lyricswriter.activity.DetailGroupActivity;
import com.guilla.lyricswriter.activity.HashTagActivity;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.activity.TipsActivity;
import com.guilla.lyricswriter.activity.TipsUserProfilActivity;
import com.guilla.lyricswriter.activity.WriterActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.guilla.lyricswriter.R.id.creatorname;
import static com.guilla.lyricswriter.R.id.listusers;
import static com.guilla.lyricswriter.activity.ProfilActivity.clientProfilActivity;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.vinflater;
import static com.guilla.lyricswriter.fragment.pagerTips.PagerFragment.PagerTwoSet;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Profil_Tips_Top_fragment extends Fragment implements Updateable,ProfilActivity.OnNotificationUpdateProfil, SwipeRefreshLayout.OnRefreshListener, AdapterMultipleRow.OnItemDonateClickListener, View.OnClickListener, AdapterTipsMultipleRow.OnItemDonateClickListener {

    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private EditText createGroup_ET;
    private Button createGroup_BT;
    private LottieAnimationView animation_nonetwork;
    private   RelativeLayout frame_nonetwork;
    private  LinearLayout reconnect;
    private RelativeLayout frame_expand;

    private AdapterMultipleRow.OnItemDonateClickListener onItemDonateClickListener;
    private RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Group> listGroup=new ArrayList<>();
    private DatabaseHandler db;
    private String objectId;
    public static User user;
    private  AdapterMultipleRow adapter2;
    public static client_Profil_Tips_Top_fragment newInstance() {
        client_Profil_Tips_Top_fragment myFragment = new client_Profil_Tips_Top_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        inflate = inflater.inflate(R.layout.fragment_profile_client2, container, false);

         db = new DatabaseHandler(getActivity());
        objectId = getUserObjectId(getActivity());
        user = db.getUser(objectId);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getActivity() instanceof ProfilActivity) {
            ((ProfilActivity) getActivity()).setProfilListener(client_Profil_Tips_Top_fragment.this);
        }

        frame_expand = (RelativeLayout) inflate.findViewById(R.id.frame_expand);
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
        frame_nonetwork = (RelativeLayout) inflate.findViewById(R.id.frame_nonetwork);
        reconnect = (LinearLayout) inflate.findViewById(R.id.reconnect);
        animation_nonetwork = (LottieAnimationView) inflate.findViewById(R.id.animation_nonetwork);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        createGroup_ET=(EditText)inflate.findViewById(R.id.createGroup_ET);
        createGroup_BT=(Button) inflate.findViewById(R.id.createGroup_BT);
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
        createGroup_BT.setOnClickListener(this);

        onItemDonateClickListener = this;
        Initalize_RecyclerView();

      /*  DatabaseReference newRef = mDatabase.child("Category");
        ArrayList<Category> categories = new ArrayList<>();

        categories.add(new Category(newRef.getKey(),"Animals & Pets",R.drawable.category_animalpets));
        categories.add(new Category(newRef.getKey(),"Home & Garden",R.drawable.category_home));
        categories.add(new Category(newRef.getKey(),"Arts & Culture",R.drawable.category_artculture));
        categories.add(new Category(newRef.getKey(),"Miscellaneous",R.drawable.category_miscellanisous));
        categories.add(new Category(newRef.getKey(),"Careers & Work",R.drawable.category_careerswork));
        categories.add(new Category(newRef.getKey(),"Money & Finance",R.drawable.category_moneyfinance));
        categories.add(new Category(newRef.getKey(),"Clothing",R.drawable.category_clothing));
        categories.add(new Category(newRef.getKey(),"Productivity",R.drawable.category_productivity));
        categories.add(new Category(newRef.getKey(),"Computers",R.drawable.category_computers));
        categories.add(new Category(newRef.getKey(),"Requests",R.drawable.category_requests));
        categories.add(new Category(newRef.getKey(),"Electronics",R.drawable.category_electronics));
        categories.add(new Category(newRef.getKey(),"School & College",R.drawable.category_schoolcollege));
        categories.add(new Category(newRef.getKey(),"Food & Drink",R.drawable.category_fooddrink));
        categories.add(new Category(newRef.getKey(),"Social",R.drawable.category_social));
        categories.add(new Category(newRef.getKey(),"Health & Fitness",R.drawable.category_healthfitness));
        categories.add(new Category(newRef.getKey(),"Travelling",R.drawable.category_travel));

    for (Category category1 : categories){
    newRef=mDatabase.child("Category").child(category1.getCategory_nom()).push();
    Drawable myDrawable7 = getResources().getDrawable(category1.getCategory_drawables());
    Bitmap myLogo6 = ((BitmapDrawable) myDrawable7).getBitmap();
    final String image6 = Utils.BitmapToString(myLogo6);
    Category category6 = new Category(newRef.getKey(),category1.getCategory_nom(),image6);
    newRef.setValue(category6);
    }*/
        return inflate;
    }



    @Override
    public void update() {

    }

    private void getProfilFromParse() {
         db = new DatabaseHandler(getActivity());
        //if user connected via Facebook, get picture profil

        try {
        if (db.getUserCount() > 0) {
            if (Utils.isConnected(getContext())) {
                ShowTips();
                PagerTwoSet = true;

            } else {
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
         adapter2 = new AdapterMultipleRow(getActivity(), listGroup, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
    }

    private void ShowTips(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(user.get_id()).child("MyLyrics");
        mDatabase.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //The PUSH ID OP WANTED
                listGroup.clear();
                if (dataSnapshot.getChildrenCount()>0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

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
                        String privateorpublic = postSnapshot.child("privateorpublic").getValue().toString();
                        String listusers = postSnapshot.child("listusers").getValue().toString();


                        if (postSnapshot.child("text").exists()) {
                            String text = postSnapshot.child("text").getValue().toString();
                            listGroup.add(new Group(postSnapshot.getKey(), groupName, creatorId, creatorname, styleMusic, text, "", elapsedTimeTips,2));
                        } else {
                            listGroup.add(new Group(postSnapshot.getKey(), groupName, creatorId, creatorname, styleMusic, privateorpublic, "", elapsedTimeTips,2));
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


    public void ShowNetworkView(){
        swipeContainer.setRefreshing(false);
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



    /*
  * Get group list
  **/


    private void CreateGroupList() {
        final DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("user").child(user.get_id()).child("MyLyrics").push();
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
                            } else {
                                newRef.child("timestamp").setValue(ServerValue.TIMESTAMP);
                            }
                        }
                    });
                }else {
                    Toast.makeText(getActivity(),"Veuillez entrer un nom de morceau",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createGroup_BT:
                CreateGroupList();
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
    public void onJoinGroupClick(int item, Group bo) {
        Intent intent=new Intent(getActivity(), WriterActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("keyid",bo.getIdkey());
        bundle.putSerializable("group",bo);
        bundle.putSerializable("fragment","single");
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent,1);
    }

    @Override
    public void onInviteFriends(int item, Group bo) {

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
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Tips").child(ref).child("like").child(user.get_id());
        mDatabase.setValue(UserSession, new DatabaseReference.CompletionListener() {
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
        Intent intent=new Intent(getActivity(), DetailGroupActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("BOGroup",bo);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onShareTips(int item, Group bo) {

    }

    @Override
    public void onFollowButtonClick(int item, Group bo, LinearLayout button, TextView textButton) {

    }

}