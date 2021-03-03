package com.agamilabs.smartshop.activity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.adapter.StockReportAdapter;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.model.StockReportModel;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StockReportActivity extends AppCompatActivity {

    private String STOCK_URL = "http://192.168.0.105/android/AgamiLab/smart_shop/StockReport.php";
    private String CATEGORY_URL = "http://192.168.0.105/android/AgamiLab/smart_shop/category.php";
//    private String STOCK_URL = "http://192.168.1.3/android/AgamiLab/agami-logbook/view_section.php";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NestedScrollView mNestedScroll ;
    private RecyclerView mStockRecyclerView;
    private TextInputEditText mProductEdit;
    private TextView mDateFrom, mDateTo;
    private Button  mFilterBtn ;
    private TextView mCategoryTextView;
    private Spinner mReorderSpin ;
    private ImageButton mImgBtnDF, mImgBtnDT;
    private RelativeLayout mRelativeLayout;
    private ShimmerFrameLayout shimmerFrameLayout;


    private List<StockReportModel> mStockList;
    private List<String> mCategoryList  ;

    private StockReportAdapter mStockAdapter ;


    private Calendar calendar;
    private int year, month, day;

    //popup dialog
    private TextInputEditText mSearchEditext ;
    ListView mSearchListView ;

    private AlertDialog.Builder dialogBuilder  ;
    private AlertDialog dialog;

    private int pageNumber = 1, lengthArray=0;
    private ProgressBar mProgressbar;

    private ArrayAdapter adapter;


    private CountDownTimer countDownTimer;
    long remainingRefreshTime = 1000 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_report);
        setTitle("Stock Report");

        initialize() ;

        dialogBuilder = new AlertDialog.Builder(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener myDateListener = null;
        mImgBtnDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);

            }
        });
        mImgBtnDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);

            }
        });
        mCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupDialog();
            }
        });

        mStockList = new ArrayList<>() ;
        mCategoryList = new ArrayList<>() ;
        initializeAdapter();

        //loadCategoryList(false);
        loadStockReportList(pageNumber);
        //loadNextPageList();






        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.DKGRAY);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAutoRefresh();
            }
        });
        //search product
        SearchProductText();

        mFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadProductsList("","", "", "", "",1);
                AppController.getAppController().getInAppNotifier().showToast("plz add php");
            }
        });

    }

    private void setAutoRefresh(){
        //if already countdowntime nul na hole, countdowntimer k stop korbe.
        //max refresh time 0 or 0 theke chotu hoi, uporer kaj ta korbe..
        if(remainingRefreshTime<=0){
            if(countDownTimer!= null){
                countDownTimer.cancel();
            }
            return;

        }
        if(countDownTimer == null){
            countDownTimer = new CountDownTimer(remainingRefreshTime, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mStockList.clear();
                    mCategoryList.clear();
                    mProductEdit.setText("");
                    pageNumber=1;

                    //shimmer effect
                    mRelativeLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.startShimmer();
                }

                @Override
                public void onFinish() {
                    initializeAdapter();
                    loadStockReportList(pageNumber);
                    //loadNextPageList();
                    loadCategoryList(false);
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();

                    mSwipeRefreshLayout.setRefreshing(false);
                    cancelAutoRefresh() ;

                }
            };

            countDownTimer.start() ;
        }
    }
    private void cancelAutoRefresh(){
        if(countDownTimer!= null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
    }


    private void loadStockReportList(int number) {
        pageNumber=Math.max(number, 1) ;
        HashMap<String, String> map = new HashMap<>() ;
        map.put("pageNumber", pageNumber+"") ;
        AppController.getAppController().getAppNetworkController().makeRequest(STOCK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppController.getAppController().getInAppNotifier().log("response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("error").equalsIgnoreCase("false")){
                        JSONArray stockArray = object.getJSONArray("report");

                        mRelativeLayout.setVisibility(View.VISIBLE);
                        lengthArray = stockArray.length() ;
                        ForLoadData(stockArray);
                        mStockAdapter.setPageNumber(pageNumber);
                        mStockAdapter.notifyDataSetChanged();

                    }
                    else{
                        mRelativeLayout.setVisibility(View.GONE);
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
    private void ForLoadData(JSONArray stockArray) {
        for(int i=0; i<stockArray.length(); i++){
            JSONObject mStockObject = null;
            try {
                mStockObject = stockArray.getJSONObject(i);
                StockReportModel aStockModel = new StockReportModel() ;
                Field[] fields =  aStockModel.getAllFields() ;

                for(int j=0; j<fields.length; j++ ){
                    String fieldName = fields[j].getName() ;
                    String fieldValueInJson =mStockObject.has(fieldName)? mStockObject.getString(fieldName) : "" ;
                    try{
                        fields[j].set(aStockModel, fieldValueInJson) ;
                    }catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                mStockList.add(aStockModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        AppController.getAppController().getInAppNotifier().log("response", mStockList.size()+"");
    }
  /*  private void loadNextPageList() {
        if (mNestedScroll != null ) {
            mNestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    pageNumber  = pageNumber + 1 ;
//                    loadStockReportList(pageNumber);
//                    AppController.getAppController().getInAppNotifier().log("page", "   pageNumber "+ pageNumber) ;
//                    AppController.getAppController().getInAppNotifier().showToast("   pageNumber "+ pageNumber); ;
                }
            });
        }


    }

*/


    private void loadCategoryList(boolean dialogClick) {
        HashMap<String, String> map = new HashMap<>() ;
        AppController.getAppController().getAppNetworkController().makeRequest(CATEGORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray mStockCategoryArray = object.getJSONArray("category");

                    for(int i=0;i<mStockCategoryArray.length();i++){
                        JSONObject mCategoryObject = mStockCategoryArray.getJSONObject(i);
                        mCategoryList.add(mCategoryObject.getString("itemname")) ;

                    }
                    if(dialogClick==true){
                        adapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, mCategoryList);
                        mSearchListView.setAdapter(adapter);
                    }

                    else{
                        mCategoryTextView.setText(mCategoryList.get(0));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);

//        StringRequest stringRequest = new StringRequest(Request.Method.GET, CATEGORY_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
////        {
////            @Override
////            protected Map<String, String> getParams() throws AuthFailureError {
////                Map<String, String> parameters = new HashMap<String, String>();
////                parameters.put("item_name",  item_name );
////                parameters.put("category_name",  category_name );
////                parameters.put("reorder_point",  reorder_point );
////                parameters.put("date_from",  date_from );
////                parameters.put("date_to",  date_to );
////                return parameters;
////            }
////
////        };
//
//
//        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
    private void initializeAdapter(){
        mStockRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        mStockAdapter = new StockReportAdapter(getApplicationContext(), mStockList){
            @Override
            public void loadNextPage(int pageNumber) {
                loadStockReportList(pageNumber);
                AppController.getAppController().getInAppNotifier().showToast("ReqPageNumber"+ (pageNumber)+"");
                AppController.getAppController().getInAppNotifier().log("ReqPageNumber", mStockList.size()+" size");
            }
        };
        mStockRecyclerView.setAdapter(mStockAdapter);
    }







    //popup
    private void createPopupDialog()
    {
        View view = getLayoutInflater().inflate(R.layout.layout_popup, null);
        mSearchEditext = view.findViewById(R.id.inputedit_popup_category) ;
        mSearchListView = view.findViewById(R.id.listview_popup);



        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        loadCategoryList(true) ;

        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCategoryTextView.setText((String) parent.getItemAtPosition(position));
                dialog.cancel();
            }
        });

        mSearchEditext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    //=======     for time
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            return new DatePickerDialog(this,
                    myDateListener1, year, month, day);
        }else if (id == 2) {
            return new DatePickerDialog(this,
                    myDateListener2, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener1 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(mDateFrom,arg1, arg2+1, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(mDateTo,arg1, arg2+1, arg3);
                }
            };
    private void showDate(TextView textView, int year, int month, int day) {
        textView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    //   initialize
    private void initialize() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mNestedScroll = findViewById(R.id.nested_scroll_stock);
        mStockRecyclerView = findViewById(R.id.recycler_stock_report) ;
        mProductEdit = findViewById(R.id.edit_product_name);
        mCategoryTextView = findViewById(R.id.text_category);
        mReorderSpin = findViewById(R.id.spinner_reorder_point);
        mDateFrom = findViewById(R.id.text_date_from);
        mDateTo = findViewById(R.id.text_date_to);
        mFilterBtn = findViewById(R.id.btn_filter);
        mImgBtnDF = findViewById(R.id.imagebtn_date_from);
        mImgBtnDT = findViewById(R.id.imagebtn_date_to);
        mProgressbar = findViewById(R.id.progressbar) ;
        //mRelativeLayout = findViewById(R.id.relative_stock_report);
        //shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
    }
    public void SearchProductText(){
        mProductEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchNameFilter(s.toString());

            }
        });
    }
    private void searchNameFilter(String text) {
        List<StockReportModel> filteredList = new ArrayList<>();

        for (StockReportModel item : mStockList) {

            if (item.getItemname().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
            /*if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }*/
        }

        mStockAdapter.searchFilterList(filteredList);
    }



    private void loadProductsList(String item_name, String category_name, String reorder_point, String date_from, String date_to, int number) {


        pageNumber=Math.max(number, 1) ;
        HashMap<String, String> map = new HashMap<>() ;
        AppController.getAppController().getAppNetworkController().makeRequest(STOCK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppController.getAppController().getInAppNotifier().log("response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("error").equalsIgnoreCase("false")){
                        JSONArray stockArray = object.getJSONArray("report");

                        mRelativeLayout.setVisibility(View.VISIBLE);
                        lengthArray = stockArray.length() ;
//                        ForLoadMoreData(stockArray, pageNumber) ; //initial load data

                        mStockAdapter.notifyDataSetChanged();
//                        loadNextPageStat(stockArray);

                        AppController.getAppController().getInAppNotifier().log("response", "response inside");
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
    private void ForLoadMoreData(JSONArray stockArray, int number) {

        AppController.getAppController().getInAppNotifier().log("page", number+"   number") ;
        if(number==lengthArray){
            return;
        }
        for(int i=number; i<number+ 5; i++){
            JSONObject mStockObject = null;
            try {
                mStockObject = stockArray.getJSONObject(i);
                StockReportModel aStockModel = new StockReportModel() ;
                Field[] fields =  aStockModel.getAllFields() ;

                for(int j=0; j<fields.length; j++ ){
                    String fieldName = fields[j].getName() ;
                    String fieldValueInJson =mStockObject.has(fieldName)? mStockObject.getString(fieldName) : "" ;
                    try{
                        fields[j].set(aStockModel, fieldValueInJson) ;
                    }catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                mStockList.add(aStockModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    private void loadNextPageStat(JSONArray jsonArray) {
        if (mNestedScroll != null ) {
            mNestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    int j  = pageNumber + 1 ;
//                    ForLoadMoreData(jsonArray, j);
//                    mProgressbar.setVisibility(View.GONE);
//                    mStockAdapter.notifyDataSetChanged();
                    AppController.getAppController().getInAppNotifier().log("page", pageNumber+"   pageNumber "+ j) ;
                }


            });
        }


    }
}