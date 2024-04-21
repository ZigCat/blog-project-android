package com.github.zigcat.blogplatform.api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.zigcat.blogplatform.LoginPage;
import com.github.zigcat.blogplatform.MainActivity;
import com.github.zigcat.blogplatform.R;
import com.github.zigcat.blogplatform.models.Post;
import com.github.zigcat.blogplatform.models.User;
import com.github.zigcat.blogplatform.models.UserRequest;
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

public class UserOkHttpHelper {
    private static final String BASE_URL = "http://10.0.2.2:8080/api/user";

    public interface CallbackLogin{
        void onSuccess(User response);
        void onFailure(Exception e);
    }
    public void register(UserRequest user, CallbackLogin callback){
        Gson gson = new Gson();
        String requestBody = gson.toJson(user);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+"/register")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    Type postListType = new TypeToken<User>() {}.getType();
                    callback.onSuccess(gson.fromJson(response.body().string(), postListType));
                } else {
                    callback.onFailure(new Exception("500"));
                }
            }
        });
    }
    public void login(String credentials, CallbackLogin callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+"/login")
                .header("Authorization", credentials)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    Type postListType = new TypeToken<User>() {}.getType();
                    callback.onSuccess(gson.fromJson(response.body().string(), postListType));
                } else {
                    callback.onFailure(new Exception("500"));
                }
            }
        });
    }
}
