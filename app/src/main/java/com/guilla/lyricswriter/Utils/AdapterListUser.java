package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guilla.lyricswriter.BO.UserLocation;
import com.guilla.lyricswriter.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


import static com.guilla.lyricswriter.Application.getContext;
import static com.guilla.lyricswriter.R.id.price;
import static com.guilla.lyricswriter.R.id.user;


public class AdapterListUser extends BaseAdapter {
    private UserLocation p;
    private Context context;
    private ArrayList<UserLocation> items;
    ToastInterface toastInterface;
    public AdapterListUser(Context context, ArrayList<UserLocation> items, ToastInterface toastInterface) {
        //super(context, R.layout.your_row, items);
        this.toastInterface=toastInterface;
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        if (items == null) {
            Log.v("LOG", "Warn, null filteredData");
            return 0;
        } else {
            return items.size();
        }
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.row_closeuser_location_listiew, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.userTV = (TextView) convertView.findViewById(user);
            viewHolder.priceET = (EditText) convertView.findViewById(price);
            viewHolder.send_BT = (Button) convertView.findViewById(R.id.send);
            viewHolder.imageView = (CircleImageView) convertView.findViewById(R.id.profil);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        p = items.get(position);
        viewHolder.send_BT.setTag(position);
        viewHolder.send_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewHolder.priceET.getText().toString().length() > 0) {
                    SendNotification(p,viewHolder.priceET);
                } else {
                    Toast.makeText(getContext(), "Veuillez entrer une valeur", Toast.LENGTH_LONG).show();
                }
            }
        });
        if (viewHolder.userTV != null) {
            viewHolder.userTV.setText(p.getUsername());
        }
        if (viewHolder.imageView != null) {
            Bitmap bmp = null;
            bmp = BitmapFactory.decodeByteArray(p.getPictureProfil(), 0, p.getPictureProfil().length);
            viewHolder.imageView.setImageBitmap(bmp);
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView userTV;
        EditText priceET;
        Button send_BT;
        CircleImageView imageView;
    }



    private void SendNotification(UserLocation user, final EditText price){

    }
}

