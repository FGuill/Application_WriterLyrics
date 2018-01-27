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

import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.InviteBO;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterGetCountry;
import com.guilla.lyricswriter.Utils.EndlessScrollListener;
import com.guilla.lyricswriter.Utils.SearchFilterCountry;
import com.guilla.lyricswriter.Utils.UpdateFacebookFriends;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.InviteCollabActivity;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.activity.TipsUserProfilActivity;
import com.guilla.lyricswriter.activity.WriterActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.guilla.lyricswriter.R.id.invite;
import static com.guilla.lyricswriter.R.id.layoutinvite;
import static com.guilla.lyricswriter.R.id.map;


/**
 * Created by Moi on 14/11/15.
 */

public class client_invitecollaboration_fragment extends Fragment implements Updateable, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener,AdapterGetCountry.OnFollowButtonClickListener, UpdateFacebookFriends.FacebookFriends, InviteCollabActivity.OnClickSaveListener {
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
    private  List<Group> userFriends=new ArrayList<>();
    private String Last10Post="";
    private int currentPage = 0;
    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    private  int nbitems=0;
    private String key=null;
    private Group ProjectGroup=null;
    private String groupnameProject=null;
    private List<Group> InvitedOrNot=new ArrayList<>();
    private UpdateFacebookFriends updateFacebookFriends;
   private RelativeLayout layoutinvite;
    private LinearLayout invitefriends;
    public static client_invitecollaboration_fragment newInstance(String key, Group group) {
        client_invitecollaboration_fragment myFragment = new client_invitecollaboration_fragment();
        Bundle args = new Bundle();
        args.putString("key",key);
        args.putSerializable("group",group);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_search_friends, container, false);

        ((InviteCollabActivity) getActivity()).setOnClickSaveListener(client_invitecollaboration_fragment.this);
        layoutinvite = (RelativeLayout) inflate.findViewById(R.id.layoutinvite);
         invitefriends = (LinearLayout) inflate.findViewById(R.id.invitefriends);
        invitefriends.setOnClickListener(this);
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
        updateFacebookFriends=new UpdateFacebookFriends(getActivity());
        updateFacebookFriends.setInterfaceFacebookFriends(this);

        onFollowButtonClickListener=this;

        if (this.isAdded()) {
            key=getArguments().getString("key");
            ProjectGroup=(Group) getArguments().getSerializable("group");
            groupnameProject=ProjectGroup.getGroupname();
        }

        Initalize_RecyclerView();

        updateFacebookFriends.hasFacebookFriends(new UpdateFacebookFriends.interfaceFriends() {
            @Override
            public void hasFriends(Boolean hasFriends) {
                updateFacebookFriends.Allusers_FirebaseDB();
            }
        });

        getProfilFromParse();

