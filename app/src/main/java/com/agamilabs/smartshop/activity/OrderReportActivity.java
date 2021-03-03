package com.agamilabs.smartshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.adapter.OrderReportAdapter;
import com.agamilabs.smartshop.model.CartStatusModel;
import com.agamilabs.smartshop.model.OrderReportModel;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderReportActivity extends AppCompatActivity {
    private String ORDER_SUMMARY_URL = "http://192.168.0.105/android/AgamiLab/smart_shop/order_summary2.json";


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mOrderRecyclerView;
    private List<OrderReportModel> mOrderList;
    private OrderReportAdapter mOrderAdapter;
    private  GridLayoutManager mGridLayoutManager;
    private int testLast = 0, lengthArray=0;
    private int totalItemCount, pastVisiblesItems,  visibleItemCount, page =1, previousTotal ;
    private boolean loading = true ;
    private ProgressBar mProgressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report);

        setTitle("Order Summary Details");


        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mProgressbar = findViewById(R.id.progressbar) ;
        mOrderRecyclerView = findViewById(R.id.recycler_order_report) ;
        mOrderRecyclerView.setHasFixedSize(true);
        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));



        mOrderList = new ArrayList<>() ;
        loadProducts(0);

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.DKGRAY);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mOrderList.clear();
                loadProducts(0);
                // To keep animation for 3 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //For stopping  swipeRefresh
                        swipeRefreshLayout.setRefreshing(false);
                        mOrderAdapter.notifyDataSetChanged();
                    }
                }, 1000);


            }
        });
    }





    private void loadProducts(int number) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ORDER_SUMMARY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("TAG", "response123: "+ response) ;

                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getString("error").equalsIgnoreCase("false")){
                                JSONArray orderSummaryDataArray = object.getJSONArray("data");

                                lengthArray = orderSummaryDataArray.length() ;
                                ForLoadMoreData(orderSummaryDataArray, number) ; //initial load data


//                                for(int i=0;i<orderSummaryDataArray.length();i++)
//                                {
//                                    List<CartStatusModel> mCartStatusList = new ArrayList<>();
//
//                                    JSONObject mSummaryDataObject = orderSummaryDataArray.getJSONObject(i);
//                                    JSONArray mCartArray = mSummaryDataObject.getJSONArray("cart_status") ;
//                                    for (int j=0 ; j<mCartArray.length(); j++){
//                                        JSONObject mCartObject = mCartArray.getJSONObject(j) ;
//                                        mCartStatusList.add(new CartStatusModel(
//                                                mCartObject.getString("cstatusno"),
//                                                mCartObject.getString("statustitle"),
//                                                mCartObject.getString("icon"),
//                                                mCartObject.getString("is_end"),
//                                                mCartObject.getString("statusno"),
//                                                mCartObject.getString("passed")
//                                        )) ;
//                                    }
//                                    mOrderList.add(new OrderReportModel(
//                                            mSummaryDataObject.getString("forstreet"),
//                                            mSummaryDataObject.getString("forcity"),
//                                            mSummaryDataObject.getString("forpostcode"),
//                                            mSummaryDataObject.getString("forcontact"),
//                                            mSummaryDataObject.getString("cartdatetime"),
//                                            mSummaryDataObject.getString("delivarydatetime"),
//                                            mSummaryDataObject.getString("cartorderid"),
//                                            mSummaryDataObject.getString("ufirstname"),
//                                            mSummaryDataObject.getString("ulastname"),
//                                            mCartStatusList
//                                    ));
//                                }
                                mOrderAdapter = new OrderReportAdapter(getApplicationContext(), mOrderList);
                                mOrderRecyclerView.setAdapter(mOrderAdapter);
                                mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                mOrderRecyclerView.setLayoutManager(mGridLayoutManager);
                                mOrderAdapter.notifyDataSetChanged();
                                loadMoreData(orderSummaryDataArray);

                            }
//                            Log.e("TAG", "mOrderList: "+ mOrderList) ;
//                            Log.e("TAG", "mCartStatusList: "+ mCartStatusList) ;



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

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
    private void ForLoadMoreData(JSONArray stockArray, int number) {
        if(number==lengthArray){
            return;
        }

        for(int i=number;i<number+5;i++)
        {
            List<CartStatusModel> mCartStatusList = new ArrayList<>();
            testLast = i ;
            JSONObject mSummaryDataObject = null;
            try {
                mSummaryDataObject = stockArray.getJSONObject(i);
                JSONArray mCartArray = mSummaryDataObject.getJSONArray("cart_status") ;
                for (int j=0 ; j<mCartArray.length(); j++){
                    JSONObject mCartObject = mCartArray.getJSONObject(j) ;
                    mCartStatusList.add(new CartStatusModel(
                            mCartObject.getString("cstatusno"),
                            mCartObject.getString("statustitle"),
                            mCartObject.getString("icon"),
                            mCartObject.getString("is_end"),
                            mCartObject.getString("statusno"),
                            mCartObject.getString("passed")
                    )) ;
                }
                mOrderList.add(new OrderReportModel(
                        mSummaryDataObject.getString("forstreet"),
                        mSummaryDataObject.getString("forcity"),
                        mSummaryDataObject.getString("forpostcode"),
                        mSummaryDataObject.getString("forcontact"),
                        mSummaryDataObject.getString("cartdatetime"),
                        mSummaryDataObject.getString("delivarydatetime"),
                        mSummaryDataObject.getString("cartorderid"),
                        mSummaryDataObject.getString("ufirstname"),
                        mSummaryDataObject.getString("ulastname"),
                        mCartStatusList
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    private void loadMoreData(final JSONArray array) {
        mOrderRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0)
                {
                    visibleItemCount = mGridLayoutManager.getChildCount();
                    totalItemCount = mGridLayoutManager.getItemCount();
                    pastVisiblesItems = mGridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount-1) {
                            loading = false;
                            mProgressbar.setVisibility(View.VISIBLE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadNextDataFromApi(array);
                                }
                            }, 1000);

                        }
                    }
                }
            }
        });
    }
    public void loadNextDataFromApi(JSONArray array) {
        loading = true;
        int j = testLast+1;
        ForLoadMoreData(array, j); //  2nd --- final load data
        mProgressbar.setVisibility(View.GONE);
        mOrderAdapter.notifyDataSetChanged();
    }



}