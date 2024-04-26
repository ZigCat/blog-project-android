package com.github.zigcat.blogplatform.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.fragments.UserFragment;
import com.github.zigcat.blogplatform.models.User;

import java.util.List;

import lombok.Getter;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final List<User> users;

    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView nickname;
        private final TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.user_item_nickname);
            username = itemView.findViewById(R.id.user_item_username);
        }
    }

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        final UserAdapter.ViewHolder viewHolder = new UserAdapter.ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                int userId = users.get(position).getId();
                ((AppCompatActivity) parent.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new UserFragment(userId))
                        .commit();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.getNickname().setText(user.getNickname());
        holder.getUsername().setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
