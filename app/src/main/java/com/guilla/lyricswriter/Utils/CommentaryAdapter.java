package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guilla.lyricswriter.BO.Commentary;
import com.guilla.lyricswriter.BO.UserJoined;
import com.guilla.lyricswriter.R;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.guilla.lyricswriter.fragment.client.client_Profil_fragment.user;


public class CommentaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    OnItemDonateClickListener listener;
    private int selectedPos = 0;
    // private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<Commentary> AL_id_text = new ArrayList<Commentary>();
    Context mContext;

    public interface OnItemDonateClickListener {
        void onItemClick(int item, Commentary bo);
        void onAnswerClick(int item, Commentary bo);

    }

    public CommentaryAdapter(Context c, ArrayList<Commentary> AL_id_text, OnItemDonateClickListener listener) {
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
        if (AL_id_text != null) {
             Commentary object = AL_id_text.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_commentary, parent, false);
                return new CommentaryAdapter.ViewComment(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_commentary_answer, parent, false);
                return new CommentaryAdapter.ViewCommentAnswer(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        final Commentary bo = (Commentary) getItem(position);
        switch (bo.getType()) {
            case 0:
                ((ViewComment) holder).bind(position, listener, bo);
                ((ViewComment) holder).icon.setTag(position);
                ((ViewComment) holder).time.setTag(position);
      /*  if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }*/
                if (bo.getHasAnswer() == 0) {
                    ((ViewComment) holder).viewHasAnswer.setBackgroundColor(Color.parseColor("#FFE2E2E2"));
                }else if (bo.getHasAnswer() == 1){
                    ((ViewComment) holder).viewHasAnswer.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

                if (bo.getCommentary_username() != null) {
                    ((ViewComment) holder).username.setText(bo.getCommentary_username());
                }

                if (bo.getCommentary_usertext() != null) {
                    ((ViewComment) holder).usertext.setText(bo.getCommentary_usertext());
                    if (bo.getMentioneduser()!=null){
                        Log.d("mention","mentioned");
                        ((ViewComment) holder).usermentioned.setText("@"+bo.getMentioneduser());
                        Link mentions = new Link(((ViewComment) holder).usermentioned.getText().toString());
                        mentions.setTextColor(Color.parseColor("#00BCD4"));
                        mentions.setUnderlined(false);
                        mentions.setOnClickListener(new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                //listener.onHashTagTouch(position,bo,clickedText);
                            }
                        });
                        LinkBuilder.on(((ViewComment) holder).usermentioned)
                                .addLink(mentions)
                                .build();

                        String answer=((ViewComment) holder).usertext.getText().toString();
                        String text2 = answer.replace("@"+bo.getMentioneduser(), "");
                        ((ViewComment) holder).usertext.setText(text2);

                    }

                }



                if (bo.getCommentary_userpicprofil()!=null){
                    byte[] image = DbBitmapUtility.StringToByteArray(bo.getCommentary_userpicprofil());
                    Bitmap PictureProfile = DbBitmapUtility.getImage(image);
                    ((ViewComment) holder).icon.setImageBitmap(PictureProfile);
                }

                if (bo.getElapsedTimeCommentary()!=null){
                    ((ViewComment) holder).time.setText(bo.getElapsedTimeCommentary());
                }

                if(bo.getCommentary_usertext()!=null){
                    ((ViewComment) holder).answer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onAnswerClick(position,bo);
                        }
                    });
                }


                break;
            case 1:

                ((ViewCommentAnswer) holder).bind(position, listener, bo);
                ((ViewCommentAnswer) holder).icon.setTag(position);
                ((ViewCommentAnswer) holder).time.setTag(position);
      /*  if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }*/

                if (bo.getHasAnswer() == 0) {
                    ((ViewCommentAnswer) holder).viewHasAnswer.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }else if (bo.getHasAnswer()==1){
                    ((ViewCommentAnswer) holder).viewHasAnswer.setBackgroundColor(Color.parseColor("#FFE2E2E2"));
                }

                if (bo.getCommentary_username() != null) {
                    ((ViewCommentAnswer) holder).username.setText(bo.getCommentary_username());
                }

                if (bo.getCommentary_usertext() != null) {
                    ((ViewCommentAnswer) holder).usertext.setText(bo.getCommentary_usertext());
                    if (bo.getMentioneduser()!=null){
                        Log.d("mention","mentioned");
                        ((ViewCommentAnswer) holder).usermentioned.setText("@"+bo.getMentioneduser());
                        Link mentions = new Link(((ViewCommentAnswer) holder).usermentioned.getText().toString());
                        mentions.setTextColor(Color.parseColor("#00BCD4"));
                        mentions.setUnderlined(false);
                        mentions.setOnClickListener(new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                //listener.onHashTagTouch(position,bo,clickedText);
                            }
                        });
                        LinkBuilder.on(((ViewCommentAnswer) holder).usermentioned)
                                .addLink(mentions)
                                .build();

                        String answer=((ViewCommentAnswer) holder).usertext.getText().toString();
                        String text2 = answer.replace("@"+bo.getMentioneduser(), "");
                        ((ViewCommentAnswer) holder).usertext.setText(text2);

                    }

                }



                if (bo.getCommentary_userpicprofil()!=null){
                    byte[] image = DbBitmapUtility.StringToByteArray(bo.getCommentary_userpicprofil());
                    Bitmap PictureProfile = DbBitmapUtility.getImage(image);
                    ((ViewCommentAnswer) holder).icon.setImageBitmap(PictureProfile);
                }

                if (bo.getElapsedTimeCommentary()!=null){
                    ((ViewCommentAnswer) holder).time.setText(bo.getElapsedTimeCommentary());
                }

                if(bo.getCommentary_usertext()!=null){
                    ((ViewCommentAnswer) holder).answer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onAnswerClick(position,bo);
                        }
                    });
                }

                break;
        }
    }


    @Override
    public int getItemCount() {
        return AL_id_text.size();
    }


    // Replace the contents of a view (invoked by the layout manager)


    public class ViewComment extends RecyclerView.ViewHolder {
        TextView usertext,username,time,answer,usermentioned;
        CircleImageView icon;
        View viewHasAnswer;
        public ViewComment(View itemView) {
            super(itemView);
            usertext = (TextView) itemView.findViewById(R.id.usertext);
            usermentioned = (TextView) itemView.findViewById(R.id.usermentioned);
            username = (TextView) itemView.findViewById(R.id.username);
            icon=(CircleImageView)itemView.findViewById(R.id.logo);
            time=(TextView)itemView.findViewById(R.id.time);
            answer =(TextView)itemView.findViewById(R.id.answer);
            viewHasAnswer =(View)itemView.findViewById(R.id.barreHasAnswer);
        }


        public void bind(final int item, final OnItemDonateClickListener listener, final Commentary bo) {
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

    public class ViewCommentAnswer extends RecyclerView.ViewHolder {
        TextView usertext,username,time,answer,usermentioned;
        CircleImageView icon;
        View viewHasAnswer;

        public ViewCommentAnswer(View itemView) {
            super(itemView);
            usertext = (TextView) itemView.findViewById(R.id.usertext);
            usermentioned = (TextView) itemView.findViewById(R.id.usermentioned);
            username = (TextView) itemView.findViewById(R.id.username);
            icon=(CircleImageView)itemView.findViewById(R.id.logo);
            time=(TextView)itemView.findViewById(R.id.time);
            answer =(TextView)itemView.findViewById(R.id.answer);
            viewHasAnswer =(View)itemView.findViewById(R.id.barreHasAnswer);
        }


        public void bind(final int item, final OnItemDonateClickListener listener, final Commentary bo) {
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



    private void JoinGroupList(String key,Commentary bo) {
        String uripicture=user.getPicurl();
        UserJoined UserSession = new UserJoined(user.get_id(), user.get_name(),"",uripicture);
        DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(key).child("UsersJoined").child(user.get_id());
      //  DatabaseReference c1v2 = FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(key).child("listusers");
        //String users = bo.getListusers();
        //users = users.concat(", "+user.get_name());
        c1v2.setValue(UserSession);
    }

    public static final Pattern MENTION_PATTERN_OLD
            = Pattern.compile("@([\\p{L}\\p{InThai}$A-Za-z0-9_-]+)");

}
