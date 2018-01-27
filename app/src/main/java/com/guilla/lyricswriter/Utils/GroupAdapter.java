package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.UserJoined;
import com.guilla.lyricswriter.R;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.guilla.lyricswriter.R.id.scrollview;
import static com.guilla.lyricswriter.R.id.wrap_content;
import static com.guilla.lyricswriter.fragment.client.client_Profil_fragment.listParticiant;
import static com.guilla.lyricswriter.fragment.client.client_Profil_fragment.user;


public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
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

    public GroupAdapter(Context c, ArrayList<Group> AL_id_text, OnItemDonateClickListener listener) {
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
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_donation, null);
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
            viewHolder.listusers.setText(bo.getListusers()+" Participants");
        }


      /*  if (bo.get_image() != null) {
            byte[] image = bo.get_image();
            Bitmap PictureProfile = DbBitmapUtility.getImage(image);
            viewHolder.image.setImageBitmap(PictureProfile);
        }*/

        if (bo.getListParticiant()!=null){
            if (bo.getListParticiant().size()>0){
                viewHolder.llGallery.removeAllViews();
                for (Group users : bo.getListParticiant()){

                    LinearLayout linearLayoutImage = new LinearLayout(mContext);
                    LinearLayout.LayoutParams layoutImageParams = new LinearLayout.LayoutParams(
                            0, ViewGroup.LayoutParams.MATCH_PARENT,0.27f
                    );
                    linearLayoutImage.setLayoutParams(layoutImageParams);
                    linearLayoutImage.setGravity(Gravity.CENTER);
                    linearLayoutImage.setWeightSum(2.0f);
                    linearLayoutImage.setOrientation(LinearLayout.VERTICAL);

                    CircleImageView imagView = new CircleImageView(mContext);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, 0,1.2f
                        );

                        lp.setMargins(3, 3, 3, 3);

                    imagView.setLayoutParams(lp);
                        imagView.setTag(users);
                    if (users.getParticipantpicurl()!=null) {
                        Bitmap PictureProfile ;
                        byte[] image = DbBitmapUtility.StringToByteArray(users.getParticipantpicurl());
                        PictureProfile = DbBitmapUtility.getImage(image);
                        imagView.setImageBitmap(PictureProfile);
                    }else {
                        imagView.setImageResource(R.drawable.icon_logo);
                    }
                    linearLayoutImage.addView(imagView);
                        viewHolder.llGallery.addView(linearLayoutImage);
                }
                //   viewHolder.adapterSafepeople.notifyDataSetChanged();
            }
        }
        viewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onJoinGroupClick(position,bo);
            }
        });

        viewHolder.invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onInviteFriends(position,bo);
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
            invite = (TextView) itemView.findViewById(R.id.invite);
            llGallery = (LinearLayout) itemView.findViewById(R.id.llGallery);
            scrollview = (HorizontalScrollView)itemView.findViewById(R.id.scrollview);
            listusers= (TextView) itemView.findViewById(R.id.listusers);
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
