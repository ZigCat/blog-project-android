package com.github.zigcat.blogplatform.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.adapters.PostAdapter;
import com.github.zigcat.blogplatform.api.PostOkHttpHelper;
import com.github.zigcat.blogplatform.models.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private final PostOkHttpHelper postOkHttpHelper = new PostOkHttpHelper();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        postOkHttpHelper.getAllPosts(new PostOkHttpHelper.CallbackGetPostsListener() {
            @Override
            public void onSuccess(List<Post> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListView listView = getView().findViewById(R.id.post_listview);
                        PostAdapter postAdapter = new PostAdapter(response);
                        listView.setAdapter(postAdapter);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView error = rootView.findViewById(R.id.home_post_error);
                        error.setVisibility(View.VISIBLE);
                        error.setText("Error: "+e.getMessage());
                    }
                });
            }
        });
        return rootView;
    }
}