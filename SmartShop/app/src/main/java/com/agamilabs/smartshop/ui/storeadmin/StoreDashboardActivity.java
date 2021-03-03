package com.agamilabs.smartshop.ui.storeadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.constants.AppServerURL;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.ui.storeadmin.adapters.StoreDashboardCartSummaryAdapter;
import com.agamilabs.smartshop.ui.storeadmin.adapters.StoreDashboardTopCustomersAdapter;
import com.agamilabs.smartshop.ui.storeadmin.adapters.StoreDashboardTopProductsAdapter;
//import com.agamilabs.smartshop.ui.storeadmin.database.StoreAdminLocal;
import com.agamilabs.smartshop.ui.storeadmin.models.StoreDashboardCartSummaryModel;
import com.agamilabs.smartshop.ui.storeadmin.models.StoreProductModel;
import com.agamilabs.smartshop.ui.storeadmin.models.StoreTopCustomerModel;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreDashboardActivity extends AppCompatActivity {
    private ProgressBar cartSummaryProgressBar, topSoldProductProgressBar, topCustomerProgressBar;
    private RecyclerView cartSummaryRecyclerView, topSoldProductRecyclerView, topCustomerRecyclerView;
    private LinearLayout topProductContainerLayout, topCustomerContainerLayout;

    private StoreDashboardCartSummaryAdapter storeDashboardCartSummaryAdapter;
    private StoreDashboardTopProductsAdapter storeDashboardTopProductsAdapter;
    private StoreDashboardTopCustomersAdapter storeDashboardTopCustomersAdapter;

    private ArrayList<StoreDashboardCartSummaryModel> dashboardCartSummaryModelArrayList;
    private ArrayList<StoreProductModel> storeProductModelArrayList;
    private ArrayList<StoreTopCustomerModel> topCustomerModelArrayList;

    //private int storeNo = 1; // need to change later

//    private StoreAdminLocal storeAdminLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_dashboard);

//        storeAdminLocal = new StoreAdminLocal(this);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            mToolbar.setNavigationIcon(R.drawable.ic_action_left_arrow_white);
        }

        topProductContainerLayout = findViewById(R.id.store_dashboard_top_product_container);
        topCustomerContainerLayout = findViewById(R.id.store_dashboard_top_customer_container);

        cartSummaryProgressBar = findViewById(R.id.store_cart_summary_progressbar);
        topSoldProductProgressBar = findViewById(R.id.store_top_products_progressbar);
        topCustomerProgressBar = findViewById(R.id.store_top_customers_progressbar);

        cartSummaryRecyclerView = findViewById(R.id.store_cart_summary_recycler_view);
        topSoldProductRecyclerView = findViewById(R.id.store_top_products_recycler_view);
        topCustomerRecyclerView = findViewById(R.id.store_top_customer_recycler_view);

        cartSummaryProgressBar.setVisibility(View.VISIBLE);
        topSoldProductProgressBar.setVisibility(View.VISIBLE);
        topCustomerProgressBar.setVisibility(View.VISIBLE);

        dashboardCartSummaryModelArrayList = new ArrayList<>();
        storeProductModelArrayList = new ArrayList<>();
        topCustomerModelArrayList = new ArrayList<>();


        storeDashboardCartSummaryAdapter = new StoreDashboardCartSummaryAdapter(this, dashboardCartSummaryModelArrayList);
        cartSummaryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        cartSummaryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cartSummaryRecyclerView.setAdapter(storeDashboardCartSummaryAdapter);


        storeDashboardTopProductsAdapter = new StoreDashboardTopProductsAdapter(this, storeProductModelArrayList);
        topSoldProductRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topSoldProductRecyclerView.setItemAnimator(new DefaultItemAnimator());
        topSoldProductRecyclerView.setAdapter(storeDashboardTopProductsAdapter);


        storeDashboardTopCustomersAdapter = new StoreDashboardTopCustomersAdapter(this, topCustomerModelArrayList);
        topCustomerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topCustomerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        topCustomerRecyclerView.setAdapter(storeDashboardTopCustomersAdapter);

