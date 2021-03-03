package com.agamilabs.smartshop.controller;

import android.content.Context;
import android.text.TextUtils;

import com.agamilabs.smartshop.constants.AppConstants;
import com.agamilabs.smartshop.database.MySharedPreferenceManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sazzad on 6/27/2018.
 */

public class AppNetworkController {

    private static volatile AppNetworkController sSoleInstance;
    private RequestQueue mRequestQueue;
    private Context context;

    //private constructor.
    private AppNetworkController(Context context) {

        //Prevent form the reflection api.
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.context = context;

        mRequestQueue = getRequestQueue(context);
    }

    private RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        this.context = context;

        return mRequestQueue;
    }

    static AppNetworkController getInstance(Context context) {
        if (sSoleInstance == null) { //if there is no instance available... create new one
            synchronized (AppNetworkController.class) {
                if (sSoleInstance == null) sSoleInstance = new AppNetworkController(context);
            }
        }

        return sSoleInstance;
    }

    private <T> boolean addToRequestQueue(Request<T> req) {

        if (mRequestQueue == null) {
            return false;
        }

        mRequestQueue.add(req);

        return true;
    }

    private static final String DEFAULT_TAG = "REQUEST";

    private StringRequest createRequest(String url, final Response.Listener<String> success, final Response.ErrorListener error, final HashMap<String, String> map, String tag) {
        AppController.getAppController().getInAppNotifier().log("payload before", map.toString());
        MySharedPreferenceManager mySharedPreferenceManager = new MySharedPreferenceManager(context);
        map.put(AppConstants.USER_NO, mySharedPreferenceManager.getUserNo());
        map.put(AppConstants.USER_LATITUDE, mySharedPreferenceManager.getUserLat());
        map.put(AppConstants.USER_LONGITUDE, mySharedPreferenceManager.getUserLon());
        //map.put(AppConstants.FCM_TOKEN, mySharedPreferenceManager.getFCMToken(mySharedPreferenceManager.getUserNo()));
        map.put(AppConstants.FCM_TOKEN, mySharedPreferenceManager.getFCMToken(mySharedPreferenceManager.getUserNo()));
        AppController.getAppController().getInAppNotifier().log("payload after", map.toString());
        StringRequest request = new StringRequest( Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (success != null) {
                    success.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                if (error != null) {
                    error.onErrorResponse(e);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };
        request.setTag(TextUtils.isEmpty(tag) ? DEFAULT_TAG : tag);
        return request;
    }

    public boolean makeRequest(final String url, Response.Listener<String> success, Response.ErrorListener error, HashMap<String, String> map) {
        return addToRequestQueue(createRequest(url, success, error, map, ""));
    }

    public boolean makeRequest(final String url, Response.Listener<String> success, Response.ErrorListener error, HashMap<String, String> map, String tag) {
        return addToRequestQueue(createRequest(url, success, error, map, tag));
    }


}