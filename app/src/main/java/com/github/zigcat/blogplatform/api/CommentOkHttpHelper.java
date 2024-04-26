package com.github.zigcat.blogplatform.api;

import androidx.annotation.NonNull;

import com.github.zigcat.blogplatform.models.Comment;
import com.github.zigcat.blogplatform.models.User;
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

public class CommentOkHttpHelper {
    private static final String BASE_URL = "http://"+ServerIP.getCurrentIP()+":8080/api/comment";

    public interface CallbackResponseString{
        void onSuccess(String response);
        void onFailure(Exception e);
    }

    public interface CallbackComments{
        void onSuccess(List<Comment> response);
        void onFailure(Exception e);
    }

    public void getCommentsByPost(int id, CallbackComments callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+"/post/"+id)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.code() == 200){
                    Gson gson = new Gson();
                    Type postListType = new TypeToken<List<Comment>>() {}.getType();
                    callback.onSuccess(gson.fromJson(response.body().string(), postListType));
                } else {
                    callback.onFailure(new Exception("500"));
                }
            }
        });
    }

    public void createComment(CommentRequest comment, String credentials, CallbackResponseString callback){
        Gson gson = new Gson();
        String requestBody = gson.toJson(comment);
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
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.code() == 200){
                    callback.onSuccess("200");
                } else {
                    callback.onFailure(new Exception("500"));
                }
            }
        });
    }
}
