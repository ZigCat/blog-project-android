package com.github.zigcat.blogplatform.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.adapters.PostAdapter;
import com.github.zigcat.blogplatform.api.PostOkHttpHelper;
import com.github.zigcat.blogplatform.fragments.userpage.UserInfoFragment;
import com.github.zigcat.blogplatform.models.Post;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserFragment extends Fragment {
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.user_fragment_container, new UserInfoFragment(userId))
                .commit();

        PostOkHttpHelper postOkHttpHelper = new PostOkHttpHelper();

        postOkHttpHelper.getPostsByUserId(userId, new PostOkHttpHelper.CallbackGetPostsListener() {
            @Override
            public void onSuccess(List<Post> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = getActivity().findViewById(R.id.user_post_recyclerview);
                        Collections.reverse(response);
                        PostAdapter postAdapter = new PostAdapter(response);
                        recyclerView.setAdapter(postAdapter);
                        postAdapter.notifyDataSetChanged();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView error = rootView.findViewById(R.id.user_post_error);
                        error.setVisibility(View.VISIBLE);
                        String errorMessage = "Error: "+e.getMessage();
                        error.setText(errorMessage);
                    }
                });
            }
        });

        return rootView;
    }
}