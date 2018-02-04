package io.gresse.hugo.flo;

import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Listener mListener;
    private List<Message> mData;
    private User user;
    static final int TYPE_SENT = 1;
    static final int TYPE_RECEIVED = 0;

    public MessageAdapter(Listener listener, List<Message> data,User user) {
        mListener = listener;
        mData = data;
        this.user=user;
    }

    public void setData(List<Message> data) {
        mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==TYPE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_messages, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_messages2, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        ImageView mUserImageView;
        TextView  mUserTextView;
        TextView  mContentTextView;
        RelativeTimeTextView mTime;

        ViewHolder(View itemView) {
            super(itemView);

           // itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mUserImageView = itemView.findViewById(R.id.userImageView);
            mUserTextView = itemView.findViewById(R.id.userTextView);
            mContentTextView = itemView.findViewById(R.id.contentTextView);
            mTime = itemView.findViewById(R.id.timestamp);

        }

        void setData(Message message) {
            mUserTextView.setText(message.userName + ": ");
            mContentTextView.setText(message.content);
            mTime.setReferenceTime(message.timestamp);

            if (!TextUtils.isEmpty(message.userEmail)) {
                Glide
                        .with(mUserImageView.getContext())
                        .load(Constant.GRAVATAR_PREFIX + Utils.md5(message.userEmail))
                        .apply(RequestOptions.circleCropTransform())
                        .into(mUserImageView);
            } else {
                mUserImageView.setImageResource(R.color.colorAccent);
            }
        }

        /*@Override
        public void onClick(View view) {
            mListener.onItemClick(getAdapterPosition(), mData.get(getAdapterPosition()));
        }*/

        @Override
        public boolean onLongClick(View view) {
            PopupMenu popup = new PopupMenu(mContentTextView.getContext(), view);
            popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    String Item = item.toString();
                    if (Item.equals("Supprimer")){
                        mListener.onItemLongClick(getAdapterPosition(), mData.get(getAdapterPosition()));
                    }else{
                        mListener.onItemClick2(getAdapterPosition(), mData.get(getAdapterPosition()));
                    }
                    return true;
                }
            });
            popup.show();
            return true;
        }

    }

    public interface Listener {
        void onItemLongClick(int position, Message message);

        void onItemClick2(int adapterPosition, Message message);
    }

    @Override
    public int getItemViewType(int position) {
        Message message=mData.get(position);
        if(message.userEmail.equals(user.email)){
            return TYPE_SENT;
        }else{
            return TYPE_RECEIVED;
        }
    }

}
