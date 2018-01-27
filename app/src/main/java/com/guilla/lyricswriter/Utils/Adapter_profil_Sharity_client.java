package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_profil_Sharity_client extends RecyclerView.Adapter<Adapter_profil_Sharity_client.ViewHolder> {
    private List<String> mDataList;
    private int mRowIndex = -1;

    OnItemDonateClickListener listener;
    private int selectedPos=0;
    // private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<Group> AL_id_text = new ArrayList<Group>();
    Context mContext;

    public interface OnItemDonateClickListener {
        void onItemClick(int item,Group bo);
    }

    public Adapter_profil_Sharity_client(Context c, ArrayList<Group> AL_id_text) {
        mInflater = LayoutInflater.from(c);
        mContext = c;
        this.AL_id_text = AL_id_text;
        this.listener = listener;
    }

    public void setData(List<String> data) {
        if (mDataList != data) {
            mDataList = data;
            notifyDataSetChanged();
        }
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }


    public int getCount() {
        return AL_id_text.size();
    }

    public Object getItem(int position) {
        return AL_id_text.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    @Override
    public Adapter_profil_Sharity_client.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_sharity_h, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return AL_id_text.size();
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final Group bo = (Group) getItem(position);
        viewHolder.bind(position,listener,bo);
        viewHolder.image.setTag(position);
        viewHolder.itemView.setTag(position);

       /* if(selectedPos == position){
            viewHolder.background.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.background.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.item_list_border_green));
            } else {
                viewHolder.background.setBackground(ContextCompat.getDrawable(mContext, R.drawable.item_list_border_green));
            }
        }else{

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.background.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.item_list_border_grey));
            } else {
                viewHolder.background.setBackground(ContextCompat.getDrawable(mContext, R.drawable.item_list_border_grey));
            }
        }*/




        if (bo.getParticipantname()!=null) {
            viewHolder.name.setText(bo.getParticipantname());
        }

        if (bo.getParticipantpicurl()!=null) {
            Bitmap PictureProfile ;
            byte[] image = DbBitmapUtility.StringToByteArray(bo.getParticipantpicurl());
            PictureProfile = DbBitmapUtility.getImage(image);
            viewHolder.image.setImageBitmap(PictureProfile);
        }else {
            viewHolder.image.setImageResource(R.drawable.icon_logo);
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        LinearLayout background;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            background = (LinearLayout) itemView.findViewById(R.id.background);
            name = (TextView) itemView.findViewById(R.id.name);
            image = (CircleImageView) itemView.findViewById(R.id.imageView);
        }


        public void bind(final int item, final OnItemDonateClickListener listener, final Group bo) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listener.onItemClick(item, bo);
                        notifyItemChanged(selectedPos);
                        selectedPos = item;
                        notifyItemChanged(selectedPos);
                    }catch (NullPointerException e) {

                    }
                }
            });
        }
    }
    }