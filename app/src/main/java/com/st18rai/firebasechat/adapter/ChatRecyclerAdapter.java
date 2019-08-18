package com.st18rai.firebasechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.st18rai.firebasechat.R;
import com.st18rai.firebasechat.model.Chat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatHolder> {

    private final int MSG_TYPE_LEFT = 0;
    private final int MSG_TYPE_RIGHT = 1;

    private FirebaseUser firebaseUser;

    private List<Chat> data;

    public ChatRecyclerAdapter() {

    }

    public List<Chat> getData() {
        return data;
    }

    public void setData(List<Chat> data) {
        this.data = data;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_left, parent, false);

            return new ChatHolder(view);

        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_right, parent, false);

            return new ChatHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ChatHolder holder, int position) {

        Chat chat = data.get(position);

        holder.message_text.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (data.get(position).getSender().equals(firebaseUser.getUid()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;

    }

    static class ChatHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_text)
        TextView message_text;

        private View layout;

        ChatHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            layout = v;
        }

        public Context getContext() {
            return layout.getContext();
        }
    }
}
