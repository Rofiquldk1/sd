package com.agamilabs.smartshop.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.Fragments.AdminDashboardFragment;
import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.model.AdminDashboardModel;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdminDashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCtx;
    private List<AdminDashboardModel> mSettingList;
    private String mLogName, mLogDomain;

    private AdminDashboardFragment mDashboardFragment;
    private SharedPreferences PREF_POSITION;
    private String mPositionId;

    public AdminDashboardAdapter(Context mCtx, List<AdminDashboardModel> mSettingList, AdminDashboardFragment mDashboard) {
        this.mCtx = mCtx;
        this.mSettingList = mSettingList;
        this.mDashboardFragment = mDashboard;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_admin_dashboard_item, null);
        PREF_POSITION = mCtx.getSharedPreferences("positionId", MODE_PRIVATE);

        return new AdminDashboardAdapter.PostViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final AdminDashboardModel mNavigation = mSettingList.get(position);
        ((PostViewHolder) holder).bind(mNavigation, position);
    }

    @Override
    public int getItemCount() {
        return mSettingList.size();
    }


    class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewLogoName, mTextViewLogoText;
        private CardView mLinearLayout;


        public PostViewHolder(View itemView) {
            super(itemView);

            mTextViewLogoName = itemView.findViewById(R.id.text_identifier);
            mTextViewLogoText = itemView.findViewById(R.id.text_name);
            mLinearLayout = itemView.findViewById(R.id.linear_layout);


        }

        public void bind(final AdminDashboardModel settings, final int position){


            mTextViewLogoName.setText(settings.getSection_identifier());
            mTextViewLogoText.setText(settings.getSection_title());

//========    give position
            mPositionId = PREF_POSITION.getString("positionId", "");
            if(!mPositionId.equals("")){
                if(Integer.valueOf(mPositionId) == position){
                    mLinearLayout.setBackgroundResource(R.drawable.input);
                }
            }
            else{
                mTextViewLogoName.setBackgroundResource(R.drawable.background);
            }
            mTextViewLogoName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //mLogName = settings.getSection_title();
                    //mLogDomain = "agamilabs.slack.com" ;

                    //mDashboardFragment.sharedSaved(PREF_POSITION,"positionId", String.valueOf(position));
                    mDashboardFragment.Finish();
                    //mDashboardFragment.onLogoName(mLogName, mLogDomain);
                }
            });

//            notifyDataSetChanged();

        }



    }

}