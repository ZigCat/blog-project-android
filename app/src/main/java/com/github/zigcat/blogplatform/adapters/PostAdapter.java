package com.github.zigcat.blogplatform.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.models.Post;

import java.util.List;

public class PostAdapter extends BaseAdapter {
    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_item, parent, false);
        }
        TextView nickname = convertView.findViewById(R.id.nickname);
        TextView username = convertView.findViewById(R.id.username);
        TextView regdate = convertView.findViewById(R.id.regdate);
        TextView content = convertView.findViewById(R.id.content);

        Post post = postList.get(position);
        nickname.setText(post.getUser().getNickname());
        username.setText(post.getUser().getUsername());
        regdate.setText(post.getCreationDate());
        content.setText(post.getContent());

        return convertView;
    }
}
