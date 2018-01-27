package com.guilla.lyricswriter.activity.ActivityTutorial;

/**
 * Created by Moi on 15/06/2016.
 */

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.guilla.lyricswriter.R;

import twitter4j.Twitter;

/**
 * Created by Moi on 01/12/15.
 */

/**
 * Created by Moi on 14/11/15.
 */



public class fragment_Tutorial2 extends Fragment {
    protected CoordinatorLayout coordinatorLayout;
    protected static Twitter twitter;
    protected int layout;
    View  inflate;
    LottieAnimationView animation_nonetwork;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        inflate = inflater.inflate(R.layout.tutorial__fragment2, container, false);
      //   animation_nonetwork=(LottieAnimationView)inflate.findViewById(R.id.animation_nonetwork);
       // ShowLottie();
        return inflate;
    }

    public void ShowLottie(){
        animation_nonetwork.setImageAssetsFolder("images/");
        animation_nonetwork.setAnimation("light.json");
        animation_nonetwork.loop(false);
        animation_nonetwork.playAnimation();
    }

}



