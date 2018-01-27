package com.guilla.lyricswriter.fragment.pro;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guilla.lyricswriter.BO.Business;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.fragment.Updateable;


import de.hdodenhof.circleimageview.CircleImageView;

import static com.guilla.lyricswriter.Application.mKinveyClient;
import static com.guilla.lyricswriter.activity.ProfilProActivity.db;
import static com.guilla.lyricswriter.activity.ProfilProActivity.profilProActivity;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Profil_fragment extends Fragment implements Updateable,SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeContainer;
    private BroadcastReceiver statusReceiver;
    private IntentFilter mIntent;
    public static final String ARG_PLANET_NUMBER = "planet_number";
    private View inflate;
    private TextView username;
    private CircleImageView imageView;
    private TextView points;
    private TextView email;
    private TextView phone;
    private TextView sharepoint_generated;
    private TextView RIB_value;



    public static Pro_Profil_fragment newInstance() {
        Pro_Profil_fragment myFragment = new Pro_Profil_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_profile_pro, container, false);
        imageView=(CircleImageView)inflate.findViewById(R.id.picture_profil);
        username=(TextView) inflate.findViewById(R.id.username_login);
        email=(TextView) inflate.findViewById(R.id.email);
        phone=(TextView) inflate.findViewById(R.id.telephone);
        sharepoint_generated=(TextView) inflate.findViewById(R.id.sharepoint_value);
        RIB_value=(TextView) inflate.findViewById(R.id.RIB_value);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKinveyClient.user().logout().execute();
            }
        });

        if (isAdded()) {
            try {
                getProfilFromParse();
            }catch (NullPointerException e){

            }
           // CreatePromo();
        }

        return inflate;
    }

    @Override
    public  void update() {
    }

    private void getProfilFromParse() {
        final String objectid = db.getBusinessId();
        Business business = db.getBusiness(objectid);
        username.setText(business.get_officerName());
        email.setText(business.get_email());
        phone.setText(business.get_telephoneNumber());
        RIB_value.setText(business.get_RIB());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver2,
                new IntentFilter("eventRefresh"));
    }

    @Override
    public void onPause() {
        if(mIntent != null) {
            getActivity().unregisterReceiver(mMessageReceiver2);
            mIntent = null;
        }
        super.onPause();
    }

    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            updateProfil();
        }
    };

    public void updateProfil() {
        profilProActivity.runOnUiThread(new Runnable() {
            public void run() {
                getProfilFromParse();
            }
        });
    }


    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())){
            swipeContainer.setRefreshing(true);
            getProfilFromParse();
        }
    }

    private void CreatePromo() {}
}