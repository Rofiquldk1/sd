package com.agamilabs.smartshop.utilities;

import android.content.Context;
import android.net.Uri;


import com.agamilabs.smartshop.constants.AppServerURL;
import com.agamilabs.smartshop.controller.AppController;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Utility {
    private static final long DAY_IN_MILLIS = 86400000;

    public static String getFormattedTime(String targetDateTime) {
        long target = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date parsedDate = simpleDateFormat.parse(targetDateTime);

            if (parsedDate == null) {
                throw new Exception("Unable to parse date");
            }

            target = parsedDate.getTime();

            Calendar current = Calendar.getInstance();
            long timeDiff = current.getTimeInMillis() - target;
            if (timeDiff >= (DAY_IN_MILLIS)) { // greater than or equal 1 day

                return ((int) (timeDiff / DAY_IN_MILLIS)) + "d ago";

            } else { // less than 1 day

                //SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
                simpleDateFormat.applyPattern("hh:mm:ss a");
                return simpleDateFormat.format(target);
            }

        } catch (Exception e) {
            return targetDateTime;
        }

    }

    public static String getFormattedTime(Calendar target) {
        Calendar current = Calendar.getInstance();
        long timeDiff = current.getTimeInMillis() - target.getTimeInMillis();
        if (timeDiff >= (DAY_IN_MILLIS)) { // greater than or equal 1 day

            return ((int) (timeDiff / DAY_IN_MILLIS)) + "d ago";

        } else { // less than 1 day

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);

            return sdf.format(target.getTime());
        }
    }









}
