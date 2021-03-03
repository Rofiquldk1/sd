package com.agamilabs.smartshop.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.agamilabs.smartshop.LoginActivity;
import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.ShopAdminHome;
import com.agamilabs.smartshop.activity.NewCampaignActivity;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.model.CampaignItem;
import com.agamilabs.smartshop.model.NotifyModel;
import com.agamilabs.smartshop.model.ScheduleItem;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationReceiver extends FirebaseMessagingService implements Response.Listener<String>, Response.ErrorListener {
    String TAG = "TAG";
    //DatabaseHandler db = new DatabaseHandler(this);
    private String url = "url.php";
    private String campaignName, recipients, campaignMessage,campaignStartDateTime,
            notificationOn,notifyTimeBeforeMesaging,apiType,saveAsTemplate;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            /*Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> map = remoteMessage.getData();

            //data save in db
            NotifyModel notifyModel = new NotifyModel(
                    map.containsKey("id")?map.get("id"):"",
                    map.containsKey("title")?map.get("title"):"",
                    map.containsKey("message")?map.get("message"):"",
                    remoteMessage.getTo()
            );
            //db.insertUserInfo(notifyModel.getTitle(), notifyModel.getBody_text(), notifyModel.getTopic());

            createNotification(notifyModel);*/
            AppController.getAppController().getAppNetworkController().makeRequest(
                    url, NotificationReceiver.this, NotificationReceiver.this, new HashMap<>());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //data save in db
            NotifyModel notifyModel = new NotifyModel(
                    "",
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(),
                    remoteMessage.getTo()
            );
            //db.insertUserInfo(notifyModel.getTitle(), notifyModel.getBody_text(), notifyModel.getTopic());

            createNotification(notifyModel);

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void scheduleJob() {

    }

    public void handleNow() {

    }

    public void createNotification(NotifyModel notifyModel) {

        String NOTIFICATION_CHANNEL_ID = "AGAMi_Smart_Shop";

        long pattern[] = {0, 1000, 500, 1000};

        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.canBypassDnd();
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
//                .setContentTitle(getString(R.string.app_name))
                .setContentTitle(notifyModel.getTitle() + " - " + getString(R.string.app_name))
//                .setContentText(remoteMessage.getNotification().getBody())
                .setContentText(notifyModel.getBody_text())
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true);

        mNotificationManager.notify(1000, notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            if (object.has("error") && !object.getBoolean("error")) {
                JSONArray jsonArray = object.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    campaignName = jsonObject.has("campaignName") ? jsonObject.getString("campaignName") : "";
                    recipients = jsonObject.has("recipients") ? jsonObject.getString("recipients") : "";
                    campaignMessage = jsonObject.has("campaignMessage") ? jsonObject.getString("campaignMessage") : "";
                    campaignStartDateTime = jsonObject.has("campaignStartDateTime") ? jsonObject.getString("campaignStartDateTime") : "";
                    notificationOn = jsonObject.has("notificationOn") ? jsonObject.getString("notificationOn") : "";
                    notifyTimeBeforeMesaging = jsonObject.has("notifyTimeBeforeMesaging") ? jsonObject.getString("notifyTimeBeforeMesaging") : "";
                    apiType = jsonObject.has("apiType") ? jsonObject.getString("apiType") : "";

                    if(apiType.equalsIgnoreCase("Device API")){
                        if (ContextCompat.checkSelfPermission(NotificationReceiver.this,
                                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            NewCampaignActivity.getActivityInstance().generateNewSchedule(campaignName,recipients,campaignMessage,
                                    campaignStartDateTime,notificationOn,notifyTimeBeforeMesaging,apiType);
                        }
                    }

                }
            } else {
                Toast.makeText(this, object.getString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }
    }
}
