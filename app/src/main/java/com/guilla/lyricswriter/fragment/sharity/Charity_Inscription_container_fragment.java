package com.guilla.lyricswriter.fragment.sharity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.SignupPro.SignUpCharityPresenterImpl;
import com.guilla.lyricswriter.SignupPro.SignUpProPresenter;
import com.guilla.lyricswriter.SignupPro.SignUpProView;
import com.guilla.lyricswriter.activity.ProfilProActivity;
import com.guilla.lyricswriter.fragment.Inscription1CallBack;
import com.guilla.lyricswriter.fragment.Inscription2CallBack;
import com.guilla.lyricswriter.fragment.Updateable;
import com.guilla.lyricswriter.fragment.pagerInscriptionSharity.PagerFragment;


/**
 * Created by Moi on 14/11/15.
 */
public class Charity_Inscription_container_fragment extends Fragment implements Updateable, PagerFragment.OnSelection,Inscription1CallBack,Inscription2CallBack, SignUpProView {

    PagerFragment.OnSelection onSelection;
    Inscription1CallBack onSelect;
    private SignUpProPresenter presenter;
    ImageView circle_slide1;
    ImageView circle_slide2;
    View inflate;
    Object[] viewFirstSscreen;

    public static Charity_Inscription_container_fragment newInstance() {
        Charity_Inscription_container_fragment myFragment = new Charity_Inscription_container_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            inflate = inflater.inflate(R.layout.fragment_inscription_container_sharity, container, false);
        circle_slide1 = (ImageView) inflate.findViewById(R.id.circle_slide1);
        circle_slide2 = (ImageView) inflate.findViewById(R.id.circle_slide2);

        circle_slide1.setImageResource(R.drawable.circles_slide_on);
        circle_slide2.setImageResource(R.drawable.circles_slide_off);
        circle_slide2.setVisibility(View.INVISIBLE);
        circle_slide1.setVisibility(View.INVISIBLE);

        presenter = new SignUpCharityPresenterImpl(this);

        return inflate;
    }

    @Override
    public void update() {
    }


    @Override
    public void OnSelect(int i) {
        if (i == 0) {
            circle_slide1.setImageResource(R.drawable.circles_slide_on);
            circle_slide2.setImageResource(R.drawable.circles_slide_off);
        } else {
            circle_slide1.setImageResource(R.drawable.circles_slide_off);
            circle_slide2.setImageResource(R.drawable.circles_slide_on);
        }
    }


    public void CallGetPicture(){
        PagerFragment fragment2 = (PagerFragment) getChildFragmentManager().findFragmentById(R.id.content);
        fragment2.getAdapter().PerfomCallPicture();
    }

    /*order fields for second screen*/
   //View[] fields={business_name,chief_name,description,Siret,phone,address,RIB,email};


    @Override
    public void CallToRegister(Object[] viewsSecondScreen,Object[] adress) {

        Object[] screenfields = concat(viewFirstSscreen,viewsSecondScreen);
        presenter.validateCredentialsSharity("charite",screenfields,adress);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setUsernameError() {

    }

    @Override
    public void setPasswordError() {

    }

    @Override
    public void setRC3Error() {

    }

    @Override
    public void setBusinessNameError() {

    }

    @Override
    public void setOwnerNameError() {

    }

    @Override
    public void setPhoneError() {

    }

    @Override
    public void setAddressError() {

    }

    @Override
    public void setRIBError() {

    }

    @Override
    public void setEmailError() {

    }

    @Override
    public void navigateToHome() {
        IsSharity();
        startActivity(new Intent(getActivity(), ProfilProActivity.class));
        getActivity().finish();
    }

    private void IsSharity(){
        SharedPreferences pref = getActivity().getSharedPreferences("Pref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("status", "Sharity");
        editor.commit();
    }

    public Object[] concat(Object[] a, Object[] b) {
        int aLen = a.length;
        int bLen = b.length;
        Object[] c= new Object[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    @Override
    public void OnSelector(Object[] viewFirstscreen) {
        viewFirstSscreen= viewFirstscreen;
        PagerFragment fragment2 = (PagerFragment) getChildFragmentManager().findFragmentById(R.id.content);
        fragment2.pager.setCurrentItem(1);
        fragment2.getAdapter().notifyDataSetChanged();
    }
}