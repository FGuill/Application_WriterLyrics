package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.EditorWriter;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.activity.ProfilActivity;

import static com.guilla.lyricswriter.R.id.name_writer;
import static com.guilla.lyricswriter.activity.ProfilActivity.user;
import static com.guilla.lyricswriter.fragment.client.client_Writer_fragment.adapter2;
import static com.guilla.lyricswriter.fragment.client.client_Writer_fragment.mylyrics;
import static com.guilla.lyricswriter.fragment.client.client_Writer_Single_fragment.mylyricsSingle;

public class MyTextWatcher implements TextWatcher {
    private LinedEditText editText;
    private TextView nameEditor;
    private String key;
    private Group group;
    private int tposition;
    private Context context;
    public MyTextWatcher(Context context, LinedEditText editText, String key, TextView nameEditor, Group group) {
        this.editText = editText;
        this.nameEditor=nameEditor;
        this.key=key;
        this.group=group;
        this.context=context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        int position = (int) editText.getTag();
        Log.d("position",String.valueOf(position));
        tposition=position;
        if (!nameEditor.getText().toString().equalsIgnoreCase(ProfilActivity.user.get_name()) && !editText.getText().toString().equalsIgnoreCase(Application.getContext().getResources().getString(R.string.writetext))){
            try {
                //    Toast.makeText(context,"Vous ne pouvez pas modifier le texte de vos amis",Toast.LENGTH_LONG).show();
                editText.setHint(Application.getContext().getResources().getString(R.string.writetext));

            }catch (NullPointerException e){

            }
        }


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int position = (int) editText.getTag();
        Log.d("position",String.valueOf(position));


      /*  if (nameEditor.getText().toString().length()>0){
            if (nameEditor.getText().toString().equalsIgnoreCase(user.get_name())){
                if (editText.getText().toString().length()>0 ) {
                    EditorWriter UserSession = new EditorWriter(user.get_id(), user.get_name().toString(), editText.getText().toString(), String.valueOf(position));
                    DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(key).child("Editor").child("lines").child(String.valueOf("line:" + position));
                    c1v2.setValue(UserSession);
                }
            }else {
                if (editText.getText().toString().length()>0 ) {
                    EditorWriter UserSession = new EditorWriter(group.getCreatorid(), nameEditor.getText().toString(), editText.getText().toString(), String.valueOf(position));
                    DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(key).child("Editor").child("lines").child(String.valueOf("line:" + position));
                    c1v2.setValue(UserSession);
                }
            }
        }else {
            if (editText.getText().toString().length()>0 ) {
                EditorWriter UserSession = new EditorWriter(user.get_id(), user.get_name().toString(), editText.getText().toString(), String.valueOf(position));
                DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(key).child("Editor").child("lines").child(String.valueOf("line:" + position));
                c1v2.setValue(UserSession);
                nameEditor.setText(user.get_name());
            }
        }*/
        // Do whatever you want with position
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (nameEditor.getText().toString().equalsIgnoreCase(ProfilActivity.user.get_name())){
            try {
                mylyrics=editText.getText().toString();
            }catch (NullPointerException e){

            }
            try {
                mylyricsSingle=editText.getText().toString();
            }catch (NullPointerException e){

            }

        }
        if (!nameEditor.getText().toString().equalsIgnoreCase(ProfilActivity.user.get_name()) && !editText.getText().toString().equalsIgnoreCase(Application.getContext().getResources().getString(R.string.writetext))){
            try {
            //    Toast.makeText(context,"Vous ne pouvez pas modifier le texte de vos amis",Toast.LENGTH_LONG).show();
                editText.setHint(Application.getContext().getResources().getString(R.string.writetext));

            }catch (NullPointerException e){

            }
        }
    }



}