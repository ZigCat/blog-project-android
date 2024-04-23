package com.github.zigcat.blogplatform.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.models.Post;

import java.util.List;

import lombok.Getter;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private final List<Post> posts;

    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView nickname;
        private final TextView username;
        private final TextView regdate;
        private final TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.nickname);
            username = itemView.findViewById(R.id.username);
            regdate = itemView.findViewById(R.id.regdate);
            content = itemView.findViewById(R.id.content);
        }
    }

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.getNickname().setText(post.getUser().getNickname());
        holder.getUsername().setText(post.getUser().getUsername());
        holder.getRegdate().setText(post.getCreationDate());
        holder.getContent().setText(post.getContent());
        Log.i("POSTADAPTER", "ELEMENT CREATED");
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
