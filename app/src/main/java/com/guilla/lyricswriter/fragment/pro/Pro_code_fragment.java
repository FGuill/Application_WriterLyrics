package com.guilla.lyricswriter.fragment.pro;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guilla.lyricswriter.LoginPro.LoginPresenter;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.fonts.EditTextMontserra;
import com.guilla.lyricswriter.fonts.TextViewSegoeUi;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_code_fragment extends Fragment implements View.OnClickListener {

    private Boolean emailVerified=null;
    private View inflate;
    private ProgressBar progress;
    private EditTextMontserra saisir_code;
    private EditTextMontserra confirmer_code;
    private TextViewSegoeUi valider;
    private TextView inscription;
    private LoginPresenter presenter;

    public static Pro_code_fragment newInstance() {
        Pro_code_fragment myFragment = new Pro_code_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_setcode_pro, container, false);
        saisir_code=(EditTextMontserra)inflate.findViewById(R.id.saisir_code);
        confirmer_code=(EditTextMontserra)inflate.findViewById(R.id.confirmer_code);
        valider=(TextViewSegoeUi)inflate.findViewById(R.id.valider);

        saisir_code.setOnClickListener(this);
        confirmer_code.setOnClickListener(this);
        valider.setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.saisir_code:

                break;

            case R.id.confirmer_code:
                break;

            case R.id.valider:
               String saisie= saisir_code.getText().toString();
                String confirmation= confirmer_code.getText().toString();
                if (saisie.length()<4 || confirmation.length()<4){
                    Toast.makeText(getActivity(),"Les codes doivent être de 4 chiffres numériques",Toast.LENGTH_LONG).show();

                }else {
                    if (saisie.equals(confirmation)){
                        SharedPreferences pref = getActivity().getSharedPreferences("Pref", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Business_numCode", saisie);
                        editor.commit();


                        if (Utils.isConnected(getContext())){
                            SaveCode_ToParse(saisie);
                        }else {
                            Utils.showDialog3(getActivity(), getString(R.string.dialog_network),getString(R.string.network),true, new Utils.Click() {
                            @Override
                            public void Ok() {

                            }
                            @Override
                            public void Cancel() {

                            }
                        });

                        }

                    }else {
                        Toast.makeText(getActivity(),"Les codes entrés sont pas identiques",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void SaveCode_ToParse(final String saisie){

    }

    private String getBusinessObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("Business_ObjectId", "");         // getting String
        return accountDisconnect;
    }



}