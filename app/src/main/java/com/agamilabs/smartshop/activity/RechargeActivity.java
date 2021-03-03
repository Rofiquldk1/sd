package com.agamilabs.smartshop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.agamilabs.smartshop.R;

public class RechargeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_campaign_statistics);

        setTitle("Recharge");
    }
}