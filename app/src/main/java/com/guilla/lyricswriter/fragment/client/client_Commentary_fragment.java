package com.guilla.lyricswriter.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.BO.Commentary;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.UserJoined;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.CommentaryAdapter;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.LoginActivity;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.vinflater;
import static com.guilla.lyricswriter.fragment.client.client_Profil_fragment.user;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Commentary_fragment extends Fragment implements Updateable,ProfilActivity.OnNotificationUpdateProfil, SwipeRefreshLayout.OnRefreshListener, CommentaryAdapter.OnItemDonateClickListener, View.OnClickListener {

    public static int hascomment=0;
    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private EditText commentary_ET;
    private ImageView commentary_BT;
    private CommentaryAdapter.OnItemDonateClickListener onItemDonateClickListener;
    private RecyclerView recyclerview;
    private DatabaseReference mDatabase;
    private ArrayList<Commentary> listGroup=new ArrayList<>();
    private DatabaseHandler db;
    private String objectId;
    private CommentaryAdapter adapter2;
    private String idkey;
    private Group groupCreatorTips;
    private Commentary answerTo=null;
    private String mentioneduser = null;
    private String mentioneduserid=null;
    private DatabaseReference newRef2 =null;
    private String matchRow;
    private int rowToTarget=0;
    public static client_Commentary_fragment newInstance(String key,Group group) {
        client_Commentary_fragment myFragment = new client_Commentary_fragment();
        Bundle args = new Bundle();
        args.putSerializable("idkey",key);
        args.putSerializable("group",group);
        myFragment.setArguments(args);
        return myFragment;
    }

    public static client_Commentary_fragment newInstanceMatchRow(String key,Group group,String matchRow) {
        client_Commentary_fragment myFragment = new client_Commentary_fragment();
        Bundle args = new Bundle();
        args.putSerializable("idkey",key);
        args.putString("matchRow",matchRow);
        args.putSerializable("group",group);
        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        inflate = inflater.inflate(R.layout.activity_commentary, container, false);

        if (getArguments().getString("idkey")!=null){
            idkey=getArguments().getString("idkey");
            groupCreatorTips=(Group) getArguments().getSerializable("group");
        }
        if (getArguments().getString("matchRow")!=null){
            matchRow=getArguments().getString("matchRow");
        }
        db = new DatabaseHandler(getActivity());
        objectId = getUserObjectId(getActivity());
        user = db.getUser(objectId);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        commentary_ET=(EditText)inflate.findViewById(R.id.commentary_ET);
        commentary_BT=(ImageView) inflate.findViewById(R.id.commentary_BT);
        recyclerview = (RecyclerView)inflate.findViewById(R.id.recyclerview);
        commentary_BT.setOnClickListener(this);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        commentary_ET.setHint("Commenter en tant que " +user.get_name());

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

        InitRecyclerView();

        if (db.getUserCount() > 0 && Utils.isConnected(getContext())) {
            try {
                getCommentary();
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



    private void InitRecyclerView(){
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerview.addItemDecoration(itemDecorator);
        adapter2 = new CommentaryAdapter(getActivity(), listGroup, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
    }

    public void getCommentary() {

        mDatabase.child("Tips").child(idkey).child("commentary").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The PUSH ID OP WANTED
                listGroup.clear();
                ArrayList<UserJoined> userjoined=new ArrayList<UserJoined>();
                userjoined.clear();

                int RowCommentary=0;
                int numberCommentary=0;

                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()){
                    numberCommentary=numberCommentary+1;

                    String elapsedTimeTips="";
                    if (datasnapshot.child("timestamp").exists()) {
                        Long Tipstimestamp = (Long) datasnapshot.child("timestamp").getValue();
                        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        sfd.format(new Date(Tipstimestamp));
                        String sCertDate = sfd.format(new Date());
                        long now = new Date().getTime();
                        elapsedTimeTips = (String) DateUtils.getRelativeTimeSpanString(Tipstimestamp, now, 0);
                        Log.d("DATE", elapsedTimeTips);
                    }

                    String mentionuser=null;
                    String mentioneduser=null;
                    String mentioneduserid=null;


                    if (datasnapshot.child("mentioneduser").exists()){
                        mentionuser = datasnapshot.child("mentionuser").getValue().toString();
                        mentioneduser = datasnapshot.child("mentioneduser").getValue().toString();
                        mentioneduserid = datasnapshot.child("mentioneduserid").getValue().toString();
                    }

                    String commentary_userid = datasnapshot.child("commentary_userid").getValue().toString();
                    String commentary_username = datasnapshot.child("commentary_username").getValue().toString();
                    String commentary_usertext = datasnapshot.child("commentary_usertext").getValue().toString();
                    String commentary_userpicprofil = datasnapshot.child("commentary_userpicprofil").getValue().toString();
                    int type= Integer.parseInt(datasnapshot.child("type").getValue().toString());

                    int hasanswer=-1;
                    int Answer=0;

                    Answer = Integer.parseInt(Long.toString(datasnapshot.child("Answer").getChildrenCount()));
                    Log.d("answer",String.valueOf(Answer));
                    if (Answer>0) {
                        hasanswer=1;
                        Log.d("answer",String.valueOf(hasanswer));
                    }else {
                        hasanswer=0;
                        Log.d("answer",String.valueOf(hasanswer));
                    }

                  /*  if (matchRow!=null) {
                        Log.d("TipsKeyId",String.valueOf(groupCreatorTips.getIdkey()));
                        Log.d("CommentsKeyId",groupCreatorTips.getCommentaryKeyId());
                        Log.d("CommentaryKey",datasnapshot.getKey());

                        if (groupCreatorTips.getCommentaryKeyId() != null) {
                            if (groupCreatorTips.getCommentaryKeyId().equalsIgnoreCase(datasnapshot.getKey())) {
                                RowCommentary = numberCommentary;
                                Log.d("groupCreatorTipsKeyId",String.valueOf(RowCommentary));
                                rowToTarget = RowCommentary;
                                break;
                            }
                        }
                    }*/

                    listGroup.add(new Commentary(datasnapshot.getKey(),commentary_userid,commentary_username,mentionuser,mentioneduser,mentioneduserid,commentary_usertext,commentary_userpicprofil,elapsedTimeTips,type,hasanswer));

                    if (Answer>0){
                        Log.d("answer",String.valueOf(hasanswer));
                        int number=0;
                        for (DataSnapshot dataSnapshot1: datasnapshot.child("Answer").getChildren()){

                            number=number+1;

                        /*    if (matchRow!=null) {
                                if (groupCreatorTips.getAnswerKeyId() != null) {
                                    Log.d("RowBB",String.valueOf(groupCreatorTips.getAnswerKeyId()));
                                    String answer_idkey = dataSnapshot1.child("answer_idkey").getValue().toString();
                                    if (groupCreatorTips.getAnswerKeyId().equalsIgnoreCase(answer_idkey)) {
                                        Log.d("RowBB",String.valueOf(RowCommentary));
                                        rowToTarget = rowToTarget + 1;
                                    }
                                }
                            }*/

                            String elapsedTimeTipsAnswer="";
                            if (dataSnapshot1.child("timestamp").exists()) {
                                Long Tipstimestamp = (Long) dataSnapshot1.child("timestamp").getValue();
                                SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                sfd.format(new Date(Tipstimestamp));
                                String sCertDate = sfd.format(new Date());
                                long now = new Date().getTime();
                                elapsedTimeTipsAnswer = (String) DateUtils.getRelativeTimeSpanString(Tipstimestamp, now, 0);
                                Log.d("DATE", elapsedTimeTipsAnswer);
                            }

                            if (dataSnapshot1.child("mentioneduser").exists()){
                                mentionuser = dataSnapshot1.child("mentionuser").getValue().toString();
                                mentioneduser = dataSnapshot1.child("mentioneduser").getValue().toString();
                                mentioneduserid = dataSnapshot1.child("mentioneduserid").getValue().toString();
                            }

                            commentary_userid = dataSnapshot1.child("commentary_userid").getValue().toString();
                            commentary_username = dataSnapshot1.child("commentary_username").getValue().toString();
                            commentary_usertext = dataSnapshot1.child("commentary_usertext").getValue().toString();
                            commentary_userpicprofil = dataSnapshot1.child("commentary_userpicprofil").getValue().toString();
                            type= Integer.parseInt(dataSnapshot1.child("type").getValue().toString());
                            if (number==Answer){
                                Log.d("answer",String.valueOf(number));
                                listGroup.add(new Commentary(datasnapshot.getKey(),commentary_userid,commentary_username,mentionuser,mentioneduser,mentioneduserid,commentary_usertext,commentary_userpicprofil,elapsedTimeTipsAnswer,type,1));
                            }else {
                                listGroup.add(new Commentary(datasnapshot.getKey(),commentary_userid,commentary_username,mentionuser,mentioneduser,mentioneduserid,commentary_usertext,commentary_userpicprofil,elapsedTimeTipsAnswer,type,0));
                            }

                        }
                    }
                }

                adapter2.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
                if (matchRow!=null) {
                    recyclerview.scrollToPosition(rowToTarget);
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





    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())) {
            swipeContainer.setRefreshing(true);
            getCommentary();
        }
    }



    @Override
    public void TaskOnNotification(String business, String sharepoints) {
     /*   Popup_onNotification onNotification=new Popup_onNotification();
        if (dummyanchor!=null){
            onNotification.displayPopupWindow(dummyanchor,getActivity(),"","");
        }*/
    }


    private void CreateCommentary() {

        if (commentary_ET.getText().toString().length()>0) {

            Commentary group=null;

            if (answerTo!=null && commentary_ET.getText().toString().contains("@")){
                newRef2 = mDatabase.child("Tips").child(idkey).child("commentary").child(answerTo.getCommentary_idkey()).child("Answer").push();
                newRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot.getValue());
                    }

                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                group = new Commentary(groupCreatorTips.getIdkey(),answerTo.getCommentary_idkey(),newRef2.getKey(),user.get_id(),user.get_name(),user.get_name(),answerTo.getCommentary_username(),answerTo.getCommentary_userid(),commentary_ET.getText().toString(),user.getPicurl(),groupCreatorTips.getGroup_categorie(), ServerValue.TIMESTAMP,1);
                final Commentary finalGroup2 = group;
                newRef2.setValue(group, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            System.out.println("Data could not be saved " + databaseError.getMessage());
                        } else {
                            newRef2.child("timestamp").setValue(ServerValue.TIMESTAMP);
                            newRef2 =  FirebaseDatabase.getInstance().getReference().child("user").child(answerTo.getCommentary_userid()).child("Mention").child("comments").push();
                            newRef2.setValue(finalGroup2, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if (databaseError != null) {
                                                    } else {
                                                        hascomment=1;
                                                        getCommentary();
                                                        commentary_ET.setHint(" Comment as " +user.get_name());
                                                        answerTo=null;
                                                    }
                                                }
                             });
                        }
                    }
                });

            }else {
                newRef2 = FirebaseDatabase.getInstance().getReference().child("Tips").child(idkey).child("commentary").push();
                newRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot.getValue());
                    }

                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Log.d("answer",String.valueOf(groupCreatorTips.getCreatorid()));
                group = new Commentary(groupCreatorTips.getIdkey(),newRef2.getKey(),user.get_id(),user.get_name(),user.get_name(),groupCreatorTips.getCreatorname(),groupCreatorTips.getCreatorid(),commentary_ET.getText().toString(),user.getPicurl(),groupCreatorTips.getGroup_categorie(), ServerValue.TIMESTAMP,0);

                final Commentary finalGroup = group;
                final Commentary finalGroup1 = group;
                newRef2.setValue(group, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            System.out.println("Data could not be saved " + databaseError.getMessage());
                        } else {
                            Log.d("Data saved", "saved");
                            newRef2.child("timestamp").setValue(ServerValue.TIMESTAMP);
                            newRef2 =  FirebaseDatabase.getInstance().getReference().child("user").child(groupCreatorTips.getCreatorid()).child("Mention").child("comments").push();
                            newRef2.setValue(finalGroup1, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                } else {
                                                    hascomment=1;
                                                    getCommentary();
                                                    answerTo = null;
                                                    commentary_ET.setHint(" Comment as " +user.get_name());
                                                }
                                            }
                                        });

                        }
                    }
                });

            }


        }

        else {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.commentary_BT:
                CreateCommentary();
                break;
        }
    }



    @Override
    public void onItemClick(int item, Commentary bo) {

    }

    @Override
    public void onAnswerClick(int item, Commentary bo) {
        answerTo=bo;
        commentary_ET.setText("@"+bo.getCommentary_username()+" ");
        int length = commentary_ET.getText().toString().length();
        commentary_ET.setFocusable(true);
        commentary_ET.setFocusableInTouchMode(true);
        commentary_ET.requestFocus();
        commentary_ET.setSelection(length);
        showVirturalKeyboard();
    }

    private void showVirturalKeyboard(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                if(m != null){
                    // m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);

                }
            }

        }, 100);
    }
}