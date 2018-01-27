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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.Like;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.BO.UserJoined;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.GroupAdapter;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.CommentaryActivity;
import com.guilla.lyricswriter.activity.DetailGroupActivity;
import com.guilla.lyricswriter.activity.HashTagActivity;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.vinflater;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Profil_Tips_Hot_fragment extends Fragment implements Updateable,ProfilActivity.OnNotificationUpdateProfil, SwipeRefreshLayout.OnRefreshListener, GroupAdapter.OnItemDonateClickListener, View.OnClickListener {

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
    public GroupAdapter adapter2;

    public static client_Profil_Tips_Hot_fragment newInstance() {
        client_Profil_Tips_Hot_fragment myFragment = new client_Profil_Tips_Hot_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        inflate = inflater.inflate(R.layout.fragment_profile_client, container, false);

         db = new DatabaseHandler(getActivity());
        objectId = getUserObjectId(getActivity());
        user = db.getUser(objectId);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ((ProfilActivity) getActivity()).setProfilListener(client_Profil_Tips_Hot_fragment.this);
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        onItemDonateClickListener = this;
        getProfilFromParse();

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
        DatabaseHandler db = new DatabaseHandler(getActivity());
        //if user connected via Facebook, get picture profil

        Initalize_RecyclerView();

        if (db.getUserCount() > 0 && Utils.isConnected(getContext())) {
            try {
                ShowTips();
            } catch (CursorIndexOutOfBoundsException e) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                swipeContainer.setRefreshing(false);
            }
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
         adapter2 = new GroupAdapter(getActivity(), listGroup, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
    }

    private void ShowTips(){
        mDatabase.child("Tips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The PUSH ID OP WANTED
                listGroup.clear();
                ArrayList<UserJoined> userjoined=new ArrayList<UserJoined>();
                userjoined.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    userjoined.clear();
                    boolean loveTips=false;

                    String like = Long.toString(postSnapshot.child("like").getChildrenCount());
                    String commentaire = Long.toString(postSnapshot.child("commentary").getChildrenCount());
                    if (commentaire.equalsIgnoreCase("null")){
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

                    String elapsedTimeTips="";
                    if ( postSnapshot.child("timestamp").exists()) {
                        Long Tipstimestamp = (Long) postSnapshot.child("timestamp").getValue();
                        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        sfd.format(new Date(Tipstimestamp));
                        String sCertDate = sfd.format(new Date());
                        long now = new Date().getTime();
                        elapsedTimeTips = (String) DateUtils.getRelativeTimeSpanString(Tipstimestamp, now, 0);
                        Log.d("DATE", elapsedTimeTips);
                    }

                    String categorie= postSnapshot.child("group_categorie").getValue().toString();
                    String creatorId= postSnapshot.child("creatorid").getValue().toString();
                    String creatorname= postSnapshot.child("creatorname").getValue().toString();
                    String textTips= postSnapshot.child("textTips").getValue().toString();
                    String privateorpublic= postSnapshot.child("privateorpublic").getValue().toString();
                    int type= Integer.parseInt(postSnapshot.child("type").getValue().toString());
                    listGroup.add(new Group(postSnapshot.getKey(), creatorId, creatorname,"", textTips, privateorpublic, like, loveTips, commentaire, categorie,elapsedTimeTips,"",type));

                }

                adapter2.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    /*
  * Get group list
  **/




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }


    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())) {
            swipeContainer.setRefreshing(true);
            ShowTips();
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

    }

    @Override
    public void onInviteFriends(int item, Group bo) {

    }


    private void ItemLike(String ref) {
        Like UserSession = new Like(ref,user.get_id(), user.get_name(),user.getPicurl());
        DatabaseReference c1v2 = mDatabase.child("Tips").child(ref).child("like").child(user.get_id());
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


}