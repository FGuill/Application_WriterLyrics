package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.guilla.lyricswriter.R.id.group;
import static com.guilla.lyricswriter.fragment.client.client_Profil_fragment.user;
import static com.guilla.lyricswriter.fragment.pro.Pro_Paiment_StepTwo_Classique_fragment.key;


public class AdapterMultipleRow extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    OnItemDonateClickListener listener;
    private int selectedPos = 0;
    // private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<Group> AL_id_text = new ArrayList<Group>();
    HashMap<String,String> DataEmail = new HashMap<String,String>();

    public static int itemlistpos=-1;
    Context mContext;

    public interface OnItemDonateClickListener {
        void onItemClick(int item, Group bo);
        void onJoinGroupClick(int item, Group bo);
        void onInviteFriends(int item, Group bo);
    }

    public AdapterMultipleRow(Context c, ArrayList<Group> AL_id_text, OnItemDonateClickListener listener) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_donation, parent, false);
                return new TipsViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_writereditor, parent, false);
                return new WriterViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_donation2, parent, false);
                return new TipsViewHolder(view);

            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_admob, parent, false);
                return new TipsViewAds(view);

            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listview_country, parent, false);
              //  return new FollowViewLike(view);

            case 7:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listview_country, parent, false);
             //   return new FollowViewLike(view);
            case 8:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_writereditor, parent, false);
                return new WriterViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return AL_id_text.size();
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        final Group bo = (Group) getItem(position);

        switch (bo.getType()) {
            case 0:

                ((TipsViewHolder) holder).bind(position, listener, bo);
                if (((TipsViewHolder) holder).join!=null) {
                    ((TipsViewHolder) holder).join.setTag(position);
                }
                //  viewHolder.recyclerParticiant.setTag(position);
      /*  if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }*/

               if (((TipsViewHolder) holder).group!=null){
                if (bo.getGroupname() != null) {
                    ((TipsViewHolder) holder).group.setText(bo.getGroupname());
                }
            }
                if (((TipsViewHolder) holder).creatorname!=null) {
                    if (bo.getCreatorname() != null) {
                        ((TipsViewHolder) holder).creatorname.setText(mContext.getResources().getString(R.string.creator) + " " + bo.getCreatorname());
                    }
                }

                if (((TipsViewHolder) holder).time!=null) {
                    if (bo.getTimelapsedTips() != null) {
                        ((TipsViewHolder) holder).time.setText(bo.getTimelapsedTips());
                    }
                }


                if (((TipsViewHolder) holder).listusers!=null) {
                    if (bo.getListusers() != null) {
            /*StringBuffer sb = new StringBuffer("");
            for (UserJoined users : bo.getUserJoined()){
                if (bo.getUserJoined().size()>=1){
                        sb.append(users.getName()+" ,");
                }

            }*/
                        ((TipsViewHolder) holder).listusers.setText(bo.getListusers() + " Participants");
                    }
                }

      /*  if (bo.get_image() != null) {
            byte[] image = bo.get_image();
            Bitmap PictureProfile = DbBitmapUtility.getImage(image);
            ((TipsViewHolder) holder).image.setImageBitmap(PictureProfile);
        }*/

                if (((TipsViewHolder) holder).llGallery!=null) {
                    if (bo.getListParticiant()!=null){
                    if (bo.getListParticiant().size()>0){
                        ((TipsViewHolder) holder).llGallery.removeAllViews();
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
                            ((TipsViewHolder) holder).llGallery.addView(linearLayoutImage);
                        }
                        //   ((TipsViewHolder) holder).adapterSafepeople.notifyDataSetChanged();
                    }
                }
                }

                if (((TipsViewHolder) holder).join!=null) {
                    ((TipsViewHolder) holder).join.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onJoinGroupClick(position, bo);
                        }
                    });
                }

                if (((TipsViewHolder) holder).invite!=null) {
                    ((TipsViewHolder) holder).invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onInviteFriends(position,bo);
                    }
                });
                }

                break;
            case 1:
                    ((WriterViewHolder) holder).bind(position, listener, bo);

                if (((WriterViewHolder) holder).editor!=null) {
                    ((WriterViewHolder) holder).editor.setTag(position);
                }

                if (((WriterViewHolder) holder).name_writer!=null) {
                    ((WriterViewHolder) holder).name_writer.setTag(position);
                }

                MyTextWatcher textWatcher = new MyTextWatcher(mContext, ((WriterViewHolder) holder).editor, key, ((WriterViewHolder) holder).name_writer, bo);
                ((WriterViewHolder) holder).editor.addTextChangedListener(textWatcher);

      /*  ((WriterViewHolder) holder).editor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                currentlyFocusedRow=position;
                recyclerview.getLayoutManager().scrollToPosition(currentlyFocusedRow+2);
                ((WriterViewHolder) holder).editor.requestFocus();
                Log.d("FocusEditText",String.valueOf(currentlyFocusedRow));
            }
        });*/
      /*  if(selectedPos == position){
            ((WriterViewHolder) holder).background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            ((WriterViewHolder) holder).background.setBackgroundColor(Color.TRANSPARENT);
        }*/

                if (((WriterViewHolder) holder).name_writer!=null) {
                    if (bo.getCreatorname()!=null){
                    Log.d("totaluserbb",String.valueOf(bo.getCreatorname()));

                    ((WriterViewHolder) holder).name_writer.setText(bo.getCreatorname());
                    if (((WriterViewHolder) holder).name_writer.getText().toString().equalsIgnoreCase(user.get_name())){
                        ((WriterViewHolder) holder).editor.requestFocus();;

                        ((WriterViewHolder) holder).name_writer.setTextColor(mContext.getResources().getColor(R.color.greensafe));
                    }else {
                        ((WriterViewHolder) holder).name_writer.setTextColor(mContext.getResources().getColor(R.color.blue));
                    }
                }
                }


                if (((WriterViewHolder) holder).editor!=null) {
                if (bo.getGroupname()!=null){
                    ((WriterViewHolder) holder).editor.setText(bo.getGroupname());
                }else {
                    ((WriterViewHolder) holder).editor.setHint(Application.getContext().getResources().getString(R.string.writetext));
                }
                }


                if (((WriterViewHolder) holder).email!=null) {
                    ((WriterViewHolder) holder).email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }



                break;

            case 2:

                ((TipsViewHolder) holder).bind(position, listener, bo);
                if (((TipsViewHolder) holder).join!=null) {
                    ((TipsViewHolder) holder).join.setTag(position);
                }
                //  viewHolder.recyclerParticiant.setTag(position);
      /*  if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }*/

                if (((TipsViewHolder) holder).group!=null){
                    if (bo.getGroupname() != null) {
                        ((TipsViewHolder) holder).group.setText(bo.getGroupname());
                    }
                }
                if (((TipsViewHolder) holder).creatorname!=null) {
                    if (bo.getCreatorname() != null) {
                        ((TipsViewHolder) holder).creatorname.setText(mContext.getResources().getString(R.string.creator) + " " + bo.getCreatorname());
                    }
                }

                if (((TipsViewHolder) holder).time!=null) {
                    if (bo.getTimelapsedTips() != null) {
                        ((TipsViewHolder) holder).time.setText(bo.getTimelapsedTips());
                    }
                }


                if (((TipsViewHolder) holder).listusers!=null) {
                    if (bo.getListusers() != null) {
            /*StringBuffer sb = new StringBuffer("");
            for (UserJoined users : bo.getUserJoined()){
                if (bo.getUserJoined().size()>=1){
                        sb.append(users.getName()+" ,");
                }

            }*/
                        ((TipsViewHolder) holder).listusers.setText(bo.getListusers() + " Participants");
                    }
                }

      /*  if (bo.get_image() != null) {
            byte[] image = bo.get_image();
            Bitmap PictureProfile = DbBitmapUtility.getImage(image);
            ((TipsViewHolder) holder).image.setImageBitmap(PictureProfile);
        }*/

                if (((TipsViewHolder) holder).llGallery!=null) {
                    if (bo.getListParticiant()!=null){
                        if (bo.getListParticiant().size()>0){
                            ((TipsViewHolder) holder).llGallery.removeAllViews();
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
                                ((TipsViewHolder) holder).llGallery.addView(linearLayoutImage);
                            }
                            //   ((TipsViewHolder) holder).adapterSafepeople.notifyDataSetChanged();
                        }
                    }
                }

                if (((TipsViewHolder) holder).join!=null) {
                    ((TipsViewHolder) holder).join.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onJoinGroupClick(position, bo);
                        }
                    });
                }

                if (((TipsViewHolder) holder).invite!=null) {
                    ((TipsViewHolder) holder).invite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onInviteFriends(position,bo);
                        }
                    });
                }

                break;


            case 3:
                AdRequest request = new AdRequest.Builder().build();
                ((TipsViewAds) holder).ads.loadAd(request);
                break;




        }
    }


    public class TipsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView group,style,listusers,creatorname,time,invite;
        Button join;
        RecyclerView recyclerParticiant;
        Adapter_profil_Sharity_client adapterSafepeople;
        RecyclerView.LayoutManager layoutManager2;
        private LinearLayout llGallery;
        private HorizontalScrollView scrollview;

        public TipsViewHolder(View itemView) {
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




    public class TipsViewAds extends RecyclerView.ViewHolder {
        AdView ads;

        public TipsViewAds(View itemView) {
            super(itemView);
            ads = (AdView) itemView.findViewById(R.id.adView);
        }
    }

    public class WriterViewHolder extends RecyclerView.ViewHolder {
        TextView name_writer;
        LinedEditText editor;
        RelativeLayout background;
        ImageView email;
        public WriterViewHolder(View itemView) {
            super(itemView);
            name_writer = (TextView) itemView.findViewById(R.id.name_writer);
            email = (ImageView) itemView.findViewById(R.id.email);
            editor = (LinedEditText) itemView.findViewById(R.id.editor);
            editor.setSingleLine(false);
            editor.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            background = (RelativeLayout) itemView.findViewById(R.id.background);
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




    @Override
    public int getItemViewType(int position) {
        if (AL_id_text != null) {
            Group object = AL_id_text.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }

    public static final Pattern HASHTAG_PATTERN_OLD
            = Pattern.compile("#([\\p{L}\\p{InThai}$A-Za-z0-9_-]+)");

    public void  setButtonFollow(LinearLayout buttonFollow){
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {

            buttonFollow.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.roundedbt) );
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                buttonFollow.setBackground( mContext.getResources().getDrawable(R.drawable.roundedbt));
            }
        }
    }
    public void  setButtonUnFollow(LinearLayout buttonFollow){
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            buttonFollow.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.roundedbtunfollow) );
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                buttonFollow.setBackground( mContext.getResources().getDrawable(R.drawable.roundedbtunfollow));
            }
        }
    }


    private String getTimeStamp(Date date){
        String elapsedTimeTipsAnswer = "";
        if (date!=null) {
            Long Tipstimestamp = date.getTime();
            long now = new Date().getTime();
            elapsedTimeTipsAnswer = (String) DateUtils.getRelativeTimeSpanString(Tipstimestamp, now, 0);
            Log.d("DATE", elapsedTimeTipsAnswer);
        }
        return elapsedTimeTipsAnswer==null?"":elapsedTimeTipsAnswer;
    }

}
