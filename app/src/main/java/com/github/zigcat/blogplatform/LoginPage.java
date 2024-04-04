package com.github.zigcat.blogplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void goToRegPage(View v){
        Intent intent = new Intent(this, RegistrationPage.class);
        startActivity(intent);
    }

    public void loginRequest(View v){
        OkHttpClient client = new OkHttpClient();
        EditText login = findViewById(R.id.login_username);
        EditText password = findViewById(R.id.login_password);
        String credentials = Credentials.basic(login.getText().toString(), password.getText().toString());
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/api/user/login")
                .header("Authorization", credentials)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int status = response.code();
                if(status == 200){
                    SharedPreferences sharedPreferences = getSharedPreferences("blogplatform", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("auth", credentials);
                    editor.apply();
                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView pagetitle = findViewById(R.id.login_pagetitle);
                            pagetitle.setText(R.string.error_message);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}