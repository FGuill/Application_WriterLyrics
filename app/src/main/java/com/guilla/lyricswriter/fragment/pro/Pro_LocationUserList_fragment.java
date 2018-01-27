package com.guilla.lyricswriter.fragment.pro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.guilla.lyricswriter.BO.UserLocation;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.AdapterListUser;
import com.guilla.lyricswriter.Utils.ToastInterface;


import java.util.ArrayList;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_LocationUserList_fragment extends Fragment implements ToastInterface {
    ListView listView;
    ArrayList<UserLocation> userLocations;
    ToastInterface toastInterface;
    View inflate;
        public static Pro_LocationUserList_fragment newInstance(ArrayList<UserLocation> userLocations) {
        Pro_LocationUserList_fragment myFragment = new Pro_LocationUserList_fragment();
        Bundle args = new Bundle();
            args.putSerializable("userLocation",userLocations);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_pro_user_list, container, false);
        listView=(ListView)inflate.findViewById(R.id.ListView);
        toastInterface=this;
        userLocations= (ArrayList<UserLocation>)getArguments().getSerializable("userLocation");

// get data from the table by the ListAdapter
        AdapterListUser customAdapter = new AdapterListUser(getActivity(), userLocations,toastInterface);
        listView .setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final UserLocation obj = (UserLocation) listView.getAdapter().getItem(position);
                final String id  = obj.getId();
                Log.d("userid",id);
            }
        });
        return inflate;

    }

    @Override
    public void onNotificationError() {

    }

    @Override
    public void onNotificationSuccess(UserLocation user, String price) {
        CreateTransaction(user, price);
        Toast.makeText(getContext(), "Notification envoyée", Toast.LENGTH_LONG).show();
    }

    private void CreateTransaction(UserLocation userid, String price) {}
}