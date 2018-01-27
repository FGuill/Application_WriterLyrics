package com.guilla.lyricswriter.fragment.pagerTipsCategory;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.activity.DetailGroupActivity;
import com.guilla.lyricswriter.fragment.client.client_GroupDetail_Top_fragment;
import com.guilla.lyricswriter.fragment.client.client_GroupDetail_fragment;
import com.guilla.lyricswriter.fragment.pro.Pro_History_fragment;

import java.util.HashMap;
import java.util.Map;

public class PagerFragment extends Fragment{
  public ViewPager pager;
  public SampleAdapter adapter;
  public static TabLayout bottomBarTop;
  private Group group;
  private int color;


  public static PagerFragment newInstance(Group group, int color) {
    PagerFragment myFragment = new PagerFragment();
    Bundle args = new Bundle();
    args.putSerializable("group",group);
    args.putInt("color",color);
    myFragment.setArguments(args);
    return myFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View result=inflater.inflate(R.layout.pager_group, container, false);

    pager=(ViewPager)result.findViewById(R.id.pager);
    bottomBarTop = (TabLayout) result.findViewById(R.id.tab_layout);
    bottomBarTop.addTab(bottomBarTop.newTab().setText("RECENT"));
    bottomBarTop.addTab(bottomBarTop.newTab().setText("TOP"));
    bottomBarTop.setTabGravity(TabLayout.GRAVITY_FILL);

    if (getArguments().getSerializable("group")!=null){
      group=(Group)getArguments().getSerializable("group");
      color = getArguments().getInt("color");
      bottomBarTop.setSelectedTabIndicatorColor(getResources().getColor(R.color.orangefonce));
      bottomBarTop.setTabTextColors(getResources().getColor(R.color.black),getResources().getColor(R.color.greyfonce));

    }


    pager.setAdapter(buildAdapter());
    pager.setCurrentItem(0, true);
    pager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(bottomBarTop));

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


  public class SampleAdapter extends FragmentPagerAdapter {
    client_GroupDetail_fragment m1stFragment;
    client_GroupDetail_Top_fragment m4stFragment;
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
        if (ctxt instanceof DetailGroupActivity){
          return client_GroupDetail_fragment.newInstance(group);
        }
      }/* else if (position == 1) {
        if (ctxt instanceof ProfilActivity){
          return client_Profil_Tips_Hot_fragment.newInstance();
        }else {
          return Pro_History_fragment.newInstance("dons");
        }
      }*/
      else if (position == 1) {
        if (ctxt instanceof DetailGroupActivity){
          return client_GroupDetail_Top_fragment.newInstance(group);
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
          if (ctxt instanceof DetailGroupActivity){
            m1stFragment = (client_GroupDetail_fragment) createdFragment;
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
          if (ctxt instanceof DetailGroupActivity){
            m4stFragment = (client_GroupDetail_Top_fragment) createdFragment;
            break;
          }
          break;
      }
      return createdFragment;
    }

    @Override
    public String getPageTitle(int position) {
      return String.valueOf(position);
    }

    public void FragmentOperation() {
      if (m1stFragment != null) {
        Log.d("m1stFragment","ok passed");
        m1stFragment.onRefresh();
      }

    }
  }

}
