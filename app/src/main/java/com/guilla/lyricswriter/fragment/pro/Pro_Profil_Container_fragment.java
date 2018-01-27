package com.guilla.lyricswriter.fragment.pro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.activity.ProfilProActivity;
import com.guilla.lyricswriter.fragment.Updateable;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Profil_Container_fragment extends Fragment implements Updateable,ProfilProActivity.ListenFromActivity {


    public static final String ARG_PLANET_NUMBER = "planet_number";
    private View inflate;
    private TextView username;
    private CircleImageView imageView;
    private TextView points;
    private TextView email;
    private TextView phone;

    public static Pro_Profil_Container_fragment newInstance(String source) {
        Pro_Profil_Container_fragment myFragment = new Pro_Profil_Container_fragment();
        Bundle args = new Bundle();
        args.putString("source",source);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_profile_container_pro, container, false);
        ((ProfilProActivity) getActivity()).setActivityListener(Pro_Profil_Container_fragment.this);

 /* LinearLayout beatmaker_layout=(LinearLayout)inflate.findViewById(R.id.activity_main);
        final LayoutInflater inflat= (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View editor= inflat.inflate(R.layout.layout_editingsequence,beatmaker_layout,false);
        beatmaker_layout.addView(editor);
        Utils.expand(editor);*/

        if (getArguments().getString("source").toString()!=null){
            if (getArguments().getString("source").equals("Profil")){
                Fragment currentFagment= getFragmentManager().findFragmentById(R.id.Fragment_profil_container);
                if (currentFagment instanceof Pro_Profil_fragment){
                }else {
                    Pro_Profil_fragment fragTwo = new Pro_Profil_fragment();
                    FragmentManager fm = getChildFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.Fragment_profil_container, fragTwo);
                    ft.commit();
                }
            }
        }

        return inflate;
    }



    @Override
    public  void update() {
    }

    @Override
    public void doSomethingInFragment(String pos) {
        if (pos.equals("Finalize_inscription")){
            Fragment currentFagment= getFragmentManager().findFragmentById(R.id.Fragment_profil_container);
            if (currentFagment instanceof Pro_Profil_fragment){
            }else {
                Pro_Profil_Ending_Inscription_fragment fragTwo = new Pro_Profil_Ending_Inscription_fragment();
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Fragment_profil_container, fragTwo);
                ft.addToBackStack(null);
                ft.commit();
            }
        }

        if (pos.equals("Profilinfo")){
            Fragment currentFagment= getFragmentManager().findFragmentById(R.id.Fragment_profil_container);
            if (currentFagment instanceof Pro_Profil_fragment){
                Log.d("fragment Profil","Pro_Profil_fragment visible");
            }else {
                Pro_Profil_Infos_fragment fragTwo = new Pro_Profil_Infos_fragment();
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Fragment_profil_container, fragTwo);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
}

}