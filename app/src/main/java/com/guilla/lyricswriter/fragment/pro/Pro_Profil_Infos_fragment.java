package com.guilla.lyricswriter.fragment.pro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.guilla.lyricswriter.BO.Business;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.activity.ProfilActivity;
import com.guilla.lyricswriter.activity.ProfilProActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.guilla.lyricswriter.activity.ProfilProActivity.db;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Profil_Infos_fragment extends Fragment{

    private CircleImageView picture;
    private EditText username;
    private EditText password;
    private EditText Siret;
    private EditText business_name;
    private EditText chief_name;
    private EditText phone;
    private EditText address;
    private EditText email;
    private EditText RIB;
    private Button done;
    private View inflate;


    public static Pro_Profil_Infos_fragment newInstance() {
        Pro_Profil_Infos_fragment myFragment = new Pro_Profil_Infos_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_pro_profilinfos, container, false);
        username=(EditText)inflate.findViewById(R.id.username);
        password=(EditText)inflate.findViewById(R.id.password);
        Siret=(EditText)inflate.findViewById(R.id.Siret);
        business_name=(EditText)inflate.findViewById(R.id.business_name);
        chief_name=(EditText)inflate.findViewById(R.id.chief_name);
        phone=(EditText)inflate.findViewById(R.id.phone_number);
        RIB=(EditText)inflate.findViewById(R.id.RIB);
        address=(EditText) inflate.findViewById(R.id.address);
        email=(EditText)inflate.findViewById(R.id.email);
        done=(Button)inflate.findViewById(R.id.done);
        picture=(CircleImageView) inflate.findViewById(R.id.picture_profil);

        String business = db.getBusinessId();
        Business biz = db.getBusiness(business);

        if (getActivity() instanceof ProfilProActivity){
        }
        if (getActivity() instanceof ProfilActivity){
        }
        password.setText("Mot de Passe : "+"");
        business_name.setText("Nom de l'entreprise : "+biz.get_businessName());
        chief_name.setText("Nom du dirigeant : "+biz.get_officerName());
        phone.setText("N° de téléphone : "+biz.get_telephoneNumber());
        address.setText("Adresse : "+biz.get_address());
        RIB.setText("RIB : "+biz.get_RIB());
        email.setText("E-mail : "+biz.get_email());

        picture.setImageResource(R.drawable.logo);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfilProActivity)getActivity()).onBackPressed();
            }
        });
        return inflate;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}