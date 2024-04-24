package com.github.zigcat.blogplatform.fragments.postpage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.zigcat.blogplatform.R;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostInfoFragment extends Fragment {
    private int postId;
    private String postContent;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_info, container, false);
        TextView content = rootView.findViewById(R.id.post_page_content);
        ImageView edit = rootView.findViewById(R.id.post_page_edit);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("blogplatform", Context.MODE_PRIVATE);
        int loggedUserId = sharedPref.getInt("id", -1);
        if(loggedUserId != -1 && loggedUserId == userId){
            edit.setVisibility(View.VISIBLE);
        }
        content.setText(postContent);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.post_page_container, new PostUpdateInfoFragment(postId, postContent, loggedUserId)).commit();
            }
        });

        return rootView;
    }
}