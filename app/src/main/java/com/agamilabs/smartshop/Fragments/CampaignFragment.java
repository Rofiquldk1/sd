package com.agamilabs.smartshop.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.model.CampaignStatItem;

public class CampaignFragment extends Fragment {
    // Store instance variables
    private String msg,campaign,totalMsg,totalCampaign;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static CampaignFragment newInstance(CampaignStatItem campaignStatItem) {
        CampaignFragment campaignFragment = new CampaignFragment();
        Bundle args = new Bundle();
        args.putString("msg", campaignStatItem.getMessage());
        args.putString("campaign", campaignStatItem.getCampaign());
        args.putString("totalMsg", campaignStatItem.getTotal_message());
        args.putString("totalCampaign", campaignStatItem.getTotal_campaign());
        campaignFragment.setArguments(args);
        return campaignFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msg = getArguments().getString("msg");
        campaign = getArguments().getString("campaign");
        totalMsg = getArguments().getString("totalMsg");
        totalCampaign = getArguments().getString("totalCampaign");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_campaign_statistics, container, false);
        TextView txt_total_msg = (TextView) view.findViewById(R.id.total_msg);
        TextView txt_total_cam = (TextView) view.findViewById(R.id.total_campaign);
        TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        TextView txt_cam = (TextView) view.findViewById(R.id.txt_campaign);

        txt_msg.setText(msg);
        txt_cam.setText(campaign);
        txt_total_msg.setText(totalMsg);
        txt_total_cam.setText(totalCampaign);

        return view;
    }
}
