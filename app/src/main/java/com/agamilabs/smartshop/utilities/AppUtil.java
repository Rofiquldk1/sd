package com.agamilabs.smartshop.utilities;

import com.agamilabs.smartshop.constants.AppServerURL;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.custom.AppJSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;

public class AppUtil {



    private static void getCategories(HashMap<String, String> map){
        AppController.getAppController().getAppNetworkController().makeRequest(AppServerURL.UserModule.Category.CATEGORY_HIERARCHY_URL_MINIFIED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppJSONObject jsonObject;
                try {
                    jsonObject = new AppJSONObject(response);
                    if (jsonObject.getBoolean("error")) {

                    } else {
                        //localDatabaseRepo.insertSettingsInfo(context, USER_LANDING_PAGE_NAVIGATION_CATEGORY, response);
                    }
                } catch (Exception e) {
                    AppController.getAppController().getInAppNotifier().log("CatJSONException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppController.getAppController().getInAppNotifier().log("ResponseJSON", error.toString());
            }
        }, map);
    }

}