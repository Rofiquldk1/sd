package com.agamilabs.smartshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.adapter.AdminHomeFragmentAdapter;
import com.agamilabs.smartshop.model.NotifyModel;

import java.util.ArrayList;
import java.util.List;

public class CardShopActivity extends AppCompatActivity {

    RecyclerView mRecyclerList;
    List<NotifyModel> mCommerceList;
    AdminHomeFragmentAdapter mAdminAdapter;

    String[] mCommerceName = {
            "Smart POS",
            "Smart e-Commerce",
            "Turtle SMS"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_shop);



        mRecyclerList = findViewById(R.id.recycler_card_shop) ;
        mRecyclerList.setHasFixedSize(true);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(getApplicationContext() ,LinearLayoutManager.VERTICAL,false));



        mCommerceList = new ArrayList<>();

        for(int i=0; i<mCommerceName.length; i++){
            mCommerceList.add(
                    new NotifyModel(
                            String.valueOf(i+1),
                            mCommerceName[i]
                    )
            );
        }

        mAdminAdapter = new AdminHomeFragmentAdapter(getApplicationContext(), mCommerceList);
        mRecyclerList.setAdapter(mAdminAdapter);

        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerList.setLayoutManager(manager);


    }
}