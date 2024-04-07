package com.github.zigcat.blogplatform.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.adapters.PostAdapter;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        new RetrievePostsTask().execute();
        return rootView;
    }

    private class RetrievePostsTask extends AsyncTask<Void, Void, List<Post>> {
        @Override
        protected List<Post> doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/post")
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                Gson gson = new Gson();
                Type postListType = new TypeToken<List<Post>>() {}.getType();
                return gson.fromJson(response.body().string(), postListType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            ListView listView = getView().findViewById(R.id.post_listview);
            PostAdapter postAdapter = new PostAdapter(posts);
            listView.setAdapter(postAdapter);
        }
    }
}