package com.guilla.lyricswriter.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.guilla.lyricswriter.BO.Drawer;
import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterDrawer;
import com.guilla.lyricswriter.Utils.DbBitmapUtility;
import com.guilla.lyricswriter.Utils.LocationUser;
import com.guilla.lyricswriter.Utils.PermissionRuntime;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.fonts.TextViewLoveStory;
import com.guilla.lyricswriter.fragment.client.client_Container_Mission_fragment;
import com.guilla.lyricswriter.fragment.client.client_Container_Partenaire_fragment;
import com.guilla.lyricswriter.fragment.client.client_Profil_container_fragment;
import com.guilla.lyricswriter.fragment.client.client_like_container_fragment;
import com.guilla.lyricswriter.fragment.client.client_like_fragment;
import com.guilla.lyricswriter.fragment.pro.History_container_fragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

import bolts.AppLinks;

import static com.guilla.lyricswriter.Application.mAuth;


/**
 * Created by Moi on 07/05/2016.
 */
public class ProfilActivity extends AppCompatActivity implements OnTabSelectListener, View.OnClickListener {

    public static DatabaseHandler db;
    private IntentFilter mIntent;
    static int TOTAL_PAGES = 3;
    public ViewPager pager;
    private LocationManager manager;
    private Toolbar toolbar;
    private BottomBar bottomBar;
    public TextViewLoveStory toolbarTitle;
    private boolean start = true;
    public static ProfilActivity clientProfilActivity;
    private DrawerLayout drawer_layout;
    private ListView myDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ArrayList<Drawer> drawersItems = new ArrayList<Drawer>();
    private AdapterDrawer adapter;
    public OnNotificationUpdateProfil onNotificationUpdateProfil;
    public OnNotificationUpdateHistoric onNotificationUpdateHistoric;
    public OnUpdateTipsProfil OnUpdateTipsProfil;
    private ImageView ic_addfriends;

    private boolean start1=false;
    private boolean start2=false;
    private boolean start3=false;
    MyPagerAdapter mViewPagerAdapter;
    public static LocationUser locationUser=null;
    public static PermissionRuntime permissionRuntime;
    public static User user;
    public static boolean isShop = true;
    private BottomBarTab nearby;
    private int requestCode;
    public static FloatingActionButton myFAB;


    public interface OnNotificationUpdateHistoric {
        void TaskOnNotification(String business, String sharepoints);
    }

    public interface OnNotificationUpdateProfil {
        void TaskOnNotification(String business, String sharepoints);
    }

    public interface OnUpdateTipsProfil {
        void UpdateTips();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (actionBarDrawerToggle != null) {
            actionBarDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (actionBarDrawerToggle != null) {
            actionBarDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
            if (targetUrl != null) {
                Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
            }

            manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            permissionRuntime = new PermissionRuntime(ProfilActivity.this);

            clientProfilActivity = this;
            db = new DatabaseHandler(this);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            bottomBar = (BottomBar) findViewById(R.id.bottomBar);
            toolbarTitle = (TextViewLoveStory) findViewById(R.id.toolbar_title);
            ic_addfriends= (ImageView) findViewById(R.id.ic_addfriends);

            /*
            * Get local database to display nav drawer including Profil picture etc
           **/

            Bitmap PictureProfile=null;
            String name=null;
            try {
                DatabaseHandler db = new DatabaseHandler(ProfilActivity.this);
                String objectId = getUserObjectId(ProfilActivity.this);
                user = db.getUser(objectId);
                String images = user.getPicurl();

                if (images.length()>0){
                byte[] image = DbBitmapUtility.StringToByteArray(images);
                PictureProfile = DbBitmapUtility.getImage(image);
                }else {
                     PictureProfile = BitmapFactory.decodeResource(ProfilActivity.this.getResources(),
                            R.mipmap.ic_launcher);
                }
                name= user.get_name();
            }catch (CursorIndexOutOfBoundsException e){
                mAuth.getInstance().signOut();
                Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Log.d("DB", "CursorIndexOutOfBoundsException");

            }catch (NullPointerException e){
                mAuth.getInstance().signOut();
                Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Log.d("DB", "NullPointerException");
            }


            drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawersItems.add(0, new Drawer(PictureProfile, name, 0));
            drawersItems.add(1, new Drawer(null, getString(R.string.disconnect), 1));

            myDrawer = (ListView) findViewById(R.id.my_drawer);
            adapter = new AdapterDrawer(ProfilActivity.this, drawersItems);
            myFAB=(FloatingActionButton)findViewById(R.id.myFAB);
            myFAB.setOnClickListener(this);
            myDrawer.setAdapter(adapter);

            pager = (ViewPager) findViewById(R.id.pager);
            pager.setOffscreenPageLimit(4);
            mViewPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(mViewPagerAdapter);
            pager.setCurrentItem(0, true);
            pager.setOnPageChangeListener(mPageChangeListener);
            mViewPagerAdapter.notifyDataSetChanged();
            buildTabs();

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            actionBarDrawerToggle = new ActionBarDrawerToggle(ProfilActivity.this, drawer_layout,
                    toolbar, R.string.open, R.string.close) {
                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu();
                }
                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu();
                }
            };
            drawer_layout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
            actionBarDrawerToggle.syncState();

