package com.agamilabs.smartshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.agamilabs.smartshop.Fragments.AdminDashboardFragment;
import com.agamilabs.smartshop.Fragments.AdminHomeFragment;
import com.agamilabs.smartshop.ShopAdminHome;

public class ShopAdminHomeViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public ShopAdminHomeViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AdminDashboardFragment tab0 = new AdminDashboardFragment() ;
                return tab0;
            case 1:
                AdminHomeFragment tab1 = new AdminHomeFragment() ;
                return tab1;
            default:
                return null ;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}