package com.agamilabs.smartshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.agamilabs.smartshop.adapter.ShopAdminHomeViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class ShopAdminHome extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ShopAdminHomeViewPagerAdapter mAdapter;

    private FloatingActionButton options_fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_admin_home);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        options_fab = findViewById(R.id.options_fab);
        options_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options_fab.hide();

                
            }
        });

        mTabLayout.addTab(mTabLayout.newTab().setText("Dashboard Fragment"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Home Fragment"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setVisibility(View.GONE);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mAdapter = new ShopAdminHomeViewPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount() );
        mViewPager.setAdapter(mAdapter);

        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0){
                    mViewPager.setPadding(0, 0, 100, 0) ;
                    mViewPager.setClipToPadding(false) ;
                }
                else if(position==1){
                    mViewPager.setPadding(0, 0, 0, 0) ;
                    mViewPager.setClipToPadding(false) ;
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSelect(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    public void setSelect(int position){
        mViewPager.setCurrentItem(position);
        //mViewPager.setPadding(0, 0, 0, 0);
        //mViewPager.setClipToPadding(false);

    }
}