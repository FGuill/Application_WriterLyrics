package com.guilla.lyricswriter.fragment.pro;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.guilla.lyricswriter.BO.UserLocation;
import com.guilla.lyricswriter.LocalDatabase.DatabaseHandler;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.CustomGrid;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.fragment.Updateable;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_PaimentStepOne_fragment extends Fragment implements Updateable, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private double myBusiness_latitude=0.0;
    private double myBusiness_longitude=0.0;
    private UserLocation user;
    private String objectiD;
    private CustomGrid adapter;
    private DatabaseHandler db;
    private List<UserLocation> locationUser=new ArrayList<>();
    private TextView paiment_classique;
    private View inflate;
    private SwipeRefreshLayout swipeContainer;
    GridView grid;
    private OnChildPaymentSelection onSelection;


    public interface OnChildPaymentSelection{
        public void OnSelectGrid(UserLocation user, int i, CircleImageView imageView);
        public void Classique();

    }

    public static Pro_PaimentStepOne_fragment newInstance() {
        Pro_PaimentStepOne_fragment myFragment = new Pro_PaimentStepOne_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_paimentstepone_pro, container, false);
        paiment_classique=(TextView)inflate.findViewById(R.id.paiment_classique);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        paiment_classique.setOnClickListener(this);
        db = new DatabaseHandler(getActivity());

        grid=(GridView)inflate.findViewById(R.id.grid);
        adapter = new CustomGrid(getActivity(),locationUser);
        grid.setAdapter(adapter);
        locationUser.clear();

        if (Utils.isConnected(getActivity())){
            GetClients();
        }else {
            Toast.makeText(getActivity(),"Veuillez activer votre wifi ou réseau",Toast.LENGTH_LONG).show();
        }

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final UserLocation User = (UserLocation) grid.getAdapter().getItem(position);
                final CircleImageView image = (CircleImageView) view.findViewById(R.id.grid_image);

                onSelection.OnSelectGrid(User, position,image);
            }
        });

        return inflate;
    }

    @Override
    public void update() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.paiment_classique:
                onSelection.Classique();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        // check if parent Fragment implements listener
        if (getParentFragment() instanceof OnChildPaymentSelection) {
            onSelection = (OnChildPaymentSelection) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnSelection");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSelection = null;
    }



    private void GetClients() {}

    @Override
    public void onRefresh() {
        if (Utils.isConnected(getActivity())){
            swipeContainer.setRefreshing(true);
            GetClients();
        }else {
            swipeContainer.setRefreshing(false);
            Toast.makeText(getActivity(),"Veuillez activer votre wifi ou réseau",Toast.LENGTH_LONG).show();
        }
    }

}