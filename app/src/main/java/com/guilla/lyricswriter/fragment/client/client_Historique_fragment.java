package com.guilla.lyricswriter.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterGetCountry;
import com.guilla.lyricswriter.Utils.EndlessScrollListener;
import com.guilla.lyricswriter.Utils.SearchFilterCountry;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.HashTagActivity;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.TipsUserProfilActivity;
import com.guilla.lyricswriter.fragment.Updateable;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static com.guilla.lyricswriter.R.string.follow;


/**
 * Created by Moi on 14/11/15.
 */

public class client_Historique_fragment extends Fragment implements Updateable, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener,AdapterGetCountry.OnFollowButtonClickListener {
    private Button active_network;
    private RelativeLayout frame_nonetwork;
    private ProgressBar animation_nonetwork;
    private String indice;
    private RelativeLayout recyclerscreen;
    private EditText search_ET;
    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private ListView recyclerview;
    private LinearLayout frame_expand;
    private DatabaseReference mDatabase;
    private AdapterGetCountry.OnFollowButtonClickListener onFollowButtonClickListener;
    private DatabaseHandler db;
    private String objectId;
    public static User user;
    private AdapterGetCountry adapter2;
    private ImageView write;
    Boolean MutualFollow=false;
    private LinearLayout reconnect;
    private   List<Group> userFriends=new ArrayList<>();
    private   List<Group> TempUserCommom = new ArrayList();
    private String Last10Post="";
    private int currentPage = 0;
    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    private  int nbitems=0;
    public static client_Historique_fragment newInstance(String indice) {
        client_Historique_fragment myFragment = new client_Historique_fragment();
        Bundle args = new Bundle();
        args.putString("indice",indice);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_search_friends, container, false);

        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        reconnect = (LinearLayout) inflate.findViewById(R.id.reconnect);
        frame_nonetwork = (RelativeLayout) inflate.findViewById(R.id.frame_nonetwork);
        animation_nonetwork = (ProgressBar) inflate.findViewById(R.id.animation_nonetwork);
        frame_expand=(LinearLayout)inflate.findViewById(R.id.frame_expand);
        db = new DatabaseHandler(getActivity());
        objectId = getUserObjectId(getActivity());
        user = db.getUser(objectId);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerview = (ListView) inflate.findViewById(R.id.recyclerview);
        recyclerview.setOnItemClickListener(this);
        search_ET = (EditText) inflate.findViewById(R.id.search_ET);
        frame_nonetwork.setOnClickListener(this);
        onFollowButtonClickListener=this;

        if (this.isAdded()) {
            indice= getArguments().getString("indice");
            Log.d("indice",indice);
        }

        Initalize_RecyclerView();
        getProfilFromParse();

