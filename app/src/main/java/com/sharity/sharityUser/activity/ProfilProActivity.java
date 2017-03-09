package com.sharity.sharityUser.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.SimpleBackPage;
import com.sharity.sharityUser.fragment.client.client_Option_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_Historique_fragment;
import com.sharity.sharityUser.fragment.pro.Profil_pro_fragment;
import com.sharity.sharityUser.fragment.pro.Partenaire_Pro_fragment;

import static com.sharity.sharityUser.R.id.tab_option;
import static com.sharity.sharityUser.R.id.tab_utilisateur;


/**
 * Created by Moi on 07/05/2016.
 */
public class ProfilProActivity extends AppCompatActivity implements OnTabSelectListener {
    public static DatabaseHandler db;
    static int TOTAL_PAGES=3;
    ViewPager pager;
    LinearLayout circles;
    boolean isOpaque = true;
    private Toolbar toolbar;
    private BottomBar bottomBar;
    private TextView toolbarTitle;
    boolean start =true;
    public static ParseUser parseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pro);

        if (savedInstanceState == null) {
            db = new DatabaseHandler(this);
            parseUser=ParseUser.getCurrentUser();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            bottomBar = (BottomBar) findViewById(R.id.bottomBar);
            toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

            pager = (ViewPager) findViewById(R.id.pager);
            pager.setOffscreenPageLimit(0);
            pager.setAdapter(mViewPagerAdapter);
            pager.setCurrentItem(1,true);
            pager.setOnPageChangeListener(mPageChangeListener);
            mViewPagerAdapter.notifyDataSetChanged();
            buildTabs();

            //   pager.setPageTransformer(true, new CrossfadePageTransformer());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_drawer);


        }
    }

    private FragmentStatePagerAdapter mViewPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }

        // Return the Fragment associated with a specified position.
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return Pro_Historique_fragment.newInstance();
            } else if (position == 1) {
                return Profil_pro_fragment.newInstance();
            } else if (position == 2) {
                return Partenaire_Pro_fragment.newInstance();
            }

            return null;
        }


        // Remove a page for the given position. The adapter is responsible for removing the view from its container.
        @Override
        public void destroyItem(android.view.ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        };

        @Override
        // To update fragment in ViewPager, we should override getItemPosition() method,
        // in this method, we call the fragment's public updating method.
        public int getItemPosition(Object object) {
            if (object instanceof Pro_Historique_fragment) {
                ((Pro_Historique_fragment) object).update();
            }
            if (object instanceof Profil_pro_fragment) {
                ((Profil_pro_fragment) object).update();
            }

            if (object instanceof Partenaire_Pro_fragment) {
                ((Partenaire_Pro_fragment) object).update();
            }

            return super.getItemPosition(object);
        };
    };



    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            pager.setBackgroundColor(getResources().getColor(R.color.white));

            if (start){
                mViewPagerAdapter.notifyDataSetChanged();
                start=false;
            }
        }

        @Override
        public void onPageSelected(int position) {
            setIndicator(position);
            bottomBar.setInActiveTabColor(getResources().getColor(R.color.black));
            bottomBar.setActiveTabColor(getResources().getColor(R.color.red));
            if (position==0){
                toolbarTitle.setText("OPTIONS");
            }else if (position==1){
                toolbarTitle.setText("PROFIL");
            }else if (position==2){
                toolbarTitle.setText("PARTENAIRE");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "not available", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pager != null) {
            pager.clearOnPageChangeListeners();
        }
    }

    private void setIndicator(int index) {
        bottomBar.selectTabAtPosition(index);
    }

    private void buildTabs() {
        bottomBar.setOnTabSelectListener(this,true);
        setIndicator(1);
        bottomBar.setInActiveTabColor(getResources().getColor(R.color.black));
        bottomBar.setActiveTabColor(getResources().getColor(R.color.red));
    }

    //Bottom Bar onCLick
    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId){
            case tab_utilisateur:
                pager.setCurrentItem(0,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
            case R.id.tab_profil:
                pager.setCurrentItem(1,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
            case R.id.tab_historique:
                pager.setCurrentItem(2,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
        }
    }

    private Fragment getFragment(int pageValue) {
        SimpleBackPage page = SimpleBackPage.getPageValue(pageValue);
        Fragment fragment;
        try {
            fragment = (Fragment) page.getCls().newInstance();
            return fragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 102: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)   {
                    Partenaire_Pro_fragment Partenaire_Pro_fragment = (Partenaire_Pro_fragment) getSupportFragmentManager().findFragmentByTag("client_Partenaire_fragment");

                    if (Partenaire_Pro_fragment != null && Partenaire_Pro_fragment.isVisible()) {
                        Partenaire_Pro_fragment.update();
                    }
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



