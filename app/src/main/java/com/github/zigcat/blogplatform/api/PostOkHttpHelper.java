package com.github.zigcat.blogplatform.api;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.models.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostOkHttpHelper {
    private static final String BASE_URL = "http://"+ServerIP.getCurrentIP()+":8080/api/post";

    public interface CallbackGetPostsListener{
        void onSuccess(List<Post> response);
        void onFailure(Exception e);
    }

    public interface CallbackCreateListener{
        void onSuccess(String response);
        void onFailure(Exception e);
    }

    public interface CallbackGetPostListener{
        void onSuccess(Post post);
        void onFailure(Exception e);
    }

    public void updatePost(int id, String credentials, String newContent, CallbackGetPostListener callback){
        OkHttpClient client = new OkHttpClient();
        String requestBody = "{\"content\":\""+newContent+"\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody);
        Request request = new Request.Builder()
                .url(BASE_URL+"/update/"+id)
                .header("Authorization", credentials)
                .patch(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    Type postListType = new TypeToken<Post>() {}.getType();
                    callback.onSuccess(gson.fromJson(response.body().string(), postListType));
                } else {
                    callback.onFailure(new Exception(String.valueOf(response.code())));
                }
            }
        });
    }

    public void getPostById(int id, CallbackGetPostListener callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+"/id/"+id)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    Type postListType = new TypeToken<Post>() {}.getType();
                    callback.onSuccess(gson.fromJson(response.body().string(), postListType));
                } else {
                    callback.onFailure(new Exception(String.valueOf(response.code())));
                }
            }
        });
    }

    public void getPostsByUserId(int userId, CallbackGetPostsListener callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+"/user/"+userId)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    Type postListType = new TypeToken<List<Post>>() {}.getType();
                    callback.onSuccess(gson.fromJson(response.body().string(), postListType));
                } else {
                    callback.onFailure(new Exception(String.valueOf(response.code())));
                }
            }
        });
    }

    public void getAllPosts(CallbackGetPostsListener callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(BASE_URL).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(new Exception("500"));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    Type postListType = new TypeToken<List<Post>>() {}.getType();
                    callback.onSuccess(gson.fromJson(response.body().string(), postListType));
                } else {
                    callback.onFailure(new Exception(String.valueOf(response.code())));
                }
            }
        });
    }

    public void createPost(String credentials, String content, CallbackCreateListener callback){
        String requestBody = "{\"content\":\""+content+"\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+"/create")
                .header("Authorization", credentials)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(new Exception("500"));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    callback.onSuccess("200");
                } else {
                    callback.onFailure(new Exception(String.valueOf(response.code())));
                }
            }
        });
    }
}
