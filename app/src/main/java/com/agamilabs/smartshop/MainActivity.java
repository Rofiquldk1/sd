package com.agamilabs.smartshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //topic
//        FirebaseMessaging.getInstance().subscribeToTopic("general") ;
//        FirebaseMessaging.getInstance().subscribeToTopic("guest") ;
    }

    public void launchBarCode(View view){
        startActivity(new Intent(this, FullScannerActivity.class));
    }

    public void launchLogin(View v){
        startActivity(new Intent(this, LoginActivity.class));
    }


}