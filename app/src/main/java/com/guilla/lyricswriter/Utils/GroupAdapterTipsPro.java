package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.guilla.lyricswriter.fragment.client.client_Profil_fragment.listParticiant;


public class GroupAdapterTipsPro extends RecyclerView.Adapter<GroupAdapterTipsPro.ViewHolder> {
    OnItemDonateClickListener listener;
    private int selectedPos = 0;
    // private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<Group> AL_id_text = new ArrayList<Group>();
    Context mContext;
    public interface OnItemDonateClickListener {
        void onItemClick(int item, Group bo);
        void onJoinGroupClick(int item, Group bo);
        void onInviteFriends(int item, Group bo);

    }

    public GroupAdapterTipsPro(Context c, ArrayList<Group> AL_id_text, OnItemDonateClickListener listener) {
        mInflater = LayoutInflater.from(c);
        mContext = c;
        this.AL_id_text = AL_id_text;
        this.listener = listener;
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
    public GroupAdapterTipsPro.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_donation2, null);
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
        viewHolder.bind(position, listener, bo);
        viewHolder.join.setTag(position);
      //  viewHolder.recyclerParticiant.setTag(position);
      /*  if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }*/

        if (bo.getGroupname() != null) {
            viewHolder.group.setText(bo.getGroupname());
        }
        if (bo.getCreatorname() != null) {
            viewHolder.creatorname.setText(mContext.getResources().getString(R.string.creator)+" "+bo.getCreatorname());
        }
        if (bo.getTimelapsedTips() != null) {
            viewHolder.time.setText(bo.getTimelapsedTips());
        }



        if (bo.getListusers() != null) {
            /*StringBuffer sb = new StringBuffer("");
            for (UserJoined users : bo.getUserJoined()){
                if (bo.getUserJoined().size()>=1){
                        sb.append(users.getName()+" ,");
                }

            }*/
          //  viewHolder.listusers.setText(bo.getListusers()+" Participants");
        }


      /*  if (bo.get_image() != null) {
            byte[] image = bo.get_image();
            Bitmap PictureProfile = DbBitmapUtility.getImage(image);
            viewHolder.image.setImageBitmap(PictureProfile);
        }*/

        viewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onJoinGroupClick(position,bo);
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView group,style,listusers,creatorname,time,invite;
        Button join;
        RecyclerView recyclerParticiant;
        Adapter_profil_Sharity_client adapterSafepeople;
        RecyclerView.LayoutManager layoutManager2;
        private LinearLayout llGallery;
        private HorizontalScrollView scrollview;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            group = (TextView) itemView.findViewById(R.id.group);
            creatorname = (TextView) itemView.findViewById(R.id.creatorname);
            time = (TextView) itemView.findViewById(R.id.time);
            join = (Button) itemView.findViewById(R.id.join);
          /*  recyclerParticiant = (RecyclerView) itemView.findViewById(R.id.recyclerviewParticiant);
            layoutManager2 = new CustomLinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
            recyclerParticiant.setLayoutManager(layoutManager2);
            adapterSafepeople = new Adapter_profil_Sharity_client(mContext, listParticiant);
            recyclerParticiant.setAdapter(adapterSafepeople);*/
        }


        public void bind(final int item, final OnItemDonateClickListener listener, final Group bo) {
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

    public void IniializeRecyclerView(RecyclerView recyclerView,Adapter_profil_Sharity_client adapterSafepeople){
    LinearLayoutManager layoutManager2
            = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager2);
        layoutManager2.setReverseLayout(true);
        layoutManager2.setStackFromEnd(true);
        adapterSafepeople = new Adapter_profil_Sharity_client(mContext, listParticiant);
        recyclerView.setAdapter(adapterSafepeople);
    }


}
