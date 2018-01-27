package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Moi on 15/11/15.
 */

public class AdapterGetCountry extends BaseAdapter implements Filterable {
    private List<Group> items;
    private List<Group> filteredItems;
    private Context context;
    private ItemFilter mFilter = new ItemFilter();
    private OnFollowButtonClickListener listener;

    public AdapterGetCountry(Context context, List<Group> ListCountry, OnFollowButtonClickListener listener) {
        this.items = ListCountry;
        this.filteredItems = ListCountry;
        this.context = context;
        this.listener=listener;
    }

    public interface OnFollowButtonClickListener {
        void onFollowButtonClick(int item, Group bo, LinearLayout button, TextView textButton);
    }

    @Override
    public int getCount() {
        if(filteredItems==null){
            Log.v("LOG","Warn, null filteredData");
            return 0;
        }else{
            return filteredItems.size();
        }
    }



    @Override
    public Object getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.adapter_listview_friendschoice, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.country_TV = (TextView) convertView.findViewById(R.id.name_contact);
             viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.follow =(TextView)convertView.findViewById(R.id.button);
            viewHolder.followLinear =(LinearLayout)convertView.findViewById(R.id.buttonLayout);
           viewHolder.time=(TextView)convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Group bo= filteredItems.get(position);
        viewHolder.imageView.setTag(position);
        viewHolder.followLinear.setTag(position);

        if (bo.getLocation()!=null) {
            if (bo.getLocation().equalsIgnoreCase("novalue")){
                viewHolder.time.setText("");
            }else {
                viewHolder.time.setText(bo.getLocation());
            }
            }

        viewHolder.followLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFollowButtonClick(position,bo,viewHolder.followLinear,viewHolder.follow);
            }
        });

        if (!bo.getIdkey().isEmpty() || viewHolder != null) {
            viewHolder.country_TV.setText(bo.getCreatorname());
            Log.d("userFriends", "getCreatorname");
        }

        if (bo.getUser_picurl()!=null) {
            viewHolder.imageView.setVisibility(View.VISIBLE);
            Bitmap PictureProfile ;
            byte[] image = DbBitmapUtility.StringToByteArray(bo.getUser_picurl());
            PictureProfile = DbBitmapUtility.getImage(image);
            viewHolder.imageView.setImageBitmap(PictureProfile);
        }else {
            viewHolder.imageView.setImageResource(R.drawable.icon_logo);
        }

        if (bo.getFollow()!=null){
            viewHolder.follow.setText(bo.getFollow());
        }

        if (bo.getFollow().equalsIgnoreCase(Application.getContext().getResources().getString(R.string.invite))){
            setButtonFollow(viewHolder.followLinear);
        }else {
            setButtonUnFollow(viewHolder.followLinear);
        }

        return convertView;
    }


    public static class ViewHolder {
        TextView country_TV,textView,follow,time;
        ImageView imageView;
        CheckBox checkBox;
        LinearLayout followLinear;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            Log.d("textchanged",filterString);
            FilterResults results = new FilterResults();
                int count = items.size();
                final ArrayList<Group> tempItems = new ArrayList<>(count);

                for (int i = 0; i < count; i++) {
                    if (items.get(i).getCreatorname().toLowerCase().contains(filterString)) {
                        tempItems.add(new Group(items.get(i).getIdkey(), items.get(i).getCreatorid(), items.get(i).getCreatorname(), items.get(i).getUser_picurl(), 0, items.get(i).getFollow(), items.get(i).getMutualFollow(),items.get(i).getLocation()));
                    }
                }
                results.values = tempItems;
                results.count = tempItems.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredItems = (ArrayList<Group>) results.values;
            notifyDataSetChanged();
            Log.d("textchanged","publishResults");

        }
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        return mFilter;
    }

    public void  setButtonFollow(LinearLayout buttonFollow){
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {

            buttonFollow.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.roundedbt) );
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                buttonFollow.setBackground( context.getResources().getDrawable(R.drawable.roundedbt));
            }
        }
    }
    public void  setButtonUnFollow(LinearLayout buttonFollow){
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            buttonFollow.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.roundedbtunfollow) );
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                buttonFollow.setBackground( context.getResources().getDrawable(R.drawable.roundedbtunfollow));
            }
        }
    }

    private String getLocation(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        String county = pref.getString("location", "novalue");         // getting String
        return county;
    }

    private Boolean getPref_location(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final Boolean accountDisconnect = pref.getBoolean("location_rules", false);         // getting String
        return accountDisconnect;
    }

}