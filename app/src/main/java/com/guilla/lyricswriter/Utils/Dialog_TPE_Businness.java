package com.guilla.lyricswriter.Utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guilla.lyricswriter.R;


import java.util.ArrayList;


/**
 * Created by Moi on 06/05/2017.
 */

public class Dialog_TPE_Businness extends DialogFragment {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> list = new ArrayList<String>();
    private ListView listView;
    private TPEDialog tpeDialog;
    private String selectedFromList;
    public interface TPEDialog{
        public void onTPECancel();
    }

    public static Dialog_TPE_Businness newInstance() {
        Dialog_TPE_Businness myFragment = new Dialog_TPE_Businness();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    public void setOnDialogClickedListener(TPEDialog l){
        tpeDialog = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View inflate = inflater.inflate(R.layout.layout_dialog_single_choice, container, false);
        listView = (ListView) inflate.findViewById(R.id.listview);
        TextView valider=(TextView) inflate.findViewById(R.id.valider);
        TextView annuler=(TextView) inflate.findViewById(R.id.annuler);
        final CheckBox box_TPE=(CheckBox) inflate.findViewById(R.id.box_TPE);

        get_TPE();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFromList = (listView.getItemAtPosition(i).toString());
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFromList!=null){}
                else {
                    Toast.makeText(getActivity(),"Veuillez séléctionner un TPE",Toast.LENGTH_LONG).show();
                }
            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tpeDialog.onTPECancel();
            }
        });
        return inflate;
    }


    private void get_TPE() {}

}



