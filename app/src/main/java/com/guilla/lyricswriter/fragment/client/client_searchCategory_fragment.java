package com.guilla.lyricswriter.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.SearchCategory;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.DetailGroupActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import java.util.ArrayList;


/**
 * Created by Moi on 14/11/15.
 */
public class client_searchCategory_fragment extends Fragment implements Updateable, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private Button active_network;
    private RelativeLayout frame_nonetwork;
    private LottieAnimationView animation_nonetwork;
    private String indice;
    private RelativeLayout recyclerscreen;
    private EditText search_ET;
    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private ListView recyclerview;
    ArrayList<Group> listGroup = new ArrayList<>();
    private DatabaseHandler db;
    private String objectId;
    public static User user;
    private SearchCategory adapter2;
    private ImageView write;
    public static client_searchCategory_fragment newInstance(String indice) {
        client_searchCategory_fragment myFragment = new client_searchCategory_fragment();
        Bundle args = new Bundle();
        args.putString("indice",indice);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_search_keyword, container, false);

        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setEnabled(false);
        swipeContainer.setOnRefreshListener(this);
        recyclerscreen= (RelativeLayout)inflate.findViewById(R.id.framebackground);
        frame_nonetwork = (RelativeLayout) inflate.findViewById(R.id.frame_nonetwork);
        animation_nonetwork = (LottieAnimationView) inflate.findViewById(R.id.animation_nonetwork);

        db = new DatabaseHandler(getActivity());
        objectId = getUserObjectId(getActivity());
        user = db.getUser(objectId);
        recyclerview = (ListView) inflate.findViewById(R.id.recyclerview);
        recyclerview.setOnItemClickListener(this);
        search_ET = (EditText) inflate.findViewById(R.id.search_ET);
        search_ET.setVisibility(View.GONE);
        Initalize_RecyclerView();
        ShowCategories();
        if (this.isAdded()) {
            indice= getArguments().getString("indice");
            Log.d("indice",indice);
        }
        return inflate;

    }

    @Override
    public void update() {

    }

    public void Initalize_RecyclerView() {
        adapter2 = new SearchCategory(getActivity(), listGroup);
        recyclerview.setAdapter(adapter2);
    }

    //ShowUserGroups
    public void ShowCategories() {
        listGroup.add(new Group("Animals & Pets",R.drawable.category_animalpets));
        listGroup.add(new Group("Home & Garden",R.drawable.category_home));
        listGroup.add(new Group("Arts & Culture",R.drawable.category_artculture));
        listGroup.add(new Group("Miscellaneous",R.drawable.category_miscellanisous));
        listGroup.add(new Group("Careers & Work",R.drawable.category_careerswork));
        listGroup.add(new Group("Money & Finance",R.drawable.category_moneyfinance));
        listGroup.add(new Group("Clothing",R.drawable.category_clothing));
        listGroup.add(new Group("Productivity",R.drawable.category_productivity));
        listGroup.add(new Group("Computers",R.drawable.category_computers));
        listGroup.add(new Group("Requests",R.drawable.category_requests));
        listGroup.add(new Group("Electronics",R.drawable.category_electronics));
        listGroup.add(new Group("School & College",R.drawable.category_schoolcollege));
        listGroup.add(new Group("Food & Drink",R.drawable.category_fooddrink));
        listGroup.add(new Group("Social",R.drawable.category_social));
        listGroup.add(new Group("Health & Fitness",R.drawable.category_healthfitness));
        listGroup.add(new Group("Travelling",R.drawable.category_travel));
        swipeContainer.setRefreshing(false);
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(getActivity())){
            Log.d("m1stFragment","onRefresh");
            swipeContainer.setRefreshing(true);
            adapter2.notifyDataSetChanged();
            HideNetworkView();
        }else {
            swipeContainer.setRefreshing(false);
            Toast.makeText(getActivity(),"Veuillez activer votre wifi ou réseau",Toast.LENGTH_LONG).show();
            ShowNetworkView();
        }}


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frame_nonetwork:
                break;
            case R.id.active_network:
                if (Utils.isConnected2(getActivity())){
                    HideNetworkView();
                }
        }
    }

    public void ShowNetworkView(){
        frame_nonetwork.setVisibility(View.VISIBLE);
        animation_nonetwork.setVisibility(View.VISIBLE);
        recyclerscreen.setVisibility(View.INVISIBLE);
        frame_nonetwork.setOnClickListener(this);
        animation_nonetwork.setAnimation("loading.json");
        animation_nonetwork.loop(true);
        animation_nonetwork.playAnimation();
    }

    public void HideNetworkView(){
        frame_nonetwork.setVisibility(View.INVISIBLE);
        animation_nonetwork.setVisibility(View.INVISIBLE);
        recyclerscreen.setVisibility(View.VISIBLE);
        frame_nonetwork.setOnClickListener(this);
        animation_nonetwork.loop(false);
        animation_nonetwork.cancelAnimation();
    }

    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Group category = (Group) recyclerview.getAdapter().getItem(i);
        Intent intent=new Intent(getActivity(), DetailGroupActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("BOGroup",category);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);

    }

}