//        loadStoreDashboardSummaryData(storeAdminLocal.getSavedStoreNo());
    }

    private void loadStoreDashboardSummaryData(int storeNo){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppServerURL.StoreModule.StoreProduct.STORENO, storeNo+"");

        AppController.getAppController().getAppNetworkController().makeRequest(AppServerURL.StoreModule.StoreDashBoard.GET_STORE_DASHBOARD_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseStoreDashboardResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppController.getAppController().getInAppNotifier().log("Store Dashboard Request Error: ", error.toString());
                    }
                }, hashMap);
    }

    private void parseStoreDashboardResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);

            int cartSummaryProgress = 0, topSoldProductProgress = 0, topCustomerProgress = 0;

            // cart summary
            dashboardCartSummaryModelArrayList.clear();
            JSONArray cartSummaryArray = jsonObject.getJSONArray("cart_summary");

            for (int i=0; i<cartSummaryArray.length(); i++){
                JSONObject singleCartSummary = cartSummaryArray.getJSONObject(i);
                cartSummaryProgress++;
                double totalAmount = 0.0;
                String amount = singleCartSummary.getString("amount");
                if(amount==null || amount=="null"){

                }else{
                    totalAmount = Double.parseDouble(amount);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cartSummaryProgressBar.setProgress(cartSummaryProgress, true);
                }

                dashboardCartSummaryModelArrayList.add(new StoreDashboardCartSummaryModel(singleCartSummary.getInt("carts"), totalAmount, singleCartSummary.getString("summary_title")));

            }

            cartSummaryProgressBar.setVisibility(View.GONE);
            storeDashboardCartSummaryAdapter.notifyDataSetChanged();

            // top sold products
            storeProductModelArrayList.clear();
            JSONArray topSoldProductArray = jsonObject.getJSONArray("top_products");

            if(topSoldProductArray.length()==0)
                topProductContainerLayout.setVisibility(View.GONE);
            else
                topProductContainerLayout.setVisibility(View.VISIBLE);

            for (int i=0; i<topSoldProductArray.length(); i++){
                JSONObject singleProduct = topSoldProductArray.getJSONObject(i);
                topSoldProductProgress++;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    topSoldProductProgressBar.setProgress(topSoldProductProgress, true);
                }

                storeProductModelArrayList.add(new StoreProductModel(singleProduct.getInt("productno"), singleProduct.getInt("store_productno"), singleProduct.getInt("unitid"),
                        singleProduct.getDouble("rate"), singleProduct.getDouble("unitqty"), singleProduct.getString("productname"), singleProduct.getString("system_given_url"),
                        singleProduct.getString("store_given_url"), singleProduct.getString("unittextshort")));
            }

            topSoldProductProgressBar.setVisibility(View.GONE);
            storeDashboardTopProductsAdapter.notifyDataSetChanged();


            // top customers
            topCustomerModelArrayList.clear();
            JSONArray topCustomerArray = jsonObject.getJSONArray("top_customers");

            if(topCustomerArray.length()==0)
                topCustomerContainerLayout.setVisibility(View.GONE);
            else
                topCustomerContainerLayout.setVisibility(View.VISIBLE);

            for (int i=0; i<topCustomerArray.length(); i++){
                JSONObject singleCustomer = topCustomerArray.getJSONObject(i);

                topCustomerProgress++;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    topCustomerProgressBar.setProgress(topCustomerProgress, true);
                }

                topCustomerModelArrayList.add(new StoreTopCustomerModel(singleCustomer.getInt("userno"), singleCustomer.getInt("storeno"), singleCustomer.getInt("total_carts"),
                        singleCustomer.getDouble("total_amount"), singleCustomer.getString("ufirstname"), singleCustomer.getString("ulastname"), singleCustomer.getString("contactno"),
                        singleCustomer.getString("imageurl")));
            }

            topCustomerProgressBar.setVisibility(View.GONE);
            storeDashboardTopCustomersAdapter.notifyDataSetChanged();

        }catch (Exception e){
            AppController.getAppController().getInAppNotifier().log("Store Dashboard Response Parse Error: ", e.toString());
        }
    }
}