package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guilla.lyricswriter.BO.User;
import com.guilla.lyricswriter.R;


import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.media.CamcorderProfile.get;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    OnItemDonateClickListener listener;
    private int selectedPos = 0;
    // private Context mContext;
    private LayoutInflater mInflater;
    public static int itemlistpos=-1;
    Context mContext;
    
     private ArrayList<User> items;
    private ArrayList<User> filteredItems;
    private ItemFilter mFilter = new ItemFilter();

    public interface OnItemDonateClickListener {
        void onItemClick(int item, User bo);
        void onItemLike(int item, User bo,boolean likedTips,ImageView likebutton);
        void onCommentary(int item, User bo);
        void onCreatorNameTouch(int item, User bo);
        void onHashTagTouch(int item, User bo,String hashTag);
        void onImageCategoryTouch(int item, User bo);

    }

    public SearchAdapter(Context c, ArrayList<User> filteredItems, OnItemDonateClickListener listener) {
        mInflater = LayoutInflater.from(c);
        mContext = c;
        this.filteredItems = filteredItems;
        this.listener = listener;
    }

    public int getCount() {
        if(filteredItems==null){
            Log.v("LOG","Warn, null filteredData");
            return 0;
        }else{
            return filteredItems.size();
        }
    }

    public Filter getFilter() {
        return mFilter;
    }


    public Object getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listview_country, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        User bo= filteredItems.get(position);
        viewHolder.bind(position, listener, bo);

      /*  if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }*/

        if (bo.get_name() != null) {
            viewHolder.textView.setText(bo.get_name());
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView country_TV,textView;
        ImageView imageView;
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            country_TV = (TextView) itemView.findViewById(R.id.name_contact);
             imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView =(TextView)itemView.findViewById(R.id.number_contact);
        }


        public void bind(final int item, final OnItemDonateClickListener listener, final User bo) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, bo);
                    notifyItemChanged(selectedPos);
                    selectedPos = item;
                    notifyItemChanged(selectedPos);
                }
            });
        }
    }

    public static final Pattern HASHTAG_PATTERN_OLD
            = Pattern.compile("#([\\p{L}\\p{InThai}$A-Za-z0-9_-]+)");

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            int count = items.size();
            final ArrayList<User> tempItems = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                if (items.get(i).get_name().toLowerCase().contains(filterString)) {
                    tempItems.add(new User(items.get(i).get_id(),items.get(i).get_name(),items.get(i).get_email(),items.get(i).getPicurl(),items.get(i).getToken()));
                }
            }
            results.values = tempItems;
            results.count = tempItems.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    }

}
