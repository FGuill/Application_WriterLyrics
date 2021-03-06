package com.guilla.lyricswriter.fragment.pro;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.guilla.lyricswriter.BO.Business;
import com.guilla.lyricswriter.BO.UserLocation;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.LocationUserActivity;
import com.guilla.lyricswriter.fragment.Updateable;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Partenaire_fragment extends Fragment implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
         OnMapReadyCallback,Updateable {


    private double myBusiness_latitude=0.0;
    private double myBusiness_longitude=0.0;

    private int counter=0;
    private String objectiD;
    private Handler mHandler = new Handler();
    private DatabaseHandler db;
    private View mView;
    public static long NOTIFY_INTERVAL = 10000; //10s = 10 000ms
    public static boolean ServiceisWorking=false;
    public static Timer mTimer = null;
    private GoogleApiClient mGoogleApiClient;
    private View inflate;
    private MapView mapView;
    private GoogleMap mMap;
    private List<UserLocation> locationUser=new ArrayList<>();
    private Button userListBT;
    private ImageView imageView;
        public static Pro_Partenaire_fragment newInstance() {
        Pro_Partenaire_fragment myFragment = new Pro_Partenaire_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_partenaire, container, false);
            try {
                mapView = (MapView) inflate.findViewById(R.id.map);
                userListBT=(Button)inflate.findViewById(R.id.userlist);
                imageView=(ImageView)inflate.findViewById(R.id.imgMyLocation);
                imageView.setVisibility(View.VISIBLE);
                mapView.onCreate(savedInstanceState);
                mapView.onResume();
                mapView.getMapAsync(this);

                if (mGoogleApiClient==null){
                    buildGoogleApiClient();
                }


        userListBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), LocationUserActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("listuser",(Serializable)locationUser);
                intent.putExtra("Bundle",bundle);
                startActivity(intent);
            }
        });

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myBusiness_latitude!=0.0 && myBusiness_longitude!=0.0){
                            LatLng sydney = new LatLng(myBusiness_latitude, myBusiness_longitude);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
                        }
                    }
                });

            }catch (NullPointerException e){
            }
        return inflate;
    }

    @Override
    public void update() {
                mapView.getMapAsync(this);
        }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        db = new DatabaseHandler(getActivity());

        try {
            if (db.getBusinessCount()>0) {
                objectiD = db.getBusinessId();
                Business business = db.getBusiness(objectiD);
                myBusiness_latitude=Double.valueOf(business.getLatitude());
                myBusiness_longitude=Double.valueOf(business.get_longitude());
               // geoPoint=new ParseGeoPoint(Double.valueOf(business.getLatitude()),Double.valueOf(business.get_longitude()));
                imageView.performClick();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LocateUsers();
                }
            }, 100);
        }catch (NullPointerException e){

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }



    private void LocateUsers(){
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            Log.v("Recreate Timer", "recreat");
            ServiceisWorking=true;
            mTimer = new Timer();
        }
        // schedule task
        try {
            mTimer.scheduleAtFixedRate(new DisasterTimerCheck(), 0, NOTIFY_INTERVAL);

        }catch (IllegalStateException e){
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new DisasterTimerCheck(), 0, NOTIFY_INTERVAL);

        }
}

    class DisasterTimerCheck extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (Utils.isConnected(getApplicationContext())) {
                        if (mView != null && mView.isShown()) {
                        } else {
                            if (ServiceisWorking) {
                                locationUser.clear();
                                Log.v("isChecking", "ServiceisWorking");
                                CheckUser();
                            } else {
                                mTimer.cancel();
                                ServiceisWorking = false;
                                Log.v("isChecking", "ServiceisStopped");
                            }
                        }
                    }
                }
            });
        }
    }

    public void CheckUser() {}

        @Override
        public void onDestroy(){
            if (mTimer!=null){
                mTimer.cancel();
            }
            super.onDestroy();
        }

}