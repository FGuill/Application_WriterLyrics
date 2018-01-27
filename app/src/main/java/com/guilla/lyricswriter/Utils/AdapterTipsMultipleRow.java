package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.BO.Repost;
import com.guilla.lyricswriter.R;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterTipsMultipleRow extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    OnItemDonateClickListener listener;
    private int selectedPos = 0;
    // private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<Group> AL_id_text = new ArrayList<Group>();
    public static int itemlistpos=-1;
    Context mContext;

    public interface OnItemDonateClickListener  {
        void onItemClick(int item, Group bo);
        void onItemLike(int item, Group bo, boolean likedTips, ImageView likebutton);
        void onCommentary(int item, Group bo);
        void onCreatorNameTouch(int item, Group bo);
        void onHashTagTouch(int item, Group bo, String hashTag);
        void onImageCategoryTouch(int item, Group bo);
        void onShareTips(int item, Group bo);
        void onFollowButtonClick(int item,Group bo,LinearLayout button, TextView textButton);
    }

    public AdapterTipsMultipleRow(Context c, ArrayList<Group> AL_id_text, OnItemDonateClickListener listener) {
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_repost, parent, false);
                return new Repost(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_like, parent, false);
                return new TipsViewLike(view);

            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_admob, parent, false);
                return new TipsViewAds(view);

            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listview_country, parent, false);
                return new FollowViewLike(view);

            case 7:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listview_country, parent, false);
                return new FollowViewLike(view);
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
                if (((TipsViewHolder) holder).like!=null){
                    ((TipsViewHolder) holder).like.setTag(position);
                }
                if (((TipsViewHolder) holder).img_category!=null) {
                    ((TipsViewHolder) holder).img_category.setTag(position);
                }

      /*  if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }*/

                if (((TipsViewHolder) holder).text!=null) {
                    if (bo.getTextTips() != null) {
                        ((TipsViewHolder) holder).text.setText(bo.getTextTips());
                        // create a single click link to the matched twitter profiles
                        Link mentions = new Link(HASHTAG_PATTERN_OLD);
                        mentions.setTextColor(Color.parseColor("#00BCD4"));
                        mentions.setUnderlined(false);
                        mentions.setOnClickListener(new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                listener.onHashTagTouch(position, bo, clickedText);
                            }
                        });
                        LinkBuilder.on(((TipsViewHolder) holder).text)
                                .addLink(mentions)
                                .build();
                    }
                }

                if (((TipsViewHolder) holder).nbcommentaire!=null) {
                    if (bo.getCommentaire() != null) {
                        if (bo.getCommentaire().equalsIgnoreCase("0")) {
                            ((TipsViewHolder) holder).nbcommentaire.setText("0" + " " + mContext.getString(R.string.commentaires));
                        } else {
                            ((TipsViewHolder) holder).nbcommentaire.setText((mContext.getString(R.string.see) + "  " + bo.getCommentaire() + " " + mContext.getString(R.string.commentaires)));
                        }
                    }
                }

                if (((TipsViewHolder) holder).nbjaime!=null) {
                    if (bo.getCreatorid() != null) {
                        ((TipsViewHolder) holder).nbjaime.setText(bo.getLike());
                    }
                }

                if (((TipsViewHolder) holder).like!=null){
                    if (bo.isLikeTips()){
                    ((TipsViewHolder) holder).like.setImageResource(R.drawable.ic_like_touch);
                    Log.d("isLikeTips","true");
                }else {
                    ((TipsViewHolder) holder).like.setImageResource(R.drawable.ic_like);
                    Log.d("isLikeTips","false");
                }
                }

                if (((TipsViewHolder) holder).categorie!=null) {
                    if (bo.getGroup_categorie() != null) {
                        ((TipsViewHolder) holder).categorie.setText(bo.getGroup_categorie());
                        Category_In_list category_in_list = new Category_In_list(mContext, bo.getGroup_categorie(), ((TipsViewHolder) holder).categorie, ((TipsViewHolder) holder).img_category, ((TipsViewHolder) holder).barre);
                        category_in_list.setColor(bo.getGroup_categorie());
                    }
                }

                if (((TipsViewHolder) holder).img_category!=null) {
                    ((TipsViewHolder) holder).img_category.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onImageCategoryTouch(position, bo);
                        }
                    });
                }

                if (((TipsViewHolder) holder).time!=null) {
                    if (bo.getTimelapsedTips() != null) {
                        ((TipsViewHolder) holder).time.setText(bo.getTimelapsedTips());
                    }
                }

                if (((TipsViewHolder) holder).creator!=null) {
                    if (bo.getCreatorname()!=null){
                    ((TipsViewHolder) holder).creator.setText((mContext.getString(R.string.by)+" "+bo.getCreatorname()));
                }
                }

                if (((TipsViewHolder) holder).creator!=null) {
                    ((TipsViewHolder) holder).creator.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onCreatorNameTouch(position, bo);
                        }
                    });
                }

                if (((TipsViewHolder) holder).like!=null) {
                    ((TipsViewHolder) holder).like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemLike(position, bo, bo.isLikeTips(), ((TipsViewHolder) holder).like);
                        }
                    });
                }

                if (((TipsViewHolder) holder).commentary!=null) {
                    ((TipsViewHolder) holder).commentary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onCommentary(position, bo);
                        }
                    });
                }

                if (((TipsViewHolder) holder).nbcommentaire!=null) {
                    ((TipsViewHolder) holder).nbcommentaire.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onCommentary(position, bo);
                        }
                    });
                }

                if (((TipsViewHolder) holder).share!=null) {
                    ((TipsViewHolder) holder).share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onShareTips(position, bo);
                        }
                    });
                }



                break;
            case 1:
                ((Repost) holder).username_repost.setText(bo.getUsernamewho_repost()+ " à retweeter une expérience");
                ((Repost) holder).bind(position, listener, bo);
                ((Repost) holder).like.setTag(position);
                ((Repost) holder).img_category.setTag(position);

      /*  if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }*/

                if (bo.getTextTips() != null) {

                    ((Repost) holder).text.setText(bo.getTextTips());
                    // create a single click link to the matched twitter profiles
                    Link mentions = new Link(HASHTAG_PATTERN_OLD);
                    mentions.setTextColor(Color.parseColor("#00BCD4"));
                    mentions.setUnderlined(false);
                    mentions.setOnClickListener(new Link.OnClickListener() {
                        @Override
                        public void onClick(String clickedText) {
                            listener.onHashTagTouch(position,bo,clickedText);
                        }
                    });
                    LinkBuilder.on(((Repost) holder).text)
                            .addLink(mentions)
                            .build();
                }

                if (bo.getCommentaire() != null) {
                    if (bo.getCommentaire().equalsIgnoreCase("0")){
                        ((Repost) holder).nbcommentaire.setText("0"+  " "+mContext.getString(R.string.commentaires));
                    }else {
                        ((Repost) holder).nbcommentaire.setText((mContext.getString(R.string.see)+"  "+bo.getCommentaire()+ " "+mContext.getString(R.string.commentaires)));
                    }
                }

                if (bo.getCreatorid() != null) {
                    ((Repost) holder).nbjaime.setText(bo.getLike());
                }

                if (bo.isLikeTips()){
                    ((Repost) holder).like.setImageResource(R.drawable.ic_like_touch);
                    Log.d("isLikeTips","true");
                }else {
                    ((Repost) holder).like.setImageResource(R.drawable.ic_like);
                    Log.d("isLikeTips","false");
                }

                if (bo.getGroup_categorie()!=null){
                    ((Repost) holder).categorie.setText(bo.getGroup_categorie());
                    Category_In_list category_in_list=new Category_In_list(mContext,bo.getGroup_categorie(),((Repost) holder).categorie,((Repost) holder).img_category,((Repost) holder).barre);
                    category_in_list.setColor(bo.getGroup_categorie());
                }

                ((Repost) holder).img_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onImageCategoryTouch(position,bo);
                    }
                });

                if (bo.getTimelapsedTips()!=null){
                    ((Repost) holder).time.setText(bo.getTimelapsedTips());
                }

                if (bo.getCreatorname()!=null){
                    ((Repost) holder).creator.setText(mContext.getString(R.string.by)+" "+bo.getCreatorname());
                }

                ((Repost) holder).creator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onCreatorNameTouch(position,bo);
                    }
                });

                ((Repost) holder).like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemLike(position,bo,bo.isLikeTips(),((Repost) holder).like);
                    }
                });

                ((Repost) holder).commentary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onCommentary(position,bo);
                    }
                });

                ((Repost) holder).nbcommentaire.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onCommentary(position,bo);
                    }
                });

                ((Repost) holder).share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onShareTips(position,bo);
                    }
                });

                break;

            case 2:

                if (bo.getTextTips() != null) {
                    if (bo.getPrivateorpublic()!=null){
                        if (bo.getPrivateorpublic().equalsIgnoreCase("theylike")){
                            if (bo.getCreatorname()!=null){
                                ((TipsViewLike) holder).name.setText(bo.getCreatorname());
                                ((TipsViewLike) holder).text.setText(" "+mContext.getString(R.string.likesolution)+" "+"\""+bo.getTextTips()+"\"");
                            }
                        }else if (bo.getPrivateorpublic().equalsIgnoreCase("ilike")){
                            if (bo.getCreatorname()!=null){
                                Log.d("ispasses","toto");
                                ((TipsViewLike) holder).name.setText("You");
                                ((TipsViewLike) holder).text.setText(" "+mContext.getString(R.string.ilikesolution)+" "+"\""+bo.getTextTips()+"\"");
                            }
                        } else if (bo.getPrivateorpublic().equalsIgnoreCase("commentary")){
                            if (bo.getCreatorname()!=null){
                                ((TipsViewLike) holder).name.setText(bo.getCreatorname());
                                ((TipsViewLike) holder).text.setText(" "+mContext.getString(R.string.answered)+" "+"\""+bo.getTextTips()+"\"");
                            }
                        }

                    }
                    // create a single click link to the matched twitter profiles
                    Link mentions = new Link(HASHTAG_PATTERN_OLD);
                    mentions.setTextColor(Color.parseColor("#00BCD4"));
                    mentions.setUnderlined(false);
                    mentions.setOnClickListener(new Link.OnClickListener() {
                        @Override
                        public void onClick(String clickedText) {
                            listener.onHashTagTouch(position,bo,clickedText);
                        }
                    });
                    LinkBuilder.on(((TipsViewLike) holder).text)
                            .addLink(mentions)
                            .build();
                }

               /* if (bo.getGroup_categorie()!=null){
                    Category_In_list category_in_list=new Category_In_list(mContext,bo.getGroup_categorie(),null,((TipsViewLike) holder).img_category,null);
                    category_in_list.setColor(bo.getGroup_categorie());
                }*/

                if (bo.getUser_picurl()!=null) {
                    ((TipsViewLike) holder).img_category.setVisibility(View.VISIBLE);
                    Bitmap PictureProfile ;
                    byte[] image = DbBitmapUtility.StringToByteArray(bo.getUser_picurl());
                    PictureProfile = DbBitmapUtility.getImage(image);
                    ((TipsViewLike) holder).img_category.setImageBitmap(PictureProfile);
                }else {
                    ((TipsViewLike) holder).img_category.setImageResource(R.drawable.icon_logo);
                }

                if (bo.getDate()!=null){
                    ((TipsViewLike) holder).time.setText(getTimeStamp(bo.getDate()));
                }

                ((TipsViewLike) holder).img_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onImageCategoryTouch(position,bo);
                    }
                });
            break;

            case 3:
                AdRequest request = new AdRequest.Builder().build();
                ((TipsViewAds) holder).ads.loadAd(request);
                break;

            case 4:
                ((FollowViewLike) holder).imageView.setTag(position);
                ((FollowViewLike) holder).followLinear.setTag(position);

                ((FollowViewLike) holder).followLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onFollowButtonClick(position,bo,((FollowViewLike) holder).followLinear,((FollowViewLike) holder).follow);
                    }
                });

                if (bo.getDate()!=null){
                    ((FollowViewLike) holder).time.setText(getTimeStamp(bo.getDate()));
                }

                if (bo.get_UserInvite()!= null) {
                    ((FollowViewLike) holder).name_contact.setText(bo.get_UserInvite()+" "+ mContext.getString(R.string.startfollow)+" "+bo.get_ProjectName());
                }


                if (bo.getUser_picurl()!=null) {
                    ((FollowViewLike) holder).imageView.setVisibility(View.VISIBLE);
                    Bitmap PictureProfile ;
                    byte[] image = DbBitmapUtility.StringToByteArray(bo.getUser_picurl());
                    PictureProfile = DbBitmapUtility.getImage(image);
                    ((FollowViewLike) holder).imageView.setImageBitmap(PictureProfile);
                }else {
                    ((FollowViewLike) holder).imageView.setImageResource(R.drawable.icon_logo);
                }

                if (bo.get_status()!=null){

                if (bo.get_status().equalsIgnoreCase("pending")){
                    setButtonFollow(((FollowViewLike) holder).followLinear);
                    ((FollowViewLike) holder).follow.setText(Application.getContext().getResources().getString(R.string.join));
                }else if (bo.get_status().equalsIgnoreCase("invitationaccepted")){
                    setButtonUnFollow(((FollowViewLike) holder).followLinear);
                    ((FollowViewLike) holder).follow.setText(Application.getContext().getResources().getString(R.string.quit));
                }
                }

                break;

        }
    }


        public class TipsViewHolder extends RecyclerView.ViewHolder {
            TextView text,nbjaime,nbcommentaire,categorie,time,creator;
            ImageView like,commentary, img_category,share;
            View barre;
            public TipsViewHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.text);
                nbjaime = (TextView) itemView.findViewById(R.id.nbjaime);
                nbcommentaire = (TextView) itemView.findViewById(R.id.nbcommentaire);
                like= (ImageView)itemView.findViewById(R.id.like);
                commentary=(ImageView)itemView.findViewById(R.id.commentary);
                categorie=(TextView)itemView.findViewById(R.id.categorie);
                barre=(View)itemView.findViewById(R.id.barre);
                img_category=(ImageView) itemView.findViewById(R.id.img_category);
                time=(TextView)itemView.findViewById(R.id.time);
                creator=(TextView)itemView.findViewById(R.id.creator);
                share=(ImageView) itemView.findViewById(R.id.ic_share);



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

    public class Repost extends RecyclerView.ViewHolder {
        TextView text,nbjaime,nbcommentaire,categorie,time,creator,username_repost;
        ImageView like,commentary, img_category,share;
        View barre;
        public Repost(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            nbjaime = (TextView) itemView.findViewById(R.id.nbjaime);
            nbcommentaire = (TextView) itemView.findViewById(R.id.nbcommentaire);
            like= (ImageView)itemView.findViewById(R.id.like);
            commentary=(ImageView)itemView.findViewById(R.id.commentary);
            categorie=(TextView)itemView.findViewById(R.id.categorie);
            barre=(View)itemView.findViewById(R.id.barre);
            img_category=(ImageView) itemView.findViewById(R.id.img_category);
            time=(TextView)itemView.findViewById(R.id.time);
            creator=(TextView)itemView.findViewById(R.id.creator);
            username_repost=(TextView)itemView.findViewById(R.id.username_repost);
            share=(ImageView) itemView.findViewById(R.id.ic_share);

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

    public class TipsViewLike extends RecyclerView.ViewHolder {
        TextView text,time,name;
        ImageView  img_category;
        public TipsViewLike(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            img_category=(ImageView) itemView.findViewById(R.id.img_category);
            time=(TextView)itemView.findViewById(R.id.time);
            name=(TextView)itemView.findViewById(R.id.name);

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

    public class FollowViewLike extends RecyclerView.ViewHolder {
        TextView text,number_contact,name_contact,follow,time;
        CircleImageView  imageView;
        LinearLayout followLinear;
        public FollowViewLike(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            imageView=(CircleImageView) itemView.findViewById(R.id.imageView);
            time=(TextView)itemView.findViewById(R.id.time);
            name_contact=(TextView)itemView.findViewById(R.id.name_contact);
            number_contact=(TextView)itemView.findViewById(R.id.number_contact);
            follow=(TextView)itemView.findViewById(R.id.button);
            followLinear =(LinearLayout)itemView.findViewById(R.id.buttonLayout);

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
