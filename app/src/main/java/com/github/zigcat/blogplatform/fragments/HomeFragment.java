package com.github.zigcat.blogplatform.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.adapters.PostAdapter;
import com.github.zigcat.blogplatform.api.PostOkHttpHelper;
import com.github.zigcat.blogplatform.models.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
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
        SharedPreferences sharedPref = getActivity().getSharedPreferences("blogplatform", Context.MODE_PRIVATE);
        String credentials = sharedPref.getString("auth", "");
        postOkHttpHelper.getAllPosts(new PostOkHttpHelper.CallbackGetPostsListener() {
            @Override
            public void onSuccess(List<Post> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = getActivity().findViewById(R.id.post_recyclerview);
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
                        TextView error = rootView.findViewById(R.id.home_post_error);
                        error.setVisibility(View.VISIBLE);
                        String errorMessage = "Error: "+e.getMessage();
                        error.setText(errorMessage);
                    }
                });
            }
        });
        Button create_post = rootView.findViewById(R.id.post_publish);
        create_post.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                TextView content = rootView.findViewById(R.id.new_content);
                if(content.getText().toString().isEmpty()){
                    content.setHint(R.string.missing_content);
                } else {
                    postOkHttpHelper.createPost(credentials, content.getText().toString(), new PostOkHttpHelper.CallbackCreateListener() {
                        @Override
                        public void onSuccess(String response) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.success, Toast.LENGTH_LONG);
                                    toast.show();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
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
        return rootView;
    }
}