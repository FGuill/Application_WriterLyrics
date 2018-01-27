package com.guilla.lyricswriter.fragment.pro;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.activity.ProfilProActivity;
import com.guilla.lyricswriter.fonts.TextViewGeoManis;
import com.guilla.lyricswriter.fragment.Updateable;
import com.guilla.lyricswriter.fragment.pagerSearch.PagerFragment;


/**
 * Created by Moi on 14/11/15.
 */
public class History_container_fragment extends Fragment implements Updateable, PagerFragment.OnSelection,ProfilActivity.OnNotificationUpdateHistoric, View.OnClickListener {

    com.guilla.lyricswriter.fragment.pagerSearch.PagerFragment.OnSelection onSelection;
    Button buttonmap;
    ImageView circle_slide1;
    ImageView circle_slide2;
    View inflate;
    private TextViewGeoManis payment;
    public static History_container_fragment newInstance() {
        History_container_fragment myFragment = new History_container_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getActivity() instanceof ProfilProActivity) {
            inflate = inflater.inflate(R.layout.fragment_history_container_pro, container, false);
            buttonmap = (Button) inflate.findViewById(R.id.buttonmap);
            buttonmap.setOnClickListener(this);
        } else {
            ((ProfilActivity) getActivity()).setHistoricListener(History_container_fragment.this);
            inflate = inflater.inflate(R.layout.fragment_history_container_client, container, false);

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



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonmap:
                break;
        }
    }

    /*
    * Refresh historic when notification received foreground.
    * */
    @Override
    public void TaskOnNotification(String business, String sharepoints) {
        PagerFragment fragment2 = (PagerFragment) getChildFragmentManager().findFragmentById(R.id.content);
        fragment2.pager.setAdapter(fragment2.getAdapter());
        fragment2.getAdapter().FragmentOperation();
    }

    private void TextSpawnTitle(){
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String red = "PRO";
        SpannableString redSpannable= new SpannableString(red);
        redSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, red.length(), 0);
        builder.append(redSpannable);

        String white = "JECT";
        SpannableString whiteSpannable= new SpannableString(white);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#75c33c")), 0, white.length(), 0);
        builder.append(whiteSpannable);
        payment.setText(builder, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void OnSelect(int i) {

    }

    public void LaunchFragmentProject(Group group) {
        Log.d("KeyOf",group.getIdkey());
        FragmentManager fm = getChildFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.AnimationFadeFragment(getActivity(),fm,R.id.Fragment_container, Pro_Paiment_StepTwo_Classique_fragment.newInstance(group.getIdkey()),"Pro_Paiment_StepTwo_Classique_fragment", Fade.IN,true);
        }else {
            Utils.replaceFragmentWithAnimationVertical(R.id.Fragment_container, Pro_Paiment_StepTwo_Classique_fragment.newInstance(group.getIdkey()),fm,"Pro_Paiment_StepTwo_Classique_fragment",true);
        }
    }
}