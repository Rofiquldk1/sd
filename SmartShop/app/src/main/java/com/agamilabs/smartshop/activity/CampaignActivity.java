package com.agamilabs.smartshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.agamilabs.smartshop.Interfaces.CallBack;
import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.adapter.CampaignAdapter;
import com.agamilabs.smartshop.adapter.CampaignStatPagerAdapter;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.model.CampaignItem;
import com.agamilabs.smartshop.model.CampaignStatItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CampaignActivity extends AppCompatActivity implements CallBack, View.OnClickListener {
    private RecyclerView rvCampaign;
    private Button btn_load_all_campaigns,btn_load_active_campaigns,btn_load_pause_campaigns,btn_load_suspend_campaigns;
    private String urlCampaign = "http://192.168.0.105/smartShop/ConvertPHPCampaign.php";
    //private String urlCampaignStatistics = "http://192.168.0.105/smartShop/ConvertPHPCampaignStatistics.php";
    private String urlCampaignStatistics = "http://192.168.0.105/smartShop/campaign_statistics_2.json";
    private String urlCampaignStatusChange = "http://192.168.1.10/android/AgamiLab/smart_shop/campaign_statistics.json";
    private ArrayList<CampaignItem> mCampaignList;
    private ArrayList<CampaignStatItem> mCampaignStatList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue mRequestQueueCampaign,mRequestQueueCampaignStat,mRequestQueueCampaignChangeStatus;
    private CampaignAdapter mCampaignAdapter;
    private FragmentPagerAdapter mCampaignStatPagerAdapter;
    LinearLayoutManager managerCampaign;
    private ShimmerFrameLayout shimmerFrameLayout;
    private int pageNumber=1,newStatus=5,page=0;
    private CountDownTimer countDownTimerForViewPager,countDownTimerForRefresh;
    long remainingRefreshTime=1000,remainingSwapingTime=10*60*1000;
    private ViewPager mViewPager;
    private boolean forward = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        initialize();
        initializeAdapters();

        loadCampaignStatList();
        loadCampaignList(pageNumber,newStatus);


        swipeRefreshLayout.setColorSchemeResources(R.color.darkblue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCampaignAdapter.setAllItemsEnabled(false);
                cancelAutoSwap();
                setAutoRefresh();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_load_all_campaigns.setOnClickListener(this);
        btn_load_active_campaigns.setOnClickListener(this);
        btn_load_pause_campaigns.setOnClickListener(this);
        btn_load_suspend_campaigns.setOnClickListener(this);
        setAutoSwap();
    }

    private void setAutoSwap() {
        if(remainingSwapingTime<=0){
            if(countDownTimerForViewPager!= null){
                countDownTimerForViewPager.cancel();
            }
            return;
        }

        if(countDownTimerForViewPager == null){
            countDownTimerForViewPager = new CountDownTimer(remainingSwapingTime, 4500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d("Page",page+"");
                    mViewPager.setCurrentItem(page, true);
                    if (page == 0) {
                        forward=true;
                        page++;
                    } else if (mCampaignStatList.size()-1 == page) {
                        forward=false;
                        page--;
                    } else {
                        if(forward){
                            page++;
                        }
                        else{
                            page--;
                        }
                    }
                }

                @Override
                public void onFinish() {
                    cancelAutoSwap();
                    setAutoSwap();
                }
            };

            countDownTimerForViewPager.start() ;
        }
    }

    private void cancelAutoSwap() {
        if(countDownTimerForViewPager!= null){
            countDownTimerForViewPager.cancel();
            countDownTimerForViewPager=null;
        }
    }

    @Override
    public void onClick(View view) {
        if(view == btn_load_all_campaigns){
            //5 for all
            newStatus=5;
            mCampaignList.clear();
            loadCampaignList(1,newStatus);
            mCampaignAdapter.notifyDataSetChanged();
            btn_load_all_campaigns.setBackgroundColor(getResources().getColor(R.color.highlight_color));
            btn_load_active_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
            btn_load_pause_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
            btn_load_suspend_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
        }
        else if(view == btn_load_active_campaigns){
            //1 for active
            newStatus=1;
            mCampaignList.clear();
            loadCampaignList(1,newStatus);
            mCampaignAdapter.notifyDataSetChanged();
            btn_load_active_campaigns.setBackgroundColor(getResources().getColor(R.color.highlight_color));
            btn_load_all_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
            btn_load_pause_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
            btn_load_suspend_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
        }
        else if(view == btn_load_pause_campaigns){
           //0 for pause
            newStatus=0;
            mCampaignList.clear();
            loadCampaignList(1,newStatus);
            mCampaignAdapter.notifyDataSetChanged();
            btn_load_pause_campaigns.setBackgroundColor(getResources().getColor(R.color.highlight_color));
            btn_load_active_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
            btn_load_all_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
            btn_load_suspend_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
        }
        else if(view == btn_load_suspend_campaigns){
           //-1 for suspend
            newStatus=-1;
            mCampaignList.clear();
            loadCampaignList(1,newStatus);
            mCampaignAdapter.notifyDataSetChanged();
            btn_load_suspend_campaigns.setBackgroundColor(getResources().getColor(R.color.highlight_color));
            btn_load_pause_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
            btn_load_active_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
            btn_load_all_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
        }

    }

    private void loadCampaignStatList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCampaignStatistics,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("error") && !response.getBoolean("error")) {
                                JSONArray campaignStatArray = response.getJSONArray("data");
                                for (int i = 0; i < campaignStatArray.length(); i++) {
                                    JSONObject mCampaignStatObject = campaignStatArray.getJSONObject(i);
                                    CampaignStatItem campaignStatModel = new CampaignStatItem();
                                    Field[] fields = campaignStatModel.getClass().getDeclaredFields();

                                    for (int j = 0; j < fields.length; j++) {
                                        String fieldName = fields[j].getName();
                                        String fieldValueInJson = mCampaignStatObject.has(fieldName) ? mCampaignStatObject.getString(fieldName) : "";
                                        try {
                                            fields[j].set(campaignStatModel, fieldValueInJson);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    mCampaignStatList.add(campaignStatModel);
                                }
                                mCampaignStatPagerAdapter =new CampaignStatPagerAdapter(getSupportFragmentManager(),mCampaignStatList);
                                mViewPager.setAdapter(mCampaignStatPagerAdapter);
                                initMagicIndicator();
                            }
                            else{
                                Toast.makeText(CampaignActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueueCampaignStat.add(jsonObjectRequest);
    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator2);
        CircleNavigator circleNavigator = new CircleNavigator(this);
        circleNavigator.setFollowTouch(false);
        circleNavigator.setCircleCount(mCampaignStatList.size());
        circleNavigator.setCircleColor(Color.BLUE);
        circleNavigator.setCircleSpacing(20);
        circleNavigator.setRadius(20);
        circleNavigator.setStrokeWidth(2);

        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                mViewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void setAutoRefresh() {
        //if already countdowntime nul na hole, countdowntimer k stop korbe.
        //max refresh time 0 or 0 theke chotu hoi, uporer kaj ta korbe..
        if(remainingRefreshTime<=0){
            if(countDownTimerForRefresh!= null){
                countDownTimerForRefresh.cancel();
            }
            return;
        }

        if(countDownTimerForRefresh == null){
            countDownTimerForRefresh = new CountDownTimer(remainingRefreshTime, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mCampaignList.clear();
                    mCampaignStatList.clear();
                    //mCampaignAdapter.setCardClickable(false);
                }

                @Override
                public void onFinish() {
                    swipeRefreshLayout.setRefreshing(false);
                    loadCampaignStatList();
                    loadCampaignList(1,5);
                    mCampaignAdapter.notifyDataSetChanged();
                    mCampaignStatPagerAdapter.notifyDataSetChanged();
                    page=0;
                    setAutoSwap();
                    cancelAutoRefresh();
                    btn_load_all_campaigns.setBackgroundColor(getResources().getColor(R.color.highlight_color));
                    btn_load_active_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
                    btn_load_pause_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
                    btn_load_suspend_campaigns.setBackgroundColor(getResources().getColor(R.color.deafult_color));
                }
            };

            countDownTimerForRefresh.start() ;
        }
    }

    private void cancelAutoRefresh() {
        if(countDownTimerForRefresh!= null){
            countDownTimerForRefresh.cancel();
            countDownTimerForRefresh=null;
            mCampaignAdapter.setAllItemsEnabled(true);
        }
    }

    private void loadCampaignList(int pageNum,int status) {
        pageNumber=Math.max(pageNum, 1) ;
        HashMap<String, String> map = new HashMap<>() ;
        map.put("pageNumber", pageNumber+"") ;
        map.put("status", status+"") ;
        AppController.getAppController().getAppNetworkController().makeRequest(urlCampaign, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppController.getAppController().getInAppNotifier().log("response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.has("error") && !object.getBoolean("error")){
                        JSONArray campaignArray = object.getJSONArray("data");
                        ForLoadData(campaignArray);
                        mCampaignAdapter.setPageNumber(pageNumber);
                        mCampaignAdapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(CampaignActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map );
    }

    private void ForLoadData(JSONArray campaignArray) {
        for(int i=0; i<campaignArray.length(); i++){
            JSONObject mCampaignObject = null;
            try {
                mCampaignObject = campaignArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CampaignItem campaignModel = new CampaignItem();
            Field[] fields = campaignModel.getClass().getDeclaredFields();

            for(int j=0;j<fields.length;j++){
                String fieldName = fields[j].getName();
                String fieldValueInJson = null;
                try {
                    fieldValueInJson = mCampaignObject.has(fieldName)? mCampaignObject.getString(fieldName) : "";
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    fields[j].set(campaignModel,fieldValueInJson);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            mCampaignList.add(campaignModel);
            //AppController.getAppController().getInAppNotifier().log("response", mStockList.size()+"");
        }

        //AppController.getAppController().getInAppNotifier().log("response", mStockList.size()+"");
    }

    private void initializeAdapters() {
        rvCampaign.setHasFixedSize(true);
        managerCampaign = new LinearLayoutManager(CampaignActivity.this,LinearLayoutManager.VERTICAL,false);
        rvCampaign.setLayoutManager(managerCampaign);
        mCampaignAdapter = new CampaignAdapter(CampaignActivity.this, mCampaignList,CampaignActivity.this){
            @Override
            public void loadNextPage(int pageNumber) {
                loadCampaignList(pageNumber,newStatus);
                AppController.getAppController().getInAppNotifier().log("RequestingPageNumber", pageNumber+"");
            }
        };
        rvCampaign.setAdapter(mCampaignAdapter);
    }

    private void initialize() {
        btn_load_all_campaigns = findViewById(R.id.btn_load_all_campaign);
        btn_load_active_campaigns = findViewById(R.id.btn_load_active_campaigns);
        btn_load_pause_campaigns = findViewById(R.id.btn_load_pause_campaigns);
        btn_load_suspend_campaigns = findViewById(R.id.btn_load_suspend_campaigns);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        rvCampaign = findViewById(R.id.recycler_view_campaign);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        //scroller = findViewById(R.id.nestedScroll);
        //shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        mCampaignList = new ArrayList<CampaignItem>();
        mCampaignStatList = new ArrayList<CampaignStatItem>();
        mRequestQueueCampaign = Volley.newRequestQueue(this);
        mRequestQueueCampaignStat = Volley.newRequestQueue(this);
        mRequestQueueCampaignChangeStatus = Volley.newRequestQueue(this);
    }

    private void parseJSONChangeStatus(final String scheduleNo,final String status){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlCampaignStatusChange, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("error") && !response.getBoolean("error")) {
                                loadCampaignList(1,newStatus);
                            }
                            else {
                                Toast.makeText(CampaignActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CampaignActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("scheduleNo",scheduleNo);
                params.put("toStatus", status);
                return params;
            }
        };
        mRequestQueueCampaignChangeStatus.add(request);
    }

    @Override
    public void onClickButton(int position,String scheduleNo) {
        if(position == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to pause this campaign?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            parseJSONChangeStatus(scheduleNo,"0");
                        }
                    });

            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else if(position == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to continue this campaign?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            parseJSONChangeStatus(scheduleNo,"1");
                        }
                    });

            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else if(position == 2){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to cancel this campaign?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            parseJSONChangeStatus(scheduleNo,"-1");
                        }
                    });

            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    @Override
    public void duplicateInfo(String schedule_no, String message_title, String message, String catid, String session, String apiid, String schedule_starting_datetime, String schedule_particular_number, String priority, String status, String schedule_entry_datetime,
                              String total_subscribers, String total_sent_subscribers) {
        mCampaignList.add(new CampaignItem(
                schedule_no,message_title, message, catid, session, apiid, schedule_starting_datetime,  schedule_particular_number,  priority,  status,  schedule_entry_datetime,
                total_subscribers,total_sent_subscribers
        ));
        mCampaignAdapter.notifyDataSetChanged();
        /*NewCampaignActivity.getActivityInstance().sendDuplicateInfo(schedule_no,message_title, message, catid, session, apiid, schedule_starting_datetime,  schedule_particular_number,  priority,  status,  schedule_entry_datetime,
                total_subscribers,  total_sent_subscribers);*/
    }

}