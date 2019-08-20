package com.st18rai.firebasechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.st18rai.firebasechat.R;
import com.st18rai.firebasechat.interfaces.Constants;
import com.st18rai.firebasechat.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UsersHolder> {
    private List<User> data;
    private ItemClickListener itemClickListener;

    public UsersRecyclerAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        final UsersHolder holder = new UsersHolder(view);

        holder.layout.setOnClickListener(view1 ->
                itemClickListener.onItemClick(holder.getAdapterPosition()));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersHolder holder, int position) {

        User user = data.get(position);

        holder.name.setText(user.getUsername());

        if (!user.getImageURL().equals(Constants.DEFAULT)) {
            Glide.with(holder.getContext()).load(user.getImageURL()).into(holder.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    static class UsersHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_name)
        TextView name;

        @BindView(R.id.avatar)
        CircleImageView avatar;

        private View layout;

        UsersHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            layout = v;
        }

        Context getContext() {
            return layout.getContext();
        }
    }
}
