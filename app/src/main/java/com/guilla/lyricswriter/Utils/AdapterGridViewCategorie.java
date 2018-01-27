package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guilla.lyricswriter.BO.Category;
import com.guilla.lyricswriter.R;

import java.util.ArrayList;



public class AdapterGridViewCategorie extends BaseAdapter{

    OnItemGridCategorieClickListener categorieClickListener;
    Context context;
    LayoutInflater inflat;
    ArrayList<Category> item=new ArrayList<>();
    private static LayoutInflater inflater=null;

    public interface OnItemGridCategorieClickListener {
        void onItemCategorieClick(int item, String categorie);
    }

    public AdapterGridViewCategorie(Context context, ArrayList<Category> item, OnItemGridCategorieClickListener categorieClickListener) {
        // TODO Auto-generated constructor stub
        inflat = LayoutInflater.from(context);
        this.categorieClickListener=categorieClickListener;
        this.item=item;
        this.context=context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return item.size();
    }
 
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 


    private class ViewHolder {
        TextView os_text;
        ImageView os_img;
        RelativeLayout background;
        public String toString() {
            return "-";
        }
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AdapterGridViewCategorie.ViewHolder holder = null;

        final Category it = item.get(position);

        if (convertView == null) {
            holder = new AdapterGridViewCategorie.ViewHolder();
            convertView = inflat.inflate(R.layout.sample_gridlayout, null);

            holder.os_text =(TextView) convertView.findViewById(R.id.os_texts);
            holder.os_img =(ImageView) convertView.findViewById(R.id.os_images);
            holder.background =(RelativeLayout) convertView.findViewById(R.id.background);

            convertView.setTag(holder);
        }
         else {
            holder = (AdapterGridViewCategorie.ViewHolder) convertView.getTag();
        }
       /* if (position == mSelectedItem) {
            holder.os_text.setTextColor(context.getResources().getColor(R.color.orange));
        }else {
            holder.os_text.setTextColor(context.getResources().getColor(R.color.greyfonce));
        }*/

        if (it.getCategory_nom()!=null){
            holder.os_text.setText(it.getCategory_nom());
        }

        holder.os_img.setTag(position);

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), it.getCategory_drawables());
            holder.os_img.setImageBitmap(bmp);

        holder.os_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                categorieClickListener.onItemCategorieClick(position, it.getCategory_nom());
            }
        });
        return convertView;
    }

}