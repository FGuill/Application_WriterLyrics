package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;

public class SearchFilterCountry implements TextWatcher {

    ListView listView;
    Context context;
    AdapterGetCountry adapter;

    public SearchFilterCountry(Context context, AdapterGetCountry adapter, ListView listView){
        this.context=context;
        this.adapter=adapter;
        this.listView=listView;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s.toString().toLowerCase());
        listView.setAdapter(adapter);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}