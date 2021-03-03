package com.agamilabs.smartshop.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.agamilabs.smartshop.Fragments.CampaignFragment;
import com.agamilabs.smartshop.model.CampaignStatItem;

import java.util.ArrayList;

public class CampaignStatPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmentManager;
    private ArrayList<CampaignStatItem> mCampaignStatList;

    public CampaignStatPagerAdapter(FragmentManager fragmentManager, ArrayList<CampaignStatItem> mCampaignStatList) {
        super(fragmentManager);
        this.fragmentManager=fragmentManager;
        this.mCampaignStatList = mCampaignStatList;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return mCampaignStatList.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        if (position < mCampaignStatList.size()){
            return CampaignFragment.newInstance(mCampaignStatList.get(position));
        }
        return null;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
