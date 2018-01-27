package com.guilla.lyricswriter.activity.ActivityTutorial;

/*
* This application is on the copyright of Guilla.Lab and associates.
*
* */


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.activity.LoginActivity;


public class MainTutorialLoginActivity extends AppCompatActivity {

    static int TOTAL_PAGES=1;
    public static CallbackManager callbackManager;
    CustomViewPager pager;
    PagerAdapter pagerAdapter;
    LinearLayout circles;
    Button btnSkip;
    Button btnDone;
    Button btnNext;
    boolean isOpaque = true;
    RelativeLayout bottomLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.welcome_layout);


        btnSkip = Button.class.cast(findViewById(R.id.btn_skip));
        bottomLayout = (RelativeLayout)findViewById(R.id.button_layout);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTutorial();
            }
        });

        btnNext = Button.class.cast(findViewById(R.id.btn_next));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTutorial();
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });

        btnDone = Button.class.cast(findViewById(R.id.done));
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTutorial();
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });

        pager = (CustomViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlideAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
     //   pager.setPageTransformer(true, new CrossfadePageTransformer());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == TOTAL_PAGES-4){
                   pager.setPagingEnabled(false);
                    btnSkip.setVisibility(View.INVISIBLE);

                }else {
                    pager.setPagingEnabled(true);
                }
                if (position == TOTAL_PAGES - 2 && positionOffset > 0) {
                    if (isOpaque) {
                        pager.setBackgroundColor(Color.TRANSPARENT);
                        isOpaque = false;
                    }
                } else {
                    if (!isOpaque) {
                        pager.setBackgroundColor(getResources().getColor(R.color.primary_material_light));
                        isOpaque = true;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                    if (position == TOTAL_PAGES - 2) {
                        btnSkip.setVisibility(View.INVISIBLE);
                        btnNext.setVisibility(View.GONE);
                        btnDone.setVisibility(View.VISIBLE);

                    }else if (position == TOTAL_PAGES - 1){
                        btnSkip.setVisibility(View.INVISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                        btnDone.setVisibility(View.INVISIBLE);
                        bottomLayout.setVisibility(View.INVISIBLE);

                    }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buildCircles();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pager != null) {
            pager.clearOnPageChangeListeners();
        }
    }

    private void buildCircles() {
        circles = LinearLayout.class.cast(findViewById(R.id.circles));

        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for (int i = 0; i < TOTAL_PAGES - 1; i++) {
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.drawable.whiteindicator);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            circles.addView(circle);
        }

        setIndicator(0);
    }

    private void setIndicator(int index) {
        if (index < TOTAL_PAGES) {
            for (int i = 0; i < TOTAL_PAGES - 1; i++) {
                ImageView circle = (ImageView) circles.getChildAt(i);
                if (i == index) {
                    circle.setColorFilter(getResources().getColor(R.color.white));
                } else {
                    circle.setColorFilter(getResources().getColor(R.color.greylight));
                }
            }
        }
    }

    private void endTutorial() {
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }



    @Override
    public void onBackPressed() {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
    }

    private class ScreenSlideAdapter extends FragmentStatePagerAdapter {

        public ScreenSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            WelcomeScreenFragment welcomeScreenFragment = null;
            switch (position) {

                case 0:
                    fragment_Tutorial2 fragment_tutorial2 = new fragment_Tutorial2();
                    return fragment_tutorial2;

                case 1:
                    welcomeScreenFragment = WelcomeScreenFragment.newInstance(R.layout.tutorial_fragment4);
                    break;
            }

            return welcomeScreenFragment;
        }

        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 102: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }



            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}


