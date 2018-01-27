package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;

import java.util.ArrayList;

import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;


/**
 * Created by Moi on 15/11/15.
 */

public class SearchCategory extends BaseAdapter implements Filterable {
    private ArrayList<Group> items;
    private ArrayList<Group> filteredItems;
    private Context context;
    private ItemFilter mFilter = new ItemFilter();

    public SearchCategory(Context context, ArrayList<Group> ListCountry) {
        this.items = ListCountry;
        this.filteredItems = ListCountry;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.adapter_search_hashtag, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.country_TV = (TextView) convertView.findViewById(R.id.name_contact);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView =(TextView)convertView.findViewById(R.id.number_contact);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Group bo= filteredItems.get(position);
       if(bo.getGroup_categorie()!=null){
        if (!bo.getGroup_categorie().isEmpty() || viewHolder != null) {
            viewHolder.country_TV.setText(bo.getGroup_categorie());
            viewHolder.textView.setVisibility(View.INVISIBLE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setImageResource(bo.getType());

        }
        }
        return convertView;
    }


    public static class ViewHolder {
        TextView country_TV,textView;
        ImageView imageView;
        CheckBox checkBox;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            int count = items.size();
            final ArrayList<Group> tempItems = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                if (items.get(i).getGroup_categorie().toLowerCase().contains(filterString)) {
                    tempItems.add(new Group(items.get(i).getGroup_categorie(),items.get(i).getType()));
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
        }
    }


    public Filter getFilter() {
        return mFilter;
    }

}
