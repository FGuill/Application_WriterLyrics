package com.guilla.lyricswriter.activity.ActivityTutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import twitter4j.auth.RequestToken;

public class WelcomeScreenFragment extends Fragment {

    final static String LAYOUT_ID = "layoutId";

    protected static RequestToken requestToken;
    public static final int WEBVIEW_REQUEST_CODE = 100;

    protected int layout;

    RelativeLayout smsLayout;

    public static String consumerKey = null;
    public static String consumerSecret = null;
    public static String callbackUrl = null;
    public static String oAuthVerifier = null;

    public static WelcomeScreenFragment newInstance(int layoutId) {
        WelcomeScreenFragment pane = new WelcomeScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        pane.setArguments(bundle);
        return pane;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
       ViewGroup inflate = (ViewGroup) inflater.inflate(getArguments().getInt(LAYOUT_ID, -1), container, false);
        layout = getArguments().getInt(LAYOUT_ID, -1);

        return inflate;

    }
}