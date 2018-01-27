package com.guilla.lyricswriter.fragment;

import com.guilla.lyricswriter.BO.LocationBusiness;

import java.util.ArrayList;

public interface MapCallback {
   public void onOpen(ArrayList<LocationBusiness> data, boolean type);
   public void onClose();
}