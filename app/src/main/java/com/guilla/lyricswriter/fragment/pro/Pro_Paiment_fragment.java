package com.guilla.lyricswriter.fragment.pro;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilla.lyricswriter.BO.UserLocation;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.activity.ProfilProActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Paiment_fragment extends Fragment implements Updateable,Pro_PaimentStepOne_fragment.OnChildPaymentSelection, ProfilProActivity.OnConfirmationPaiment {
    private View inflate;
    private Pro_PaimentStepOne_fragment.OnChildPaymentSelection onSelection;
    public static Pro_Paiment_fragment newInstance() {
        Pro_Paiment_fragment myFragment = new Pro_Paiment_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_paiment_pro, container, false);
        ((ProfilProActivity) getActivity()).setConfirmationListener(Pro_Paiment_fragment.this);

            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Pro_PaimentStepOne_fragment fragTwo = new Pro_PaimentStepOne_fragment();
            ft.add(R.id.Fragment_container, fragTwo);
            ft.commit();

        return inflate;
    }

    @Override
    public void update() {
    }


    /*
 * GridView user is selected.
 * */
    @Override
    public void OnSelectGrid(UserLocation user, int i, CircleImageView imageView) {
        FragmentManager fm = getChildFragmentManager();
        Pro_Paiment_StepTwo_fragment fragmentB = Pro_Paiment_StepTwo_fragment.newInstance(user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.AnimationFadeFragment(getActivity(),fm,R.id.Fragment_container,fragmentB,"Pro_Paiment_StepTwo_fragment", Fade.IN,false);
        }else {
            Utils.replaceFragmentWithAnimationVertical(R.id.Fragment_container, Pro_Paiment_StepTwo_fragment.newInstance(user),fm,"Pro_Paiment_StepTwo_fragment",true);
        }
    }



    /*
* Classique Paiment selected
* */
    @Override
    public void Classique() {
        FragmentManager fm = getChildFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.AnimationFadeFragment(getActivity(),fm,R.id.Fragment_container,new Pro_Paiment_StepTwo_Classique_fragment(),"Pro_Paiment_StepTwo_Classique_fragment", Fade.IN,true);
        }else {
            Utils.replaceFragmentWithAnimationVertical(R.id.Fragment_container,new Pro_Paiment_StepTwo_Classique_fragment(),fm,"Pro_Paiment_StepTwo_Classique_fragment",true);
        }

    }

    private void Display_Paiment_Confirmation(String montant, String clientName,boolean approved){
        FragmentManager fm = getChildFragmentManager();
        if (fm.getBackStackEntryCount()>0){
            Utils.replaceFragmentWithAnimationVertical(R.id.Fragment_container,Pro_Paiment_Confirmation_fragment.newInstance(montant,clientName,approved),fm,"Display_Paiment_Confirmation",false);
        }else {
            Utils.replaceFragmentWithAnimationVertical(R.id.Fragment_container,Pro_Paiment_Confirmation_fragment.newInstance(montant,clientName,approved),fm,"Display_Paiment_Confirmation",true);

        }
    }


    @Override
    public void TaskOnConfirmation(String amount, String clientName,boolean approved) {
        Display_Paiment_Confirmation(amount, clientName,approved);
    }
}