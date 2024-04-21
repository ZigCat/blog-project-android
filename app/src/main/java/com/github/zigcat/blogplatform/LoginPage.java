package com.github.zigcat.blogplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zigcat.blogplatform.api.UserOkHttpHelper;
import com.github.zigcat.blogplatform.models.User;

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
        UserOkHttpHelper userOkHttpHelper = new UserOkHttpHelper();
        Button login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText login = findViewById(R.id.login_username);
                EditText password = findViewById(R.id.login_password);
                String credentials = Credentials.basic(login.getText().toString(), password.getText().toString());
                userOkHttpHelper.login(credentials, new UserOkHttpHelper.CallbackUser() {
                    @Override
                    public void onSuccess(User response) {
                        SharedPreferences sharedPreferences = getSharedPreferences("blogplatform", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("auth", credentials);
                        editor.putInt("id", response.getId());
                        editor.apply();
                        Intent intent = new Intent(LoginPage.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(e.getMessage().equals("500")){
                                    TextView pagetitle = findViewById(R.id.login_pagetitle);
                                    pagetitle.setText(R.string.error_message);
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void goToRegPage(View v){
        Intent intent = new Intent(this, RegistrationPage.class);
        startActivity(intent);
    }
}