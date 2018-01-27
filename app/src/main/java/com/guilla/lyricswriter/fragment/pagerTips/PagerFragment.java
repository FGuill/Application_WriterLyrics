package com.guilla.lyricswriter.fragment.pagerTips;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.fragment.client.client_Container_Mission_fragment;
import com.guilla.lyricswriter.fragment.client.client_Profil_Tips_Top_fragment;
import com.guilla.lyricswriter.fragment.client.client_Profil_container_fragment;
import com.guilla.lyricswriter.fragment.client.client_Profil_fragment;
import com.guilla.lyricswriter.fragment.client.client_like_fragment;
import com.guilla.lyricswriter.fragment.pro.Pro_History_fragment;

import java.util.HashMap;
import java.util.Map;

import static com.guilla.lyricswriter.R.id.bottomBar;
import static com.guilla.lyricswriter.R.id.ic_addfriends;
import static com.guilla.lyricswriter.R.id.myFAB;


public class PagerFragment extends Fragment{
  public ViewPager pager;
  private OnSelection onSelection;
  public SampleAdapter adapter;
  public static TabLayout bottomBarTop;
  public static boolean  PagerOneSet=false;
  public static Boolean  PagerTwoSet=false;


  public interface OnSelection{
    public void OnSelect(int i);
  }



  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View result=inflater.inflate(R.layout.pager, container, false);
    pager=(ViewPager)result.findViewById(R.id.pager);
    bottomBarTop = (TabLayout) result.findViewById(R.id.tab_layout);
    bottomBarTop.setTabGravity(TabLayout.GRAVITY_FILL);
    bottomBarTop.setupWithViewPager(pager);
    bottomBarTop.addTab(bottomBarTop.newTab().setText("Collaboration"));
    bottomBarTop.addTab(bottomBarTop.newTab().setText("Mes lyrics"));
    pager.setAdapter(buildAdapter());
    pager.setCurrentItem(0, true);
    pager.setOnPageChangeListener(mPageChangeListener);

    bottomBarTop.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });

    //buildTabs();
    return(result);
  }

  public SampleAdapter buildAdapter() {
    adapter = new SampleAdapter(getActivity(), getChildFragmentManager());
    pager.setAdapter(adapter);
    return adapter;
  }

  public SampleAdapter getAdapter() {
    return adapter;
  }

 /* private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
      setIndicator(position);
      if (position!=1){
        bottomBarTop.setInActiveTabColor(getResources().getColor(R.color.greyfonce));
        bottomBarTop.setActiveTabColor(getResources().getColor(R.color.orangefonce));
        bottomBarTop.setBadgeBackgroundColor(getResources().getColor(R.color.orangefonce));
      }

      onSelection.OnSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };*/

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);


    // check if parent Fragment implements listener
    if (getParentFragment() instanceof OnSelection) {
      onSelection = (OnSelection) getParentFragment();
    } else {
      throw new RuntimeException("The parent fragment must implement OnSelection");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    onSelection = null;
  }



 /* private void setIndicator(int index) {
    bottomBarTop.selectTabAtPosition(index);
  }

  private void buildTabs() {
    bottomBarTop.setOnTabSelectListener(this, true);
    setIndicator(0);
    bottomBarTop.setInActiveTabColor(getResources().getColor(R.color.greyfonce));
    bottomBarTop.setActiveTabColor(getResources().getColor(R.color.orangefonce));
  }*/

/*  @Override
  public void onTabSelected(@IdRes int tabId) {
    switch (tabId) {
      case tab_recent:
        pager.setCurrentItem(0, true);
        break;
      case R.id.tab_hot:
        pager.setCurrentItem(1, true);
        break;
      case R.id.tab_top:
        pager.setCurrentItem(2, true);
        break;
    }
  }*/


  public ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      if (position == 0) {
        if (ProfilActivity.myFAB!=null){
          ProfilActivity.myFAB.setVisibility(View.VISIBLE);
        }
          if (!PagerOneSet){
            if (adapter.m1stFragment != null) {
              new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  adapter.m1stFragment.onRefresh();
                }
              }, 400);
            }
        }

      } else if (position == 1) {
        if (ProfilActivity.myFAB != null) {
          ProfilActivity.myFAB.setVisibility(View.INVISIBLE);
        }
        if (!PagerTwoSet) {
          if (adapter.m4stFragment != null) {
            new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                adapter.m4stFragment.onRefresh();
              }
            }, 400);
          }
        }
      }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };


  public class SampleAdapter extends FragmentPagerAdapter {
    client_Profil_fragment m1stFragment;
    client_Profil_Tips_Top_fragment m4stFragment;
    Pro_History_fragment m3stFragment;
    FragmentManager mgr;
    Context ctxt=null;
    private Map<Integer, String> mFragmentTags;

    public SampleAdapter(Context ctxt, FragmentManager mgr) {
      super(mgr);
      this.mgr=mgr;
      this.ctxt=ctxt;
      mFragmentTags = new HashMap<Integer, String>();

    }

    @Override
    public int getCount() {
      return(2);
    }

    // Return the Fragment associated with a specified position.
    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        if (ctxt instanceof ProfilActivity){
          return client_Profil_fragment.newInstance();
        }else {
          return Pro_History_fragment.newInstance("payements");
        }
      }/* else if (position == 1) {
        if (ctxt instanceof ProfilActivity){
          return client_Profil_Tips_Hot_fragment.newInstance();
        }else {
          return Pro_History_fragment.newInstance("dons");
        }
      }*/
      else if (position == 1) {
        if (ctxt instanceof ProfilActivity){
          return client_Profil_Tips_Top_fragment.newInstance();
        }else {
          return Pro_History_fragment.newInstance("dons");
        }
      }
      return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
      // save the appropriate reference depending on position
      switch (position){
        case 0:
          if (ctxt instanceof ProfilActivity){
            m1stFragment = (client_Profil_fragment) createdFragment;
            break;
          }
          break;

      /*  case 1:
          if (ctxt instanceof ProfilActivity){
            m2stFragment = (client_Profil_Tips_Hot_fragment) createdFragment;
            break;
          }else {
            m2stFragment = (client_Profil_Tips_Hot_fragment) createdFragment;
          }
          break;*/

        case 1:
          if (ctxt instanceof ProfilActivity){
            m4stFragment = (client_Profil_Tips_Top_fragment) createdFragment;
            break;
          }
          break;
      }
      return createdFragment;
    }


    @Override
    public CharSequence getPageTitle(int position) {
      //this is where you set the titles
      switch(position) {
        case 0:
          return "Collaboration";
        case 1:
          return PagerFragment.this.getResources().getString(R.string.mylyrics);
      }
      return null;
    }

    public void FragmentOperation() {
      if (m1stFragment != null) {
        m1stFragment.onRefresh();
      }
    }
  }

}
