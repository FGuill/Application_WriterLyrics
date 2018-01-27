package com.guilla.lyricswriter.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guilla.lyricswriter.Application;
import com.guilla.lyricswriter.BO.Group;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.activity.ProfilActivity;

import java.util.ArrayList;

import static com.guilla.lyricswriter.R.id.name_writer;
import static com.guilla.lyricswriter.fragment.client.client_Profil_fragment.user;
import static com.guilla.lyricswriter.fragment.pro.Pro_Paiment_StepTwo_Classique_fragment.key;


public class WriterAdapter extends RecyclerView.Adapter<WriterAdapter.ViewHolder> {
    OnItemDonateClickListener listener;
    private int selectedPos = 0;
    // private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<Group> AL_id_text = new ArrayList<Group>();
    Context mContext;
    public static int currentlyFocusedRow =-1;
    public interface OnItemDonateClickListener {
       public void onItemClick(int item, Group bo);
       public void onSendNote(int item, Group bo);

    }


    public WriterAdapter(Context c, ArrayList<Group> AL_id_text, OnItemDonateClickListener listener) {
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
    public WriterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_writereditor, null);
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
        viewHolder.editor.setTag(position);
        viewHolder.name_writer.setTag(position);
        MyTextWatcher textWatcher = new MyTextWatcher(mContext,viewHolder.editor,key,viewHolder.name_writer,bo);
        viewHolder.editor.addTextChangedListener(textWatcher);
      /*  viewHolder.editor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                currentlyFocusedRow=position;
                recyclerview.getLayoutManager().scrollToPosition(currentlyFocusedRow+2);
                viewHolder.editor.requestFocus();
                Log.d("FocusEditText",String.valueOf(currentlyFocusedRow));
            }
        });*/
      /*  if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }*/

        if (bo.getCreatorname()!=null){
            Log.d("totaluserbb",String.valueOf(bo.getCreatorname()));

            viewHolder.name_writer.setText(bo.getCreatorname());
            if (viewHolder.name_writer.getText().toString().equalsIgnoreCase(user.get_name())){
                viewHolder.editor.requestFocus();;

                viewHolder.name_writer.setTextColor(mContext.getResources().getColor(R.color.greensafe));
            }else {
                viewHolder.name_writer.setTextColor(mContext.getResources().getColor(R.color.blue));
            }

        }



        if (bo.getGroupname()!=null){
            viewHolder.editor.setText(bo.getGroupname());
        }else {
                viewHolder.editor.setHint(Application.getContext().getResources().getString(R.string.writetext));
        }

        if (viewHolder.email!=null) {
            viewHolder.email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            listener.onSendNote(position,bo);
                }
            });
        }
      /*  if (bo.get_image() != null) {
            byte[] image = bo.get_image();
            Bitmap PictureProfile = DbBitmapUtility.getImage(image);
            viewHolder.image.setImageBitmap(PictureProfile);
        }*/
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_writer;
        LinedEditText editor;
        RelativeLayout background;
        ImageView email;
        public ViewHolder(View itemView) {
            super(itemView);
            name_writer = (TextView) itemView.findViewById(R.id.name_writer);
            editor = (LinedEditText) itemView.findViewById(R.id.editor);
            editor.setSingleLine(false);
            email = (ImageView) itemView.findViewById(R.id.email);
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

    public ArrayList<Group> getListItemRecycler(){
        return AL_id_text==null?null:AL_id_text;
    }
}
