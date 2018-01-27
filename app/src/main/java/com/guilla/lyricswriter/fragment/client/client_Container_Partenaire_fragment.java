package com.guilla.lyricswriter.fragment.client;


import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.guilla.lyricswriter.BO.Category;
import com.guilla.lyricswriter.BO.LocationBusiness;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterGridViewCategorie;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.fragment.MapCallback;
import com.guilla.lyricswriter.fragment.Updateable;

import java.util.ArrayList;

import static com.guilla.lyricswriter.activity.ProfilActivity.isShop;
import static com.guilla.lyricswriter.activity.ProfilActivity.permissionRuntime;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Container_Partenaire_fragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Updateable, MapCallback, ResultCallback<LocationSettingsResult> {

    protected static ArrayList<LocationBusiness> list_shop = new ArrayList<>();
    protected static ArrayList<LocationBusiness> list_shop_filtered = new ArrayList<LocationBusiness>();
    protected static ArrayList<Category> list_categorieReal = new ArrayList<Category>();

    protected static FrameLayout recyclerFrame;
    protected static double latitude = 0.0;
    protected static double longitude = 0.0;
    private double mlatitude=0.0;
    private double mlongitude=0.0;

    protected static Location mLastLocation;
    protected static Marker mCurrLocationMarker;
    private View inflate;
    private byte[] imageByte = null;
    protected static boolean on=false;
    protected static RelativeLayout frameCategorie;
    protected static LinearLayout search_layout;
    protected static View mViewcategorieColapse;
    protected static LayoutInflater vinflater;
    protected static GridView gridview;
    protected static ArrayList<Integer> images = new ArrayList<>();
    protected static GoogleMap mMap=null;
    int REQUEST_CHECK_SETTINGS = 100;
    protected static boolean isLocationUpdate=false;
    int size_listshopNew=0;
    public static LocationRequest mLocationRequest;
    public static GoogleApiClient mGoogleApiClient;
    public static AdapterGridViewCategorie gridViewCategorie;
    public static String categorie;


    public static client_Container_Partenaire_fragment newInstance() {
        client_Container_Partenaire_fragment myFragment = new client_Container_Partenaire_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    protected interface DataCallBack{
        public void onSuccess();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_partenaire_container_client, container, false);

        //We instantiate the List in first, seen by user first.
        client_Partenaire_list_fragment fragTwo = client_Partenaire_list_fragment.newInstance();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content, fragTwo, "client_Partenaire_list_fragment");
        ft.commit();


        return inflate;
    }

    // Callback to open map fragment when user click open map.
    @Override
    public void onOpen(ArrayList<LocationBusiness> data, boolean type) {
        FragmentManager fm = getChildFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.AnimationSlideFragment(getActivity(),fm,R.id.content, client_PartenaireMap_fragment.newInstance(data, type),"client_PartenaireMap_fragment", Gravity.BOTTOM,true);
        }else {
            Utils.replaceFragmentWithAnimationVertical(R.id.content, client_PartenaireMap_fragment.newInstance(data, type), fm, "client_PartenaireMap_fragment", true);
        }
    }

    // Callback close map when user click close map.
    @Override
    public void onClose() {
        try {
            ((ProfilActivity)getActivity()).onBackPressed();
        }catch (IllegalStateException e){
        }
    }

    @Override
    public void update() {
    }

    /*
    *  Google Api Location
    * */

    public synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
            get_Categorie();
        }catch (NullPointerException e){

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        OpenGPSettings();

        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {

                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();

                    Log.d("latitude", String.valueOf(latitude));
                   // geoPoint = new ParseGeoPoint(latitude, longitude);
                    client_Partenaire_list_fragment list_fragment = (client_Partenaire_list_fragment) getChildFragmentManager().findFragmentByTag("client_Partenaire_list_fragment");
                    if (list_fragment!=null && list_fragment.isVisible()) {
                        list_fragment.HideNetworkView();
                        list_fragment.ShowShop();
                    }
                }else {

                }
                startLocationUpdates();
            }
        }
    }

    // Trigger new location updates at interval
    public void startLocationUpdates() {
        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                isLocationUpdate=true;
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, gpsLocationListener);
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


   public LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            if (Utils.isConnected(getActivity())) {

            }

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            client_Partenaire_list_fragment list_fragment = (client_Partenaire_list_fragment) getChildFragmentManager().findFragmentByTag("client_Partenaire_list_fragment");
            if (list_fragment != null && list_fragment.isVisible()) {
                    if (isShop){
                        list_fragment.ShowShop();
                    }else {
                        list_fragment.ShowSPromotion();
                    }
            }
        }
    };

    private void OpenGPSettings(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(this);
    }





    public void RemoveLocationUpdate(){
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, gpsLocationListener);
                isLocationUpdate=false;
            }
        }
    }

    public void ChangeTitleActivity(String title){
        ((ProfilActivity) getActivity()).toolbarTitle.setText(title);
    }



    //Call Business data
    public void GetBusiness(final DataCallBack dataCallBack) {

    }

    //Call Promo data
    public void get_Promo(final DataCallBack dataCallBack) {


    }

    //Call Category data
    public void get_Categorie(final DataCallBack callBack) {
    }

    public void get_Categorie() {

    }

    public static ArrayList<Category> getList_categorie() {
        return list_categorieReal;
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {
                    //failed to show dialog
                }catch (NullPointerException e){

                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }


    public static boolean isLocationUpdate() {
        return isLocationUpdate;
    }


    // when fragment container start, we check if permission is granted and lauc
    @Override
    public void onStart() {
        if (mGoogleApiClient == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            Log.d("mGoogleApiClient", "Start connection");
                            buildGoogleApiClient();
                        }
                    } else {
                        permissionRuntime.Askpermission(permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION, permissionRuntime.Code_ACCESS_FINE_LOCATION);
                    }
                }
            }, 2000);
        }else {
            if (!mGoogleApiClient.isConnected()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buildGoogleApiClient();
                    }
                }, 1000);
                Log.d("mGoogleApiClient", "Disconnected");
            }
        }


        super.onStart();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, gpsLocationListener);
            }
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
}