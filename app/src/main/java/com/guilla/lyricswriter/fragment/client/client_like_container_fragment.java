package com.guilla.lyricswriter.fragment.client;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.fragment.Updateable;
import com.guilla.lyricswriter.fragment.pagerLike.PagerFragment;


/**
 * Created by Moi on 14/11/15.
 */
public class client_like_container_fragment extends Fragment implements Updateable, PagerFragment.OnSelection,ProfilActivity.OnNotificationUpdateHistoric, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    PagerFragment.OnSelection onSelection;
    ImageView circle_slide1;
    ImageView circle_slide2;
    View inflate;
    public static SwipeRefreshLayout Historic_swipeContainer;
    private boolean isClient=false;
    public static client_like_container_fragment newInstance() {
        client_like_container_fragment myFragment = new client_like_container_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         if (getActivity() instanceof ProfilActivity){
            isClient=true;
            ((ProfilActivity) getActivity()).setHistoricListener(client_like_container_fragment.this);
            inflate = inflater.inflate(R.layout.fragment_history_container_client, container, false);
            //   TextSpawnTitle();
            Fragment currentFagment= getFragmentManager().findFragmentById(R.id.Fragment_container);
            if (currentFagment instanceof PagerFragment){
            }else {
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                PagerFragment fragTwo =  new PagerFragment();
                ft.add(R.id.Fragment_container, fragTwo);
                ft.commit();
            }
        }


        return inflate;
    }

    @Override
    public void update() {

    }

    /*
 * CallBack when historic pager is selected
 * */
    @Override
    public void OnSelect(int i) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    /*
    * Refresh historic when notification received foreground. //Disabled, uncomment line 577 in ProfilActivity
    * */
    @Override
    public void TaskOnNotification(String business, String sharepoints) {
        PagerFragment fragment2 = (PagerFragment) getChildFragmentManager().findFragmentById(R.id.content);
        fragment2.pager.setAdapter(fragment2.getAdapter());
        fragment2.getAdapter().FragmentOperation();
    }

    private void TextSpawnTitle(){
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String red = "1€ =";
        SpannableString redSpannable= new SpannableString(red);
        redSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, red.length(), 0);
        builder.append(redSpannable);

        String white = " 1SP";
        SpannableString whiteSpannable= new SpannableString(white);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#75c33c")), 0, white.length(), 0);
        builder.append(whiteSpannable);
    }


    @Override
    public void onRefresh() {         //   getProfil();
            PagerFragment fragment2 = (PagerFragment) getChildFragmentManager().findFragmentById(R.id.Fragment_container);
            fragment2.pager.setAdapter(fragment2.getAdapter());
            fragment2.getAdapter().FragmentOperation();
    }


     /*
    *  CallBack Dashboard Button allocated and get Sharepoint
    *
    * */
}