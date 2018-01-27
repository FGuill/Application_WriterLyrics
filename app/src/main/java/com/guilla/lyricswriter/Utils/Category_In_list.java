package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.guilla.lyricswriter.R;

/**
 * Created by Moi on 21/11/15.
 */
public class Category_In_list {

    private String category_nom;
    private Context context;
    private TextView textView;
    private ImageView img;
    private View barre;

    private int color;

    // constructor


    public Category_In_list(Context context, String category_nom, TextView textView,ImageView img,View barre) {
        this.category_nom = category_nom;
        this.context=context;
        this.textView=textView;
        this.img=img;
        this.barre=barre;
    }


    public void setColor(String category_nom){
       if (category_nom.equalsIgnoreCase(context.getString(R.string.Animals))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_animalpets);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_animals));
               }
           }

           if (textView!=null) {
               textView.setTextColor(context.getResources().getColor(R.color.color_animals));
           }
           color=R.color.color_animals;
       }
       if (category_nom.equalsIgnoreCase(context.getString(R.string.HomeGarden))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_home);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_home));
               }
           }
           if (textView!=null) {
               textView.setTextColor(context.getResources().getColor(R.color.color_home));
           }
           color=R.color.color_home;

       }
       if (category_nom.equalsIgnoreCase(context.getString(R.string.Arts))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_artculture);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_art));
               }
           }
           if (textView!=null) {
               textView.setTextColor(context.getResources().getColor(R.color.color_art));
           }
           color=R.color.color_art;
       }

       if (category_nom.equalsIgnoreCase(context.getString(R.string.Miscellaneous))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_miscellanisous);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_miscellaneous));
               }
           }
           if (textView!=null) {
               textView.setTextColor(context.getResources().getColor(R.color.color_miscellaneous));
           }
           color=R.color.color_miscellaneous;
       }

       if (category_nom.equalsIgnoreCase(context.getString(R.string.Careers))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_careerswork);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_careers));
               }
           }
           if (textView!=null) {
               textView.setTextColor(context.getResources().getColor(R.color.color_careers));
           }
           color=R.color.color_careers;
       }
       if (category_nom.equalsIgnoreCase(context.getString(R.string.Money))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_moneyfinance);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_money));
               }
           }
           if (textView!=null) {
               textView.setTextColor(context.getResources().getColor(R.color.color_money));
           }
           color=R.color.color_money;
       }
       if (category_nom.equalsIgnoreCase(context.getString(R.string.Clothing))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_clothing);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_clothing));
               }
           }
           if (textView!=null) {
               textView.setTextColor(context.getResources().getColor(R.color.color_clothing));
           }
           color=R.color.color_clothing;
       }
       if (category_nom.equalsIgnoreCase(context.getString(R.string.Productivity))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_productivity);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_productivity));
               }
           }

           if (textView!=null) {
               textView.setTextColor(context.getResources().getColor(R.color.color_productivity));
           }
           color=R.color.color_productivity;

       }
       if (category_nom.equalsIgnoreCase(context.getString(R.string.Computers))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_computers);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_computers));
               }
           }
           textView.setTextColor(context.getResources().getColor(R.color.color_computers));
           color=R.color.color_computers;

       }
       if (category_nom.equalsIgnoreCase(context.getString(R.string.Requests))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_requests);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_requests));
               }
           }
           if (textView!=null) {
               textView.setTextColor(context.getResources().getColor(R.color.color_requests));
           }
           color=R.color.color_requests;


       }  if (category_nom.equalsIgnoreCase(context.getString(R.string.Electronics))){
            if (img!=null){
                img.setBackgroundResource(R.drawable.category_electronics);
                if (barre!=null) {
                    barre.setBackgroundColor(context.getResources().getColor(R.color.color_electronics));
                }
            }
            if (textView!=null) {
                textView.setTextColor(context.getResources().getColor(R.color.color_electronics));
            }
            color=R.color.color_electronics;

        }
       if (category_nom.equalsIgnoreCase(context.getString(R.string.School))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_schoolcollege);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_school));
               }
           }
           if (textView!=null) {
               textView.setTextColor(context.getResources().getColor(R.color.color_school));
           }
           color=R.color.color_school;
       }
       if (category_nom.equalsIgnoreCase(context.getString(R.string.Food))){
           if (img!=null){
               img.setBackgroundResource(R.drawable.category_fooddrink);
               if (barre!=null) {
                   barre.setBackgroundColor(context.getResources().getColor(R.color.color_food));
               }
           }
           textView.setTextColor(context.getResources().getColor(R.color.color_food));
           color=R.color.color_food;

       }
        if (category_nom.equalsIgnoreCase(context.getString(R.string.Social))){
            if (img!=null){
                img.setBackgroundResource(R.drawable.category_social);
                if (barre!=null) {
                    barre.setBackgroundColor(context.getResources().getColor(R.color.color_social));
                }
            }

            if (textView!=null) {
                textView.setTextColor(context.getResources().getColor(R.color.color_social));
            }
            color=R.color.color_social;
        }

        if (category_nom.equalsIgnoreCase(context.getString(R.string.Health))){
            if (img!=null){
                img.setBackgroundResource(R.drawable.category_healthfitness);
                if (barre!=null) {
                    barre.setBackgroundColor(context.getResources().getColor(R.color.color_health));
                }
            }
            if (textView!=null) {
                textView.setTextColor(context.getResources().getColor(R.color.color_health));
            }
            color=R.color.color_health;
        }

        if (category_nom.equalsIgnoreCase(context.getString(R.string.Travelling))){
            if (img!=null){
                img.setBackgroundResource(R.drawable.category_travel);
                if (barre!=null) {
                    barre.setBackgroundColor(context.getResources().getColor(R.color.color_travelling));
                }
            }
            if (textView!=null) {
                textView.setTextColor(context.getResources().getColor(R.color.color_travelling));
            }
            color=R.color.color_travelling;
        }

   }

    public int getColor() {
        return color;
    }
}
