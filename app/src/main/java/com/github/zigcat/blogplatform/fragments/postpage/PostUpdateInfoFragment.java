package com.github.zigcat.blogplatform.fragments.postpage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.api.PostOkHttpHelper;
import com.github.zigcat.blogplatform.models.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUpdateInfoFragment extends Fragment {
    private int postId;
    private String postContent;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_update_info, container, false);
        EditText newContent = rootView.findViewById(R.id.post_page_newcontent);
        newContent.setText(postContent);
        Button cancel = rootView.findViewById(R.id.post_page_cancel);
        Button publish = rootView.findViewById(R.id.post_page_editcontent);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("blogplatform", Context.MODE_PRIVATE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.post_page_container, new PostInfoFragment(postId, postContent, userId)).commit();
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOkHttpHelper postOkHttpHelper = new PostOkHttpHelper();
                String credentials = sharedPref.getString("auth", null);
                if(credentials != null){
                    postOkHttpHelper.updatePost(postId, credentials, newContent.getText().toString(), new PostOkHttpHelper.CallbackGetPostListener() {
                        @Override
                        public void onSuccess(Post post) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.success, Toast.LENGTH_LONG);
                                    toast.show();
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.post_page_container, new PostInfoFragment(postId, post.getContent(), post.getUser().getId()))
                                            .commit();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.server_error, Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            });
                        }
                    });
                }
            }
        });
        PostOkHttpHelper postOkHttpHelper = new PostOkHttpHelper();

        return rootView;
    }
}