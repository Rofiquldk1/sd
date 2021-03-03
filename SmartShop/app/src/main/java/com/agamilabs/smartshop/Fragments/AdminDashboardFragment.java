package com.agamilabs.smartshop.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agamilabs.smartshop.FullScannerActivity;
import com.agamilabs.smartshop.Interfaces.AdminDashboardInterface;
import com.agamilabs.smartshop.LoginActivity;
import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.ShopAdminHome;
import com.agamilabs.smartshop.SplashScreenActivity;
import com.agamilabs.smartshop.activity.CampaignActivity;
import com.agamilabs.smartshop.activity.NewCampaignActivity;
import com.agamilabs.smartshop.activity.OrderReportActivity;
import com.agamilabs.smartshop.activity.RechargeActivity;
import com.agamilabs.smartshop.activity.ShopInboxActivity;
import com.agamilabs.smartshop.activity.StockReportActivity;
import com.agamilabs.smartshop.adapter.AdminDashboardAdapter;
import com.agamilabs.smartshop.database.MySharedPreferenceManager;
import com.agamilabs.smartshop.model.AdminDashboardModel;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class AdminDashboardFragment extends Fragment implements AdminDashboardInterface, View.OnClickListener {

    public static String SETTING_JSON_URL = "http://192.168.1.5/android/AgamiLab/agami-logbook/view_section.php";


    private RecyclerView mFragmentRecyclerview;
    private AdminDashboardAdapter mAdapter;
    private GridLayoutManager manager;

    private ImageButton mAddBtn;
    private TextView mTextName, mTextDomain, logout;
    private LinearLayout mLinearDashboardPOS, mLinearNewSale, mLinearInvoices, mLinearStockReport, mLinearDashboardcommerce,
            mLinearOrderReport, mLinearInbox, mLinearCampaign, mLinearRecharge;

    private List<AdminDashboardModel> mSettingList = new ArrayList<>();

    private MySharedPreferenceManager mySharedPreferenceManager;

    public AdminDashboardFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        mySharedPreferenceManager = new MySharedPreferenceManager(Objects.requireNonNull(getContext()));


        InitializeFields(view);
        //loadProducts();

        onLogoName(mySharedPreferenceManager.getSavedShopName(), mySharedPreferenceManager.getSavedDomain(mySharedPreferenceManager.getSavedAuthKey()));

        mLinearDashboardPOS.setOnClickListener(this);
        mLinearNewSale.setOnClickListener(this);
        mLinearInvoices.setOnClickListener(this);
        mLinearStockReport.setOnClickListener(this);
        mLinearDashboardcommerce.setOnClickListener(this);
        mLinearOrderReport.setOnClickListener(this);
        mLinearInbox.setOnClickListener(this);
        mLinearCampaign.setOnClickListener(this);
        mLinearRecharge.setOnClickListener(this);
        logout.setOnClickListener(this);


        return view;
    }

    private void loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SETTING_JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray sectionArray = object.getJSONArray("section");

                            for (int i = 0; i < sectionArray.length(); i++) {
                                JSONObject mNavigateObject = sectionArray.getJSONObject(i);
                                AdminDashboardModel aNavigationModel = new AdminDashboardModel();
                                Field[] fields = aNavigationModel.getAllFields();

                                for (int j = 0; j < fields.length; j++) {
                                    String fieldName = fields[j].getName();
                                    String fieldValueInJson = mNavigateObject.has(fieldName) ? mNavigateObject.getString(fieldName) : "";
                                    try {
                                        fields[j].set(aNavigationModel, fieldValueInJson);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                                mSettingList.add(aNavigationModel);
                            }

                            mAdapter = new AdminDashboardAdapter(getContext(), mSettingList, AdminDashboardFragment.this);
                            mFragmentRecyclerview.setAdapter(mAdapter);
                            manager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
                            mFragmentRecyclerview.setLayoutManager(manager);
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


    private void InitializeFields(View view) {
        AdminDashboardModel adm = new AdminDashboardModel();
        adm.section_title = mySharedPreferenceManager.getSavedShopName();
        adm.section_id = mySharedPreferenceManager.getSavedDomain(mySharedPreferenceManager.getSavedAuthKey());
        adm.section_identifier = mySharedPreferenceManager.getSavedShopName().substring(0, 1);

        mSettingList.clear();
        mSettingList.add(adm);
        mFragmentRecyclerview = view.findViewById(R.id.recycler_view_nagation);
        mAdapter = new AdminDashboardAdapter(getContext(), mSettingList, AdminDashboardFragment.this);
        mFragmentRecyclerview.setAdapter(mAdapter);
        manager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        mFragmentRecyclerview.setLayoutManager(manager);

//        mAddBtn = view.findViewById(R.id.image_btn_add) ;
        mTextName = view.findViewById(R.id.text_Name);
        mTextDomain = view.findViewById(R.id.text_Domain);

        mLinearDashboardPOS = view.findViewById(R.id.linear_layout_dashboard);
        mLinearNewSale = view.findViewById(R.id.linear_layout_new_sale);
        mLinearInvoices = view.findViewById(R.id.linear_layout_sale_invoices);
        mLinearStockReport = view.findViewById(R.id.linear_layout_stock_report);
        mLinearDashboardcommerce = view.findViewById(R.id.linear_layout_dashboard_ecommerce);
        mLinearOrderReport = view.findViewById(R.id.linear_layout_order_reports);
        mLinearInbox = view.findViewById(R.id.linear_layout_inbox);
        mLinearCampaign = view.findViewById(R.id.linear_layout_campaign);
        mLinearRecharge = view.findViewById(R.id.linear_layout_recharge);
        logout = view.findViewById(R.id.logout);
    }

    @Override
    public void onLogoName(String name, String domain) {
        mTextName.setText(name);
        mTextDomain.setText(domain);
    }

    public void Finish() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), ShopAdminHome.class);
                startActivity(intent);
                getActivity().finish();
            }
        }, 100);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_layout_dashboard:
                onIntent(getContext(), ShopAdminHome.class);
                break;
            case R.id.linear_layout_new_sale:
                onIntent(getContext(), FullScannerActivity.class);
                break;
            case R.id.linear_layout_sale_invoices:
                break;
            case R.id.linear_layout_stock_report:
                onIntent(getContext(), StockReportActivity.class);
                break;
            case R.id.linear_layout_dashboard_ecommerce:
                break;
            case R.id.linear_layout_order_reports:
                onIntent(getContext(), OrderReportActivity.class);
                break;
            case R.id.linear_layout_inbox:
                onIntent(getContext(), ShopInboxActivity.class);
                break;
            case R.id.linear_layout_campaign:
                onIntent(getContext(), CampaignActivity.class);
                break;
            case R.id.linear_layout_recharge:
                onIntent(getContext(), RechargeActivity.class);
                break;
            case R.id.logout:
                mySharedPreferenceManager.clearDB();
                Intent intent = new Intent(getContext(), SplashScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Objects.requireNonNull(getContext()).startActivity(intent);
                break;
        }
    }

    @Override
    public void onIntent(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }
}