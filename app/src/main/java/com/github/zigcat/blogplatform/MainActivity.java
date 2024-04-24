package com.github.zigcat.blogplatform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.zigcat.blogplatform.fragments.HomeFragment;
import com.github.zigcat.blogplatform.fragments.SearchFragment;
import com.github.zigcat.blogplatform.fragments.UserFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = getSharedPreferences("blogplatform", MODE_PRIVATE);
        int userId = sharedPref.getInt("id", -1);
        if(userId == -1){
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView menuButton = findViewById(R.id.menu_button);
        ImageView userButton = findViewById(R.id.post_user_button);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int loggerUserId = sharedPref.getInt("id", -1);
                if(loggerUserId != -1){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("page_user_id", loggerUserId);
                    editor.apply();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserFragment()).commit();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPref = getSharedPreferences("blogplatform", MODE_PRIVATE);
        switch(item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_user:
                int loggedUserId = sharedPref.getInt("id", -1);
                if(loggedUserId != -1){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("page_user_id", loggedUserId);
                    editor.apply();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserFragment()).commit();
                }
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                break;
            case R.id.nav_logout:
                sharedPref.edit().clear().apply();
                Intent intent = new Intent(this, LoginPage.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
            super.onBackPressed();
        }
    }
}