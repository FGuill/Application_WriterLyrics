package com.guilla.lyricswriter.fragment.pagerLike;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.fragment.client.client_like_fragment;

import java.util.HashMap;
import java.util.Map;

public class SampleAdapter extends FragmentPagerAdapter {
  client_like_fragment m1stFragment;
  client_like_fragment m2stFragment;
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
    return(3);
  }

  // Return the Fragment associated with a specified position.
  @Override
  public Fragment getItem(int position) {
    if (position == 0) {
      if (ctxt instanceof ProfilActivity){
        return client_like_fragment.newInstance("theylike");
      }
    } else if (position == 1) {
      if (ctxt instanceof ProfilActivity){
        return client_like_fragment.newInstance("mylike");
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
          m1stFragment = (client_like_fragment) createdFragment;
          break;
        }
        break;

      case 1:
        if (ctxt instanceof ProfilActivity){
          m2stFragment = (client_like_fragment) createdFragment;
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

    if (m2stFragment != null) {
      // m2ndFragment.doSomeWorkToo();
    }
  }
}
