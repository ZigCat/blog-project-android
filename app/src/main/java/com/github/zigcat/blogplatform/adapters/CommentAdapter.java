package com.github.zigcat.blogplatform.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.models.Comment;

import java.util.List;

import lombok.Getter;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private final List<Comment> comments;

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

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.getNickname().setText(comment.getUser().getNickname());
        holder.getUsername().setText(comment.getUser().getUsername());
        holder.getRegdate().setText(comment.getCreationDate());
        holder.getContent().setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