        return inflate;

    }
    private void getProfilFromParse() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        //if user connected via Facebook, get picture profil
        try {
        if (db.getUserCount() > 0 && Utils.isConnected(getContext())) {
            if (!swipeContainer.isRefreshing()){
                swipeContainer.setRefreshing(true);
            }
                ShowKeyWords();
        }else {
            ShowNetworkView();
        }
            } catch (CursorIndexOutOfBoundsException e) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                swipeContainer.setRefreshing(false);
            }catch (NullPointerException e){

        }

    }

    @Override
    public void update() {

    }

    public void Initalize_RecyclerView() {
        adapter2 = new AdapterGetCountry(getActivity(), userFriends,onFollowButtonClickListener);
        search_ET.addTextChangedListener(new SearchFilterCountry(getActivity(), adapter2, recyclerview));
        recyclerview.setOnScrollListener(endlessScrollListener);

    }

    EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
        @Override
        public boolean onLoadMore(int page, int totalItemsCount) {
            Log.d("totalItemsCount",String.valueOf(totalItemsCount));

            if (totalItemsCount<=nbitems){
                loadNextDataFromApi(page);
            }
            Log.d("onLoadMore","loadmore");
            // Triggered only when new data needs to be appended to the list
            // Add whatever code is needed to append new items to your AdapterView

            // or loadNextDataFromApi(totalItemsCount);
            return true; // ONLY if more data is actually being loaded; false otherwise.
        }
    };


    public void loadNextDataFromApi(int offset) {
        currentPage++;
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
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
        InvitedOrNot.clear();

        FirebaseDatabase.getInstance().getReference().child("InvitationCollaboration").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String _UserInvitedid = null;
                    String idProject = null;
                    String idCreatorProject = null;


                    if (snapshot.child("idProject").exists()) {
                        idProject = snapshot.child("idProject").getValue().toString();
                        if (idProject.equalsIgnoreCase(ProjectGroup.getIdkey())) {
                            if (snapshot.child("InUserInvitedid").exists()) {

                                idCreatorProject = snapshot.child("idCreatorProject").getValue().toString();
                                _UserInvitedid = snapshot.child("InUserInvitedid").getValue().toString();

                                if (snapshot.child("Instatus").getValue().toString().equalsIgnoreCase("pending")) {
                                    if (idCreatorProject.equalsIgnoreCase(_UserInvitedid)){
                                        InvitedOrNot.add(new Group(snapshot.getKey(), _UserInvitedid, "", "", 0, "iscreator", false, ""));
                                    }else {
                                        InvitedOrNot.add(new Group(snapshot.getKey(), _UserInvitedid, "", "", 0, "pending", false, ""));
                                    }
                                } else {
                                    if (idCreatorProject.equalsIgnoreCase(_UserInvitedid)){
                                        InvitedOrNot.add(new Group(snapshot.getKey(), _UserInvitedid, "", "", 0, "iscreator", false, ""));
                                    }else {
                                        InvitedOrNot.add(new Group(snapshot.getKey(), _UserInvitedid, "", "", 0, "accepted", false, ""));

                                    }
                                }
                            }
                        }
                    }
                }

                mDatabase.child("user").child(user.get_id()).child("FacebookFriends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //The PUSH ID OP WANTED
                        userFriends.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String safe_userid = null;
                            String safe_username = null;
                            String safe_userpicprofil = null;
                            String location = null;

                            if (dataSnapshot.getChildrenCount() > 0) {
                                safe_userid = postSnapshot.getKey().toString();
                                safe_username = postSnapshot.getValue().toString();
                                Log.d("onLoadMore", postSnapshot.getKey());
                            }

                            if (InvitedOrNot.size()>0){
                            for (Group entry : InvitedOrNot)
                            {
                                if (safe_userid.equalsIgnoreCase(entry.getCreatorid())){
                                    if (entry.getFollow().equalsIgnoreCase("accepted")){
                                        if (user.get_id() != null) {
                                            if (client_invitecollaboration_fragment.this.isAdded()) {
                                                String accepted = Application.getContext().getString(R.string.inviteaccepted);
                                                userFriends.add(new Group(postSnapshot.getKey(), safe_userid, safe_username, safe_userpicprofil, 0, accepted, false, location));
                                            }
                                        }
                                        break;
                                    }else {
                                        if (entry.getFollow().equalsIgnoreCase("pending")){
                                            if (user.get_id() != null) {
                                                if (client_invitecollaboration_fragment.this.isAdded()) {
                                                    String sent = Application.getContext().getString(R.string.invitesent);
                                                    userFriends.add(new Group(postSnapshot.getKey(), safe_userid, safe_username, safe_userpicprofil, 0, sent, false, location));
                                                }
                                            }
                                        }else  if (entry.getFollow().equalsIgnoreCase("iscreator")){
                                            if (user.get_id() != null) {
                                                if (client_invitecollaboration_fragment.this.isAdded()) {
                                                    String iscreator = Application.getContext().getString(R.string.iscreator);
                                                    userFriends.add(new Group(postSnapshot.getKey(), safe_userid, safe_username, safe_userpicprofil, 0, iscreator, false, location));
                                                }
                                            }
                                        }
                                  break;
                                    }
                                }else {
                                    if (user.get_id() != null) {
                                        String follow = Application.getContext().getString(R.string.invite);
                                        if (client_invitecollaboration_fragment.this.isAdded()) {
                                            userFriends.add(new Group(postSnapshot.getKey(), safe_userid, safe_username, safe_userpicprofil, 0, Application.getContext().getResources().getString(R.string.invite), false, location));

                                        }
                                    }
                                break;
                                }
                            }
                            }else {
                                if (client_invitecollaboration_fragment.this.isAdded()) {
                                    userFriends.add(new Group(postSnapshot.getKey(), safe_userid, safe_username, safe_userpicprofil, 0, Application.getContext().getResources().getString(R.string.invite), false, location));

                                }
                            }

                        }

                        HideNetworkView();
                        swipeContainer.setRefreshing(false);
                        recyclerview.setAdapter(adapter2);
                        adapter2.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
        if (Utils.isConnected(getActivity())){
            Log.d("m1stFragment","onRefresh");
            swipeContainer.setRefreshing(true);
            userFriends.clear();
            if(adapter2!=null){
                adapter2.notifyDataSetChanged();
                endlessScrollListener.resetState();
            }
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
                    if(adapter2!=null){
                        adapter2.notifyDataSetChanged();
                        endlessScrollListener.resetState();
                    }
                    getProfilFromParse();
                }
                break;
            case R.id.active_network:
                if (Utils.isConnected2(getActivity())){
                    userFriends.clear();
                    if(adapter2!=null){
                        adapter2.notifyDataSetChanged();
                        endlessScrollListener.resetState();
                    }
                    getProfilFromParse();
                }
                break;
            case R.id.invitefriends:
                if (Utils.isConnected2(getActivity())) {
                    String appLinkUrl, previewImageUrl;
                    appLinkUrl = "https://fb.me/1504667966290049";
                    previewImageUrl = "http://www.hostingpics.net/viewer.php?id=998362safetychecklogo.png";

                    if (AppInviteDialog.canShow()) {
                        AppInviteContent content = new AppInviteContent.Builder()
                                .setApplinkUrl(appLinkUrl)
                                .setPreviewImageUrl(previewImageUrl)
                                .build();
                        AppInviteDialog.show(getActivity(), content);
                        RemoveInviteFriends();
                    }
                }else {
                    Toast.makeText(getActivity(),getString(R.string.dialog_network),Toast.LENGTH_LONG).show();
                }
                    break;
        }
    }


    public void ShowLoading(){
        frame_expand.setVisibility(View.INVISIBLE);
        frame_nonetwork.setVisibility(View.INVISIBLE);
        animation_nonetwork.setVisibility(View.VISIBLE);
        reconnect.setVisibility(View.VISIBLE);
        animation_nonetwork.setIndeterminate(true);
    }

    public void ShowInviteFriends(){
        frame_expand.setVisibility(View.INVISIBLE);
        frame_nonetwork.setVisibility(View.VISIBLE);
        animation_nonetwork.setVisibility(View.INVISIBLE);
        reconnect.setVisibility(View.INVISIBLE);
        animation_nonetwork.setIndeterminate(false);
        layoutinvite.setVisibility(View.VISIBLE);
        invitefriends.setVisibility(View.VISIBLE);

    }

    public void RemoveInviteFriends(){
        frame_expand.setVisibility(View.VISIBLE);
        frame_nonetwork.setVisibility(View.INVISIBLE);
        animation_nonetwork.setVisibility(View.INVISIBLE);
        reconnect.setVisibility(View.INVISIBLE);
        animation_nonetwork.setIndeterminate(false);
        layoutinvite.setVisibility(View.INVISIBLE);
        invitefriends.setVisibility(View.INVISIBLE);

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
      /*  Group user = (Group) recyclerview.getAdapter().getItem(i);
        Intent intent=new Intent(getActivity(), TipsUserProfilActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("BOGroup",user);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);*/
    }

    @Override
    public void onFollowButtonClick(int item, Group bo, LinearLayout button, TextView textButton) {
       if (Utils.isConnected(getActivity())) {
            textButton.getTag(item);
           String accepted = Application.getContext().getResources().getString(R.string.inviteaccepted);
           String sent = Application.getContext().getResources().getString(R.string.invitesent);

           if (bo.getFollow().equalsIgnoreCase(accepted)){

           }else if (bo.getFollow().equalsIgnoreCase(sent)){

           }else if (bo.getFollow().equalsIgnoreCase(Application.getContext().getResources().getString(R.string.invite))){
               SABONNER(bo, textButton.getText().toString(), button, textButton);
           }
        }else {
            Toast.makeText(getActivity(),getString(R.string.dialog_network),Toast.LENGTH_LONG).show();
       }
    }

    public void SABONNER(final Group userInvite, final String followType, final LinearLayout followbutton, final TextView followText) {
        //Set Followers
        setButtonUnFollow(followbutton);
        String sent = Application.getContext().getResources().getString(R.string.invitesent);
        followText.setText(sent);

        final DatabaseReference c1v2 = mDatabase.child("InvitationCollaboration").push();
        c1v2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final InviteBO UserSession = new InviteBO(ProjectGroup.getIdkey(), groupnameProject, ProjectGroup.getCreatorid(), ProjectGroup.getCreatorname(), ProjectGroup.getUser_picurl(),user.get_name(),user.get_id(), userInvite.getCreatorid(), userInvite.getCreatorname(),"", "", ServerValue.TIMESTAMP,"pending",0);
                c1v2.setValue(UserSession, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            setButtonFollow(followbutton);
                            followText.setText(Application.getContext().getResources().getString(R.string.invite));
                        } else {

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                                textViewFollow.setText(getString(R.string.follow));
                                abonnerTo.setFollow(getString(R.string.follow));
                                setButtonFollow(button);
                            }
                            mDatabase.child("user").child(user.get_id()).child("Following").child(abonnerTo.getCreatorid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().setValue(null, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                            } else {
                                                if (MutualFollow){
                                                    mDatabase.child("user").child("Friends").child(abonnerTo.getCreatorid()).child(user.get_id()).child("MutualFollow").setValue(false);
                                                    mDatabase.child("user").child("Friends").child(user.get_id()).child(abonnerTo.getCreatorid()).child("MutualFollow").setValue(null);
                                                }else {
                                                    mDatabase.child("user").child("Friends").child(abonnerTo.getCreatorid()).child(user.get_id()).child("MutualFollow").setValue(null);
                                                    mDatabase.child("user").child("Friends").child(user.get_id()).child(abonnerTo.getCreatorid()).child("MutualFollow").setValue(null);
                                                }

                                                userFriends.clear();

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

    @Override
    public void setFacebookUsersToFireBase(List<Group> group) {
        Log.d("hasFriends","hasUpdatedFriends");
        if (group.size()>0){
            getProfilFromParse();
        }else {
            ShowInviteFriends();
        }
    }

    @Override
    public void saveWriteClick() {
        invitefriends.performClick();
    }
}