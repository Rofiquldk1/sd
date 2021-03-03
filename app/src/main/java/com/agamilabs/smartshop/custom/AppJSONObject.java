package com.agamilabs.smartshop.custom;

import androidx.annotation.NonNull;

import com.agamilabs.smartshop.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class AppJSONObject extends JSONObject {
    public AppJSONObject(String res) throws JSONException {
        super(res);
    }

    @NonNull
    @Override
    public String getString(@NonNull String name) throws JSONException {
        //AppController.getAppController().getInAppNotifier().log("checking json", name);
        if (!super.has(name) || super.isNull(name)) {
            return "";
        }
        return super.getString(name);
    }

    @Override
    public int getInt(@NonNull String name) throws JSONException {
        if (!super.has(name) || super.isNull(name)) {
            return 0;
        }
        return super.getInt(name);
    }

    @Override
    public double getDouble(@NonNull String name) throws JSONException {
        if (!super.has(name) || super.isNull(name)) {
            return 0.0d;
        }
        return super.getDouble(name);
    }

}