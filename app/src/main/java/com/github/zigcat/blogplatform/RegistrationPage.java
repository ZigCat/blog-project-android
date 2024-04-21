package com.github.zigcat.blogplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zigcat.blogplatform.api.UserOkHttpHelper;
import com.github.zigcat.blogplatform.models.User;
import com.github.zigcat.blogplatform.models.UserRequest;

import okhttp3.Credentials;

public class RegistrationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        UserOkHttpHelper userOkHttpHelper = new UserOkHttpHelper();

        Button button = findViewById(R.id.registration_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.reg_username);
                EditText email = findViewById(R.id.reg_email);
                EditText password = findViewById(R.id.reg_password);
                UserRequest user = new UserRequest(username.getText().toString(),
                        username.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        "USER");
                String credentials = Credentials.basic(username.getText().toString(), password.getText().toString());
                userOkHttpHelper.register(user, new UserOkHttpHelper.CallbackLogin() {
                    @Override
                    public void onSuccess(User response) {
                        SharedPreferences sharedPreferences = getSharedPreferences("blogplatform", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("auth", credentials);
                        editor.putInt("id", response.getId());
                        editor.apply();
                        Intent intent = new Intent(RegistrationPage.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(e.getMessage().equals("500")){
                                    TextView pagetitle = findViewById(R.id.reg_title);
                                    pagetitle.setText(R.string.error_message);
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void goToLoginPage(View v){
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}