package com.github.zigcat.blogplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    public void goToMainPage(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}