        return inflate;

    }
    private void getProfilFromParse() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        //if user connected via Facebook, get picture profil
        if (db.getUserCount() > 0 && Utils.isConnected(getContext())) {
            try {
                ShowKeyWords();
            } catch (CursorIndexOutOfBoundsException e) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                swipeContainer.setRefreshing(false);
            }
        }
    }

    @Override
    public void update() {

    }

    public void Initalize_RecyclerView() {
        adapter2 = new AdapterGetCountry(getActivity(), TempUserCommom,onFollowButtonClickListener);
        recyclerview.setAdapter(adapter2);
        search_ET.addTextChangedListener(new SearchFilterCountry(getActivity(), adapter2, recyclerview));
    }


  /*  public void FollowHasMoved() {
        mDatabase.child("user").child("Friends").child(user.get_id()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("childchagend",dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
    //ShowUserGroups



    public void ShowKeyWords() {

        FirebaseDatabase.getInstance().getReference().child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //The PUSH ID OP WANTED
                        userFriends.clear();
                        int item=0;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            item=item+1;
                            String safe_userid=null;
                            String safe_username=null;
                            String safe_userpicprofil=null;
                            String location=null;
                            String facebookid=null;



                            if (dataSnapshot.getChildrenCount()>0) {
                                safe_userid = postSnapshot.child("_id").getValue().toString();
                                facebookid = postSnapshot.child("_facebookid").getValue().toString();
                                safe_username = postSnapshot.child("_name").getValue().toString();
                                safe_userpicprofil = postSnapshot.child("picurl").getValue().toString();

                                Log.d("onLoadMore", postSnapshot.getKey());
                            }


                            if (user.get_id()!=null && user.get_id()!=safe_userid) {
                                String follow = Application.getContext().getString(R.string.follow);
                                if (client_Historique_fragment.this.isAdded()) {
                                    userFriends.add(new Group(postSnapshot.getKey(), facebookid, safe_username, safe_userpicprofil, 0, safe_userid, false, location));
                                }
                            }
                        }

                        HideNetworkView();
                        swipeContainer.setRefreshing(false);
                        RequestFacebookFriends();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }





    @Override
    public void onRefresh() {
        if (Utils.isConnected(getActivity())){
            Log.d("m1stFragment","onRefresh");
            swipeContainer.setRefreshing(true);
            userFriends.clear();
            TempUserCommom.clear();
            getProfilFromParse();
        }else {
            swipeContainer.setRefreshing(false);
            Toast.makeText(getActivity(),getString(R.string.dialog_network),Toast.LENGTH_LONG).show();
            ShowNetworkView();
        }}


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frame_nonetwork:
                if (Utils.isConnected2(getActivity())){
                    userFriends.clear();
                    TempUserCommom.clear();

                    if(adapter2!=null){
                        adapter2.notifyDataSetChanged();
                    }
                    getProfilFromParse();
                }
                break;
            case R.id.active_network:
                if (Utils.isConnected2(getActivity())){
                    userFriends.clear();
                    TempUserCommom.clear();

                    if(adapter2!=null){
                        adapter2.notifyDataSetChanged();
                    }
                    getProfilFromParse();
                }
        }
    }


    public void ShowLoading(){
        frame_expand.setVisibility(View.INVISIBLE);
        frame_nonetwork.setVisibility(View.VISIBLE);
        animation_nonetwork.setVisibility(View.VISIBLE);
        reconnect.setVisibility(View.INVISIBLE);
        animation_nonetwork.setIndeterminate(true);

    }


    public void ShowNetworkView(){
        frame_expand.setVisibility(View.INVISIBLE);
        frame_nonetwork.setVisibility(View.VISIBLE);
        animation_nonetwork.setVisibility(View.VISIBLE);
        reconnect.setVisibility(View.VISIBLE);
        animation_nonetwork.setIndeterminate(true);
    }


    public void HideNetworkView(){
        frame_expand.setVisibility(View.VISIBLE);
        frame_nonetwork.setVisibility(View.INVISIBLE);
        animation_nonetwork.setVisibility(View.INVISIBLE);
        reconnect.setVisibility(View.INVISIBLE);
        animation_nonetwork.setIndeterminate(false);
    }

    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Group user = (Group) recyclerview.getAdapter().getItem(i);
        Intent intent=new Intent(getActivity(), TipsUserProfilActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("BOGroup",user);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);

    }

    @Override
    public void onFollowButtonClick(int item, Group bo, LinearLayout button, TextView textButton) {
        Intent intent=new Intent(getActivity(), TipsUserProfilActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("BOGroup",bo);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
      /*  if (Utils.isConnected(getActivity())) {
            textButton.getTag(item);
            if (bo.getMutualFollow()) {
                MutualFollow = true;
                if (textButton.getText().toString().equalsIgnoreCase(getString(R.string.followback))) {
                    SABONNER(bo, textButton.getText().toString(), button, textButton);
                } else if ((textButton.getText().toString().equalsIgnoreCase(getString(R.string.unfollow)))) {
                    UNFOLLOW(bo, button, textButton);
                }
            } else {
                MutualFollow = false;
                if (textButton.getText().toString().equalsIgnoreCase(getString(R.string.follow))) {
                    SABONNER(bo, textButton.getText().toString(), button, textButton);
                } else if (textButton.getText().toString().equalsIgnoreCase(getString(R.string.unfollow))) {
                    UNFOLLOW(bo, button, textButton);
                }
            }
        }else {
            Toast.makeText(getActivity(),getString(R.string.dialog_network),Toast.LENGTH_LONG).show();
       }*/
    }

    public void SABONNER(final Group abonnerTo, final String followType, final LinearLayout followbutton, final TextView followText){
        //Set Followers
        mDatabase.child("user").child(user.get_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (!abonnerTo.getCreatorid().equalsIgnoreCase(user.get_id())) {
            final User UserSession = new User(user.get_id(), user.get_name(), user.get_email(), user.getPicurl(), user.getToken());
            final DatabaseReference c1v2 = mDatabase.child("user").child(abonnerTo.getCreatorid()).child("Followers").child(user.get_id());
            c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                    } else {
                        Log.d("abonnement","passedfollowers");
                        c1v2.child("timestamp").setValue(ServerValue.TIMESTAMP);
                        if (MutualFollow && followText.getText().toString().equalsIgnoreCase(getString(R.string.followback))){
                            followText.setText(getString(R.string.unfollow));
                            abonnerTo.setFollow(getString(R.string.unfollow));
                            setButtonUnFollow(followbutton);
                        }else if (followText.getText().toString().equalsIgnoreCase(getString(follow))){
                            Log.d("abonnement","passedfollowers");
                            followText.setText(getString(R.string.unfollow));
                            abonnerTo.setFollow(getString(R.string.unfollow));
                            setButtonUnFollow(followbutton);
                        }

                        User UserSession = new User(abonnerTo.getCreatorid(), abonnerTo.getCreatorname(), "", abonnerTo.getUser_picurl(), "");
                        final DatabaseReference c1v2 = mDatabase.child("user").child(user.get_id()).child("Following").child(abonnerTo.getCreatorid());
                        c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                }else {
                                    if (MutualFollow){
                                        Log.d("abonnement","donemutual");
                                        mDatabase.child("user").child("Friends").child(abonnerTo.getCreatorid()).child(user.get_id()).child("MutualFollow").setValue(true);
                                        mDatabase.child("user").child("Friends").child(user.get_id()).child(abonnerTo.getCreatorid()).child("MutualFollow").setValue(true);
                                    }else {
                                        Log.d("abonnement","done");
                                        mDatabase.child("user").child("Friends").child(abonnerTo.getCreatorid()).child(user.get_id()).child("MutualFollow").setValue(false);
                                    }

                                }
                            }
                        });

                    }
                }
            });
        }else {
            Toast.makeText(getActivity(),"Vous ne pouvez pas vous suivre",Toast.LENGTH_LONG).show();
        }
    }

    private void UNFOLLOW(final Group abonnerTo, final LinearLayout button, final TextView textViewFollow) {
        FirebaseDatabase.getInstance().getReference().child("user").child(abonnerTo.getCreatorid()).child("Followers").child(user.get_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                        } else {
                            if (MutualFollow && textViewFollow.getText().toString().equalsIgnoreCase(getString(R.string.unfollow))){
                                textViewFollow.setText(getString(R.string.followback));
                                abonnerTo.setFollow(getString(R.string.followback));
                                setButtonFollow(button);
                            }else {
                                textViewFollow.setText(getString(follow));
                                abonnerTo.setFollow(getString(follow));
                                setButtonFollow(button);
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


    private boolean Facebook_Permissions_Friends() {
        Set<String> permission = com.facebook.AccessToken.getCurrentAccessToken().getPermissions();
        if (permission.contains("user_friends")) {
            return true;
        }
        return false;
    }


    private void RequestFacebookFriends(){
        if (Facebook_Permissions_Friends()) {
            TempUserCommom.clear();
            final List<Group> names = new ArrayList();
            final HashMap<String,String> finalResult = new HashMap<>();

            GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                    com.facebook.AccessToken.getCurrentAccessToken(),
                    //AccessToken.getCurrentAccessToken(),
                    "/me/friends",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @SuppressWarnings("UnusedAssignment")
                        public void onCompleted(GraphResponse response) {
                            try {
                                JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                for (int l = 0; l < rawName.length(); l++) {
                                    try {
                                        String name = rawName.getJSONObject(l).get("name").toString();
                                        String id_facebook = rawName.getJSONObject(l).get("id").toString();
                                        names.add(new Group("", id_facebook, name, "", 0, "", false, ""));
                                        Log.d("Friends1", id_facebook+name);

                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }


                                if (names.size()>0) {
                                    for (Group users : userFriends){
                                        for (Group facebookFriends : names){
                                            if (users.getCreatorid().equalsIgnoreCase(facebookFriends.getCreatorid())){
                                                TempUserCommom.add(new Group(users.getIdkey(),users.getCreatorid(),users.getCreatorname(),users.getUser_picurl(),0,users.getFollow(),false,"location"));
                                                finalResult.put(users.getFollow(),facebookFriends.getCreatorname());
                                                break;
                                            }
                                        }
                                    }

                                    if (finalResult.size() > 0){
                                        final DatabaseReference c1v1 = FirebaseDatabase.getInstance().getReference().child("user").child(user.get_id()).child("FacebookFriends");
                                    c1v1.setValue(finalResult, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                adapter2.notifyDataSetChanged();
                                            } else {
                                                userFriends.clear();
                                                userFriends=TempUserCommom;
                                                adapter2.notifyDataSetChanged();

                                            }
                                        }
                                    });
                                }
                                }else {
                                    userFriends.clear();
                                    adapter2.notifyDataSetChanged();
                                }
                            }catch (NullPointerException e){

                            }catch (JSONException e) {
                                e.getMessage();
                            }
                        }
                    }
            ).executeAsync();
        }
    }

}