            //Navigation draw item Click
            myDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            mAuth.getInstance().signOut();
                            Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 2:

                            break;
                        case 3:

                            break;
                        case 4:

                            break;

                        case 5:
                            //Disconnection

                            break;
                    }
                }
            });

            if (getIntent().getExtras()!=null){
                requestCode =getIntent().getExtras().getInt("notification");

                if (requestCode == 0) {
                    nearby = bottomBar.getTabWithId(R.id.tab_partenaire);
                    nearby.setBadgeCount(1);

                }
                if (requestCode == 1) {
                    // pager.setCurrentItem(2,true);
                    nearby = bottomBar.getTabWithId(R.id.tab_partenaire);
                    nearby.setBadgeCount(1);

                }
                if (requestCode == 2) {
                    nearby = bottomBar.getTabWithId(R.id.tab_partenaire);
                    nearby.setBadgeCount(1);
                }
            }

            /*
            * Parse Live Query permit to listen live Update on CISSTransaction.
            * if a payment transaction is approved, it inform the user
            * */

        }

    }


    public ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            pager.setBackgroundColor(getResources().getColor(R.color.white));
            if (position == 0) {
                if (start) {
                    final client_Profil_container_fragment client_Profil_container_fragment=(client_Profil_container_fragment)mViewPagerAdapter.getRegisteredFragment(position);
                    if (client_Profil_container_fragment!=null){
                        if (client_Profil_container_fragment.isAdded()){
                            //Permit to call the update function in fragment to reanimate circleprogress
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    client_Profil_container_fragment.onRefresh();

                                }
                            }, 1000);
                        }
                    }
                    start = false;
                }
            }

            if (position == 2) {
            }
        }

        @Override
        public void onPageSelected(int position) {
            setIndicator(position);
            if (position!=1){
                bottomBar.setInActiveTabColor(getResources().getColor(R.color.greyfonce));
                bottomBar.setActiveTabColor(getResources().getColor(R.color.orange));
                bottomBar.setBadgeBackgroundColor(getResources().getColor(R.color.orange));
            }

          /*  if (position == 0) {
                toolbarTitle.setText(getString(R.string.search));
                if (getIsFacebook(ProfilActivity.this)) {
                    ic_addfriends.setVisibility(View.VISIBLE);//set Visible
                    ic_addfriends.setClickable(true);
                    ic_addfriends.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String appLinkUrl, previewImageUrl;
                            appLinkUrl = "https://fb.me/1504667966290049";
                            previewImageUrl = "http://www.hostingpics.net/viewer.php?id=998362safetychecklogo.png";

                            if (AppInviteDialog.canShow()) {
                                AppInviteContent content = new AppInviteContent.Builder()
                                        .setApplinkUrl(appLinkUrl)
                                        .setPreviewImageUrl(previewImageUrl)
                                        .build();
                                AppInviteDialog.show(ProfilActivity.this, content);
                            }
                        }
                    });

                }

            }*/  if (position == 0) {
                ic_addfriends.setVisibility(View.INVISIBLE);
                toolbarTitle.setText(getString(R.string.tips));
                if (!start1) {
                    final client_Profil_container_fragment client_Profil_container_fragment = (client_Profil_container_fragment) mViewPagerAdapter.getRegisteredFragment(position);
                    if (client_Profil_container_fragment != null) {
                        if (client_Profil_container_fragment.isAdded()) {
                            //Permit to call the update function in fragment to reanimate circleprogress
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    client_Profil_container_fragment.onRefresh();

                                }
                            }, 500);
                        }
                    }
                start1=true;
                }
            } else if (position == 1) {
                ic_addfriends.setVisibility(View.INVISIBLE);
            //    toolbarTitle.setText(getString(R.string.likes));
                if (!start2) {
                    final client_like_container_fragment client_Profil_container_fragment=(client_like_container_fragment)mViewPagerAdapter.getRegisteredFragment(position);
                if (client_Profil_container_fragment!=null) {
                    if (client_Profil_container_fragment.isAdded()) {
                        //Permit to call the update function in fragment to reanimate circleprogress
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                client_Profil_container_fragment.onRefresh();

                            }
                        }, 500);
                    }
                start2=true;
                }
                }
            } else if (position == 2) {
                ic_addfriends.setVisibility(View.INVISIBLE);
                toolbarTitle.setText(getString(R.string.profil));
                if (!start3) {
                    final client_Container_Mission_fragment client_like_fragment = (client_Container_Mission_fragment) mViewPagerAdapter.getRegisteredFragment(position);
                    if (client_like_fragment != null) {
                        if (client_like_fragment.isAdded()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        client_like_fragment.onRefresh();
                                }
                            }, 400);
                        }
                    }
                    start3=true;
                }

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
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void setIndicator(int index) {
        bottomBar.selectTabAtPosition(index);
    }

    private void buildTabs() {
        bottomBar.setOnTabSelectListener(this, true);
        setIndicator(0);

        bottomBar.setInActiveTabColor(getResources().getColor(R.color.greyfonce));
        bottomBar.setActiveTabColor(getResources().getColor(R.color.orange));
    }

    //Bottom Bar onCLick
    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
          /*  case tab_option:
                pager.setCurrentItem(0, true);
               // OnUpdateTipsProfil.UpdateTips();
                // updateProfil();
                break;*/
            case R.id.tab_profil:
                pager.setCurrentItem(0, true);
                //updateProfil();
                if (nearby!=null) {
                    nearby.removeBadge();
                    nearby=null;
                }
                break;
            case R.id.tab_partenaire:
                pager.setCurrentItem(1, true);
                final client_like_container_fragment client_like_fragment=(client_like_container_fragment)mViewPagerAdapter.getRegisteredFragment(1);

                if (nearby!=null) {
                    if (requestCode == 1) {
                        if (client_like_fragment != null) {
                            if (client_like_fragment.isAdded()) {
                                client_like_fragment.onRefresh();
                            }
                        }


                        nearby.removeBadge();
                        nearby = null;
                    }
                }
                // updateProfil();
                break;
            case R.id.tab_mission:
                pager.setCurrentItem(2, true);
                // updateProfil();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int result=data.getIntExtra("result",0);
                if (result==0){

                }else {
                    if (pager.getCurrentItem() == 0) {
                        client_Profil_container_fragment client_Profil_container_fragment = (client_Profil_container_fragment) mViewPagerAdapter.getRegisteredFragment(pager.getCurrentItem());
                        if (client_Profil_container_fragment != null) {
                            client_Profil_container_fragment.onRefresh();
                        }
                    }else  if (pager.getCurrentItem() == 2) {
                        client_Container_Mission_fragment client_Container_Mission_fragment = (client_Container_Mission_fragment) mViewPagerAdapter.getRegisteredFragment(pager.getCurrentItem());
                        if (client_Container_Mission_fragment != null) {
                            client_Container_Mission_fragment.onRefresh();
                        }
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 102: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                       if (pager.getCurrentItem() == 3) {
                           client_Container_Partenaire_fragment clientProfilFragment = (client_Container_Partenaire_fragment) mViewPagerAdapter.getRegisteredFragment(pager.getCurrentItem());
                           clientProfilFragment.buildGoogleApiClient();
                       }
                        Log.d("Network granted", "passed");

                }else {
                    // permission was not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ProfilActivity.this, permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION)) {
                        Utils.showDialogPermission(ProfilActivity.this,"L'application Sharity recquiert votre localisation pour le service de paiment et d'achat à proximité","Permission",false, new Utils.Click() {
                            @Override
                            public void Ok() {
                                ActivityCompat.requestPermissions(ProfilActivity.this,
                                        new String[]{permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION},
                                        permissionRuntime.Code_ACCESS_FINE_LOCATION);
                            }

                            @Override
                            public void Cancel() {

                            }
                        });
                    } else {

                    }
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static ProfilActivity getInstance() {
        return clientProfilActivity;
    }

    //Notification handler
    public void setProfilListener(ProfilActivity.OnNotificationUpdateProfil activityListener) {
        this.onNotificationUpdateProfil = activityListener;
    }

    public void setHistoricListener(ProfilActivity.OnNotificationUpdateHistoric activityListener) {
        this.onNotificationUpdateHistoric = activityListener;
    }

    public void setUpdateTipsListener(ProfilActivity.OnUpdateTipsProfil activityListener) {
        this.OnUpdateTipsProfil = activityListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        //registerReceiver(statusReceiver,mIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
    }

    @Override
    public void onPause() {
        if (mIntent != null) {
            this.unregisterReceiver(mMessageReceiver);
            mIntent = null;
        }
        super.onPause();
    }

    //Broadcast which trigger Task in profil when Live Query update

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            boolean approved = intent.getBooleanExtra("approved",false);
            String sharepoints = intent.getStringExtra("sharepoints");
            String business = intent.getStringExtra("business");
            int item = pager.getCurrentItem();
            if (item!=1){
                pager.setCurrentItem(1);
                onNotificationUpdateProfil.TaskOnNotification(business, sharepoints);
            }else {
                onNotificationUpdateProfil.TaskOnNotification(business, sharepoints);
            }
        }
    };


    protected class MyPagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }

        // Return the Fragment associated with a specified position.
        @Override
        public Fragment getItem(int position) {
          /*  if (position == 0) {
                return History_container_fragment.newInstance();
            }*/  if (position == 0) {
                return client_Profil_container_fragment.newInstance();
            } else if (position == 1) {
                return client_like_container_fragment.newInstance();
            } else if (position == 2) {
                return client_Container_Mission_fragment.newInstance(null);
            }

            return null;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        };
    }

    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }

    @Override
    public void onBackPressed() {
        SparseArray<FragmentManager> managers = new SparseArray<>();
        traverseManagers(getSupportFragmentManager(), managers, 0);
        if (managers.size() > 0) {
            managers.valueAt(managers.size() - 1).popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    private void traverseManagers(FragmentManager manager, SparseArray<FragmentManager> managers, int intent) {
        if (manager.getBackStackEntryCount() > 0) {
            managers.put(intent, manager);
        }
        if (manager.getFragments() == null) {
            return;
        }
        for (Fragment fragment : manager.getFragments()) {
            if (fragment != null)
                traverseManagers(fragment.getChildFragmentManager(), managers, intent + 1);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myFAB:
                Intent intent=new Intent(this, TipsActivity.class);
                startActivityForResult(intent,1);
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pager != null) {
            pager.clearOnPageChangeListeners();
        }
    }

    private Boolean getIsFacebook(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final Boolean accountDisconnect = pref.getBoolean("isFacebook", false);         // getting String
        return accountDisconnect;
    }
}





