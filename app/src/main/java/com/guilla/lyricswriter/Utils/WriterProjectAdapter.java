package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;

import java.util.ArrayList;

import static com.guilla.lyricswriter.fragment.client.client_Profil_fragment.user;


public class WriterProjectAdapter extends RecyclerView.Adapter<WriterProjectAdapter.ViewHolder> {
    OnItemDonateClickListener listener;
    private int selectedPos = 0;
    // private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<Group> AL_id_text = new ArrayList<Group>();
    Context mContext;

    public interface OnItemDonateClickListener {
        void onItemClick(int item, Group bo);
        void onItemWrite(int item, Group bo);

    }


    public WriterProjectAdapter(Context c, ArrayList<Group> AL_id_text, OnItemDonateClickListener listener) {
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
    public WriterProjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_projectwriter, null);
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
        viewHolder.image.setTag(position);
        viewHolder.join.setTag(position);
        viewHolder.listusers.setTag(position);
        viewHolder.write.setTag(position);
      /*  if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }*/

        if (bo.getCommentaire() != null) {
            viewHolder.group.setText(bo.getLike());
        }

        if (bo.getCreatorid() != null) {
            viewHolder.style.setText(bo.getTextTips());
        }

        if (bo.getCommentaire() != null) {
            /*StringBuffer sb = new StringBuffer("");
            for (UserJoined users : bo.getUserJoined()){
                if (bo.getUserJoined().size()>=1){
                        sb.append(users.getName()+" ,");
                }

            }*/
            viewHolder.listusers.setText("Participant : "+ bo.getCreatorid());
        }


      /*  if (bo.get_image() != null) {
            byte[] image = bo.get_image();
            Bitmap PictureProfile = DbBitmapUtility.getImage(image);
            viewHolder.image.setImageBitmap(PictureProfile);
        }*/



        viewHolder.write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemWrite(position,bo);
            }
        });

        viewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveFromGroup(bo.getIdkey(),bo);
                Toast.makeText(mContext, "Vous avez quitter le groupe "+bo.getCreatorid(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView group,style,listusers;
        Button join;
        ImageView write;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            group = (TextView) itemView.findViewById(R.id.group);
            style = (TextView) itemView.findViewById(R.id.style);
            listusers = (TextView) itemView.findViewById(R.id.listusers);
            write = (ImageView) itemView.findViewById(R.id.write);
            join = (Button) itemView.findViewById(R.id.join);

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

    private void RemoveFromGroup(String key,Group bo) {

      FirebaseDatabase.getInstance().getReference().child("GroupCreated").child(key).child("UsersJoined").child(user.get_id()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().setValue(null);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
}
