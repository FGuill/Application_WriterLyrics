package com.guilla.lyricswriter.fragment.pro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.guilla.lyricswriter.R;


/**
 * Created by Moi on 14/11/15.
 */
public class Passord_forgotten_fragment extends Fragment implements View.OnClickListener {
    private EditText email_ET;
    private EditText password;
    private Button cancel;
    private Button validate;


    View inflate;
        public static Passord_forgotten_fragment newInstance(String indice) {
        Passord_forgotten_fragment myFragment = new Passord_forgotten_fragment();
        Bundle args = new Bundle();
            args.putString("indice",indice);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String what= getArguments().get("indice").toString();

        if (what.equals("forgotPassword")){
            inflate = inflater.inflate(R.layout.view_forgot_password, container, false);
            Forgotten_Password(inflate);
            cancel.setOnClickListener(this);
            validate.setOnClickListener(this);
        }else if (what.equals("Create_new_Password")){
            inflate = inflater.inflate(R.layout.view_create_password, container, false);
            Create_New_Password(inflate);
        }

        return inflate;
    }

    private void Forgotten_Password(View inflate){
        email_ET=(EditText)inflate.findViewById(R.id.email);
        cancel=(Button)inflate.findViewById(R.id.cancel_btn);
        validate=(Button)inflate.findViewById(R.id.btn_dialog);
    }

    private void Create_New_Password(View inflate){
        password=(EditText)inflate.findViewById(R.id.password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_btn:
            Fragment prev= getParentFragment().getFragmentManager().findFragmentByTag("dialog");
                if (prev!=null){
                    Passord_forgotten_Container_fragment df = (Passord_forgotten_Container_fragment)prev;
                    df.dismiss();
                }
                break;
            case R.id.btn_dialog:
                if (email_ET.getText().toString().length()>0 && email_ET.getText().toString().contains("@")){

                }else {
                    Toast.makeText(getActivity(),"Saisie incorrecte",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}