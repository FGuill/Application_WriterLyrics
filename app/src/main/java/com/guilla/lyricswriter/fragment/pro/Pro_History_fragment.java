package com.guilla.lyricswriter.fragment.pro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.common.net.MediaType;
import com.guilla.lyricswriter.BO.History;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterHistory;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.fragment.Updateable;


import java.util.ArrayList;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_History_fragment extends Fragment implements Updateable, SwipeRefreshLayout.OnRefreshListener {

    String customer="";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private SwipeRefreshLayout swipeContainer;
    ArrayList<History> payment_value=new ArrayList<>();
    ListView listView;
    private String indice;
    private AdapterHistory customAdapter;

    View inflate;
        public static Pro_History_fragment newInstance(String indice) {
        Pro_History_fragment myFragment = new Pro_History_fragment();
        Bundle args = new Bundle();
            args.putString("indice",indice);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_history_pro, container, false);
        listView=(ListView)inflate.findViewById(R.id.ListView);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

            indice= getArguments().getString("indice");
            payment_value.clear();
            customAdapter = new AdapterHistory(getActivity(), payment_value);
            listView .setAdapter(customAdapter);
            if (Utils.isConnected(getContext())){
                get_client_Historic();
            }
        return inflate;

    }
    private void get_client_Historic() {

    }


    @Override
    public void update() {

    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())){
            swipeContainer.setRefreshing(true);
            payment_value.clear();
            customAdapter.notifyDataSetChanged();
            get_client_Historic();
        }
    }
}