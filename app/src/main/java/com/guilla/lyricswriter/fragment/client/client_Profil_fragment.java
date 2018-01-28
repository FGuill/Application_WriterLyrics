package com.guilla.lyricswriter.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterMultipleRow;
import com.guilla.lyricswriter.Utils.GroupAdapter;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.InviteCollabActivity;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.WriterActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.vinflater;
import static com.guilla.lyricswriter.fragment.pagerTips.PagerFragment.PagerOneSet;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Profil_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterMultipleRow.OnItemDonateClickListener, GroupAdapter.OnItemDonateClickListener, View.OnClickListener {

    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private EditText createGroup_ET;
    private Button createGroup_BT;
    private AdapterMultipleRow.OnItemDonateClickListener onItemDonateClickListener;
    private RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Group> listGroup=new ArrayList<>();
    private DatabaseHandler db;
    private String objectId;
    public static User user;
    private  AdapterMultipleRow adapter2;
    private  RelativeLayout illustrator_collaboration;
    public static ArrayList<Group> listParticiant=new ArrayList<>();
    private   RelativeLayout frame_nonetwork;
    private  LinearLayout reconnect;
    private RelativeLayout frame_expand;
    private LottieAnimationView animation_nonetwork;
    private int numbeitem=0;

    public static client_Profil_fragment newInstance() {
        client_Profil_fragment myFragment = new client_Profil_fragment();
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
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
        illustrator_collaboration = (RelativeLayout)inflate.findViewById(R.id.illustrator_collaboration);

        frame_expand = (RelativeLayout) inflate.findViewById(R.id.frame_expand);
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
        frame_nonetwork = (RelativeLayout) inflate.findViewById(R.id.frame_nonetwork);
        frame_nonetwork.setOnClickListener(this);
        reconnect = (LinearLayout) inflate.findViewById(R.id.reconnect);
        animation_nonetwork = (LottieAnimationView) inflate.findViewById(R.id.animation_nonetwork);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        onItemDonateClickListener = this;
        Initalize_RecyclerView();
        return inflate;
    }



    private void getProfilFromParse() {
        try {
        if (db.getUserCount() > 0 && Utils.isConnected(getContext())) {
            PagerOneSet=true;
            if (!swipeContainer.isRefreshing()) {
                swipeContainer.setRefreshing(true);
            }
            getGroupList();
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerview.addItemDecoration(dividerItemDecoration);

          adapter2 = new AdapterMultipleRow(getActivity(), listGroup, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
    }


    private void getGroupList() {

        Observable<ArrayList<Group>> todoObservable = Observable.create(new ObservableOnSubscribe<ArrayList<Group>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<Group>> emitter) throws Exception {
                try {
                    getData(emitter);
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });

        todoObservable.subscribe(observer);
    }


    Observer<ArrayList<Group>> observer = new Observer<ArrayList<Group>>() {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onNext(ArrayList<Group> value) {
            if (numbeitem>0){
                HideIllustration();
            }else {
                ShowIllustration();
            }
            adapter2.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
            HideNetworkView();
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {

        }
    };

    public ArrayList<Group> getData(final ObservableEmitter<ArrayList<Group>> emitter){
        FirebaseDatabase.getInstance().getReference().child("GroupCreated").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The PUSH ID OP WANTED
                listGroup.clear();
                listParticiant.clear();
                numbeitem=0;
                if (dataSnapshot.getChildrenCount()>0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String creatorId = postSnapshot.child("creatorid").getValue().toString();

                        if (creatorId.equalsIgnoreCase(user.get_id()) || postSnapshot.child("UsersParticiant").child(user.get_id()).exists()) {
                            String coutuser = Long.toString(postSnapshot.child("UsersParticiant").getChildrenCount());
                            numbeitem=numbeitem+1;
                            if (numbeitem==1){
                                listGroup.add(new Group("", "", "","", "", "", "", false, "", "","","",3));
                            }
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
                    ShowIllustration();
                }

                emitter.onNext(listGroup);
                emitter.onComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return listGroup;
    }



    /*
  * Get group list
  **/


    public void ShowIllustration(){
        swipeContainer.setRefreshing(false);
        frame_expand.setVisibility(View.INVISIBLE);
        frame_nonetwork.setVisibility(View.INVISIBLE);
        animation_nonetwork.setVisibility(View.INVISIBLE);
        reconnect.setVisibility(View.INVISIBLE);
        animation_nonetwork.setAnimation("loading.json");
        animation_nonetwork.loop(false);
        animation_nonetwork.playAnimation();
        illustrator_collaboration.setVisibility(View.VISIBLE);
    }

    public void HideIllustration(){
        swipeContainer.setRefreshing(false);
        illustrator_collaboration.setVisibility(View.INVISIBLE);
    }



    public void ShowNetworkView(){
        HideIllustration();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frame_nonetwork:
                    onRefresh();
                break;
        }
    }



    @Override
    public void onItemClick(int item, Group bo) {

    }

    @Override
    public void onJoinGroupClick(int item, final Group bo) {
        if (bo.getIdkey()!=null){
            Intent intent=new Intent(getActivity(), WriterActivity.class);
            Bundle bundle=new Bundle();
            Log.d("keiyid",bo.getIdkey());
            bundle.putString("keyid",bo.getIdkey());
            bundle.putSerializable("group",bo);
            bundle.putSerializable("fragment","collaboration");
            intent.putExtras(bundle);
            getActivity().startActivityForResult(intent,1);
        }
    }

    @Override
    public void onInviteFriends(int item, Group bo) {
        if (bo.getIdkey()!=null){
            if (Utils.isConnected(getActivity())){
            Intent intent=new Intent(getActivity(), InviteCollabActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("keyid",bo.getIdkey());
            bundle.putSerializable("group",bo);
            intent.putExtras(bundle);
            getActivity().startActivityForResult(intent,1);
        }else {
            Toast.makeText(getActivity(), Application.getContext().getResources().getString(R.string.dialog_network),Toast.LENGTH_LONG).show();
        }
        }

    }

}