package com.guilla.lyricswriter.fragment.client;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.guilla.lyricswriter.BO.LocationBusiness;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterGridViewCategorie;
import com.guilla.lyricswriter.Utils.AdapterPartenaireClient;
import com.guilla.lyricswriter.Utils.GPSservice;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.fragment.MapCallback;
import com.guilla.lyricswriter.fragment.Updateable;

import static com.guilla.lyricswriter.activity.ProfilActivity.isShop;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.frameCategorie;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.getList_categorie;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.gridViewCategorie;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.gridview;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.isLocationUpdate;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.list_categorieReal;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.list_shop;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.list_shop_filtered;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.mGoogleApiClient;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.mViewcategorieColapse;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.on;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.recyclerFrame;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.search_layout;
import static com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment.vinflater;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Partenaire_list_fragment extends Fragment implements Updateable, AdapterPartenaireClient.OnItemDonateClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterGridViewCategorie.OnItemGridCategorieClickListener  {

    private View inflate;
    protected AdapterPartenaireClient adapter2;
    protected  RecyclerView recyclerview;
    private MapCallback onSelect;
    private AdapterGridViewCategorie.OnItemGridCategorieClickListener onItemGridCategorieClickListener;
    private AdapterPartenaireClient.OnItemDonateClickListener onItemDonateClickListener;
    private byte[] imageByte = null;
    private Button map;
    private Button type;
    private SwipeRefreshLayout swipeContainer;
    private Button active_network;
    private RelativeLayout frame_nonetwork;
    private LottieAnimationView animation_nonetwork;
    private GPSservice gpSservice;
    private  RelativeLayout frame_expand;
    private RelativeLayout frame_progress_data;
    private LottieAnimationView animation_progress_data;
    public static client_Partenaire_list_fragment newInstance() {
        client_Partenaire_list_fragment myFragment = new client_Partenaire_list_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_partenaire_list_client, container, false);
        vinflater=inflater;
        recyclerview = (RecyclerView) inflate.findViewById(R.id.recyclerview);
        recyclerFrame = (FrameLayout) inflate.findViewById(R.id.recyclerFrame);
        frame_expand = (RelativeLayout) inflate.findViewById(R.id.frame_expand);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        frame_nonetwork = (RelativeLayout) inflate.findViewById(R.id.frame_nonetwork);
        active_network = (Button) inflate.findViewById(R.id.active_network);
        search_layout = (LinearLayout) inflate.findViewById(R.id.search_layout);
        animation_nonetwork = (LottieAnimationView) inflate.findViewById(R.id.animation_nonetwork);

        frame_progress_data = (RelativeLayout) inflate.findViewById(R.id.frame_progress_data);
        animation_progress_data = (LottieAnimationView) inflate.findViewById(R.id.animation_progress_data);

        map = (Button) inflate.findViewById(R.id.map);
        type = (Button) inflate.findViewById(R.id.type);
        swipeContainer.setOnRefreshListener(this);
        onItemDonateClickListener = this;
        onItemGridCategorieClickListener=this;
        search_layout.setOnClickListener(this);
        type.setText("PROMOTION");
        gpSservice=new GPSservice(getContext());

        Initalize_RecyclerView();
        StartLocation();

        Log.d("client_Partenaire_list","client_Partenaire_list");

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpSservice.getState();
                if (gpSservice.isGPSEnabled() && gpSservice.isNetworkEnabled()){
                    if (isShop) {
                        onSelect.onOpen(list_shop, true);
                    } else {
                        onSelect.onOpen(list_shop, false);
                    }
                }else {
                    Toast.makeText(getActivity(),"Veuillez activer votre réseau, ainsi que le GPS",Toast.LENGTH_LONG).show();
                }

            }
        });


        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpSservice.getState();
                if (gpSservice.isGPSEnabled() && gpSservice.isNetworkEnabled()){
                    if (!isLocationUpdate()){
                        if (mGoogleApiClient.isConnected()) {
                            ((client_Container_Partenaire_fragment) getParentFragment()).startLocationUpdates();
                        }
                    }
                    if (on){
                       ShowShop();
                    }else {
                        ShowSPromotion();
                    }
                }else {
                    ShowNetworkView();
                }

            }
        });


        return inflate;
    }

    @Override
    public void update() {
    }



    @Override
    public void onItemClick(int item, Object bo) {

    }



    public void Initalize_RecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        recyclerview.addItemDecoration(dividerItemDecoration);
        adapter2 = new AdapterPartenaireClient("client_Partenaire_list_fragment", true, getActivity(), list_shop, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof MapCallback) {
            onSelect = (MapCallback) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnSelection");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSelect = null;
    }



    @Override
    public void onRefresh() {
            gpSservice.getState();
        if (gpSservice.isGPSEnabled() && gpSservice.isNetworkEnabled()){
            swipeContainer.setRefreshing(true);
            if (!isShop) {
               ShowSPromotion();
            } else {
                ShowShop();
            }
        }else {
            ShowNetworkView();
        }
    }


    boolean issearch= true;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_layout:
                //Show  "Categorie" expand collapse //

                    if (issearch) {
                        frameCategorie = (RelativeLayout) inflate.findViewById(R.id.frame_expand);
                        mViewcategorieColapse = vinflater.inflate(R.layout.layout_editingsequence, frameCategorie, false);
                        frameCategorie.addView(mViewcategorieColapse);
                        gridview = (GridView) mViewcategorieColapse.findViewById(R.id.customgrid);
                        gridViewCategorie=new AdapterGridViewCategorie(getActivity(), list_categorieReal, onItemGridCategorieClickListener);

                        if (getList_categorie().isEmpty()){
                            ((client_Container_Partenaire_fragment)getParentFragment()).get_Categorie(new client_Container_Partenaire_fragment.DataCallBack() {
                                @Override
                                public void onSuccess() {
                                    gridViewCategorie.notifyDataSetChanged();
                                }
                            });
                        }else {
                            gridview.setAdapter(gridViewCategorie);
                        }
                        Utils.expand(mViewcategorieColapse);
                        issearch = false;
                    } else {
                        //  frameCategorie.removeView(mViewcategorieColapse);
                        Utils.collapse(mViewcategorieColapse);
                        issearch = true;
                    }
                break;

            case R.id.frame_nonetwork:
                if (mGoogleApiClient != null) {
                    gpSservice.getState();
                    if (gpSservice.isGPSEnabled() && gpSservice.isNetworkEnabled()){
                        if (mGoogleApiClient.isConnected()){
                            ((client_Container_Partenaire_fragment) getParentFragment()).startLocationUpdates();
                        }
                    }
                }
                break;
            case R.id.active_network:
                if (mGoogleApiClient != null) {
                    gpSservice.getState();
                    if (gpSservice.isGPSEnabled() && gpSservice.isNetworkEnabled()){
                      if (mGoogleApiClient.isConnected()){
                          ((client_Container_Partenaire_fragment) getParentFragment()).startLocationUpdates();
                      }
                }
                }
                break;
        }
    }


    public void StartLocation(){
        gpSservice = new GPSservice(getContext());
        gpSservice.getState();

        if (!gpSservice.isGPSEnabled()|| !Utils.isConnected(getContext())){
            ShowNetworkView();
        }else {
            if (isShop){
                ShowShop();
            }else {
                ShowSPromotion();
            }
        }
    }

    public void ShowShop(){
        adapter2 = new AdapterPartenaireClient("client_Partenaire_list_fragment", true, getActivity(), list_shop, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
        ((client_Container_Partenaire_fragment) getParentFragment()).GetBusiness(new client_Container_Partenaire_fragment.DataCallBack() {
            @Override
            public void onSuccess() {
                HideNetworkView();
                swipeContainer.setRefreshing(false);
                adapter2.notifyDataSetChanged();
            }
        });


        if (((ProfilActivity)getActivity()).pager.getCurrentItem()==3){
            ((client_Container_Partenaire_fragment) getParentFragment()).ChangeTitleActivity("SHOP");
        }

        type.setText("PROMOTION");
        type.setTextColor(getResources().getColor(R.color.blue));
        isShop = true;
        on=false;
    }

    public void ShowSPromotion(){
        adapter2 = new AdapterPartenaireClient("client_Partenaire_list_fragment", false, getActivity(), list_shop, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);

        if (((ProfilActivity)getActivity()).pager.getCurrentItem()==2) {
            ((client_Container_Partenaire_fragment) getParentFragment()).ChangeTitleActivity("PROMOTION");
        }

        type.setText("SHOP");
        type.setTextColor(getResources().getColor(R.color.green));

        ((client_Container_Partenaire_fragment) getParentFragment()).get_Promo(new client_Container_Partenaire_fragment.DataCallBack() {
            @Override
            public void onSuccess() {
                HideNetworkView();
                swipeContainer.setRefreshing(false);
                adapter2.notifyDataSetChanged();
            }
        });

        isShop = false;
        on=true;
    }



    public void ShowProgress(){
        frame_progress_data.setVisibility(View.VISIBLE);
        animation_progress_data.setAnimation("data2.json");
        animation_progress_data.loop(true);
        animation_progress_data.playAnimation();
    }

    public void HideProgress(){
        frame_progress_data.setVisibility(View.INVISIBLE);
        animation_progress_data.cancelAnimation();
    }

    public void ShowNetworkView(){
        frame_nonetwork.setVisibility(View.VISIBLE);
        active_network.setVisibility(View.VISIBLE);
        frame_expand.setVisibility(View.INVISIBLE);
        search_layout.setVisibility(View.INVISIBLE);
        frame_nonetwork.setOnClickListener(this);
        active_network.setOnClickListener(this);
        animation_nonetwork.setAnimation("data2.json");
        animation_nonetwork.loop(true);
        animation_nonetwork.playAnimation();
    }

    public void HideNetworkView(){
        if (active_network.getVisibility()==View.VISIBLE) {
            frame_nonetwork.setVisibility(View.INVISIBLE);
            active_network.setVisibility(View.INVISIBLE);
            frame_expand.setVisibility(View.VISIBLE);
            search_layout.setVisibility(View.VISIBLE);
            active_network.setOnClickListener(this);
            frame_nonetwork.setOnClickListener(this);
            animation_nonetwork.loop(false);
            animation_nonetwork.cancelAnimation();
        }
    }

    /**
     * GridView for Categories
     */



    private void DisplayItemFromCategorie(String selectedCategorie){
        list_shop.clear();
        if (isShop){
            adapter2 = new AdapterPartenaireClient("client_Partenaire_list_fragment", true, getActivity(), list_shop, onItemDonateClickListener);
        }else {
            adapter2 = new AdapterPartenaireClient("client_Partenaire_list_fragment", false, getActivity(), list_shop, onItemDonateClickListener);
        }
        Log.d("onItemCategorieClick",selectedCategorie);
        for (LocationBusiness object : list_shop_filtered){
            if(object.getCategorie()!=null) {
                if (object.getCategorie().equals(selectedCategorie) && !object.isoffset()) {
                    Log.d("onItemCategolickpassed", selectedCategorie);
                    list_shop.add(object);
                    break;
                }
            }
        }
        if (list_shop.size()>1) {
            LocationBusiness business = list_shop.get(list_shop.size() - 1);
            Double lat = business.get_latitude();
            Double lon = business.get_longitude();
            String description = business.getDescription();
            String prix = business.getPrix();
            String reduction = business.getReduction();
            String busines = business.get_businessName();
            String categorie = business.getCategorie();
            list_shop.add(new LocationBusiness(categorie,lat, lon, busines, 0 , description,prix,reduction, true));
        }

        recyclerview.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void onItemCategorieClick(int item, String categorie) {
        Log.d("onItemCategorielist",categorie);
        if (isLocationUpdate()){
            ((client_Container_Partenaire_fragment) getParentFragment()).RemoveLocationUpdate();
        }

        DisplayItemFromCategorie(categorie);
        Utils.collapse(mViewcategorieColapse);
    }
}