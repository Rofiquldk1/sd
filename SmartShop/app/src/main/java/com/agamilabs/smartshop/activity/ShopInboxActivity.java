package com.agamilabs.smartshop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.agamilabs.smartshop.R;

public class ShopInboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_inbox);

        setTitle("Inbox");
    }
}