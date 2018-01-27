package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.fragment.client.client_Historique_fragment;
import com.guilla.lyricswriter.fragment.client.client_invitecollaboration_fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.guilla.lyricswriter.R.id.swipeContainer;
import static com.guilla.lyricswriter.activity.ProfilActivity.user;

/**
 * Created by Moi on 30/10/2017.
 */

public class UpdateFacebookFriends {
    Boolean hasFriends = false;
    private List<Group> usersList = new ArrayList<>();
    private List<Group> FriendsList = new ArrayList();
    public  FacebookFriends FacebookFriends;
    Context context;

    public interface FacebookFriends{
        public void setFacebookUsersToFireBase(List<Group> group);
    }

    public UpdateFacebookFriends(Context context) {
        this.context = context;
    }

    public void setInterfaceFacebookFriends(FacebookFriends friends){
        this.FacebookFriends=friends;
    }

    public void Allusers_FirebaseDB() {
        FirebaseDatabase.getInstance().getReference().child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The PUSH ID OP WANTED
                usersList.clear();
                int item = 0;
                dataSnapshot.getChildrenCount();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    item = item + 1;
                    String safe_userid = null;
                    String safe_username = null;
                    String safe_userpicprofil = null;
                    String location = null;
                    String facebookid = null;

                    if (dataSnapshot.getChildrenCount() > 0) {
                        safe_userid = postSnapshot.child("_id").getValue().toString();
                        facebookid = postSnapshot.child("_facebookid").getValue().toString();
                        safe_username = postSnapshot.child("_name").getValue().toString();
                        safe_userpicprofil = postSnapshot.child("picurl").getValue().toString();
                        Log.d("onLoadMore", postSnapshot.getKey());



                    if (user.get_id() != null && user.get_id() != safe_userid) {
                        usersList.add(new Group(postSnapshot.getKey(), facebookid, safe_username, safe_userpicprofil, 0, safe_userid, false, location));
                    }
                    }
                }
                if (dataSnapshot.getChildrenCount()>0) {
                    RequestFacebookFriends(FacebookFriends);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface interfaceFriends{
        public void hasFriends(Boolean hasFriends);
    }



    public boolean hasFacebookFriends(final interfaceFriends interfaceFriends) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.get_id()).child("FacebookFriends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The PUSH ID OP WANTED
                if (dataSnapshot.getChildrenCount() > 0) {
                    hasFriends = true;
                } else {
                    hasFriends = false;
                }
                interfaceFriends.hasFriends(hasFriends);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return false;
    }




    private boolean Facebook_Permissions_Friends() {
        Set<String> permission = com.facebook.AccessToken.getCurrentAccessToken().getPermissions();
        if (permission.contains("user_friends")) {
            return true;
        }
        return false;
    }


    private void RequestFacebookFriends(final FacebookFriends processFinish){
        if (Facebook_Permissions_Friends()) {
            FriendsList.clear();
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
                                    for (Group users : usersList){
                                        for (Group facebookFriends : names){
                                            if (users.getCreatorid().equalsIgnoreCase(facebookFriends.getCreatorid())){
                                                FriendsList.add(new Group(users.getIdkey(),users.getCreatorid(),users.getCreatorname(),users.getUser_picurl(),0,users.getFollow(),false,"location"));
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
                                                } else {
                                                    usersList.clear();
                                                    if (processFinish!=null) {
                                                        processFinish.setFacebookUsersToFireBase(FriendsList);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }else {
                                    usersList.clear();
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

    public List<Group> getFriendsList() {
        return FriendsList;
    }

}
