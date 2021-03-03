package com.agamilabs.smartshop.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agamilabs.smartshop.activity.NewCampaignActivity;
import com.agamilabs.smartshop.database.SmsDatabaseHelper;
import com.agamilabs.smartshop.model.Sms;
import com.agamilabs.smartshop.model.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import SeparatePk.IRemoteServiceCallback;
import SeparatePk.aidlInterface;

import static android.content.Context.BIND_AUTO_CREATE;

public class AlarmReceiver extends BroadcastReceiver {
    public Context context;
    private SmsDatabaseHelper smsDatabaseHelper;
    private int SmsID;
    private List<String> phoneNumList;
    private String recipientNumbers,campaignMessage,campaignName;
    private Queue<Integer> smsQueue = new LinkedList<>();
    String[] name = {"client1", "client2", "client3", "client4", "client5"} ;


    @Override
    public void onReceive(Context context, Intent intent) {
        setContext(context);

        //Log.d("58","called");
        smsDatabaseHelper = new SmsDatabaseHelper(getContext());

        // Receive SmsID from alarm extra
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            SmsID = bundle.getInt("SmsID");
            smsQueue.add(SmsID);
        }

        while (smsQueue.size() > 0){
            int head = smsQueue.peek();
            NewCampaignActivity.getActivityInstance().assignWorkToClients(head,"outside");
            smsQueue.remove();
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    // Retrieve sms details from database using SmsID from alarm
    public void getSmsDetails(int sms_id) {
        ArrayList<Sms> sms = smsDatabaseHelper.getSmsByID(sms_id);
        recipientNumbers = sms.get(0).recipientsNumber;
        campaignMessage = sms.get(0).message;
        campaignName = sms.get(0).campaignName;
        /*String replace = recipientNumbers.replace("[","");
        String replace1 = replace.replace("]","");
        phoneNumList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        sendSMStoAllContacts(campaignMessage,campaignName);*/
    }

    private void sendSMStoAllContacts(String message, String name) {
        for (int i = 0; i < phoneNumList.size(); i++)
        {
            String tempMobileNumber = phoneNumList.get(i).trim();
            sendSMSandNotifyUser(tempMobileNumber, message,name,i+1);
        }

    }

    // Send sms and Notify user
    public void sendSMSandNotifyUser(String phoneNumber, String message, String name,int sentMessageSLNo) {
        SmsManager sms = SmsManager.getDefault();
        // If message is too long for single sms split into multiple messages
        List<String> messages = sms.divideMessage(message);
        for (String msg : messages) {
            Intent intent = new Intent(getContext(), NotifyUser.class);

            // Pass recipient name and SmsID
            intent.putExtra("name", name);
            intent.putExtra("SmsID", SmsID);
            intent.putExtra("scheduleType", "exactTime");
            intent.putExtra("sentMessageSLNo", sentMessageSLNo);
            intent.putExtra("totalRecipients", phoneNumList.size());

            new SmsDatabaseHelper(context).
                    addTestInfo(new Test(
                            SmsID+"",
                            phoneNumber+"",
                            message+"",
                            "Calender Time Before Sent Msg "+Calendar.getInstance().getTime()
                    ));

            // Broadcast receiver to listen for the sentstatus of the message.
            PendingIntent sentIntent = PendingIntent.getBroadcast(getContext(), SmsID, intent, 0);
            sms.sendTextMessage(phoneNumber, null, msg, sentIntent, null);

            new SmsDatabaseHelper(context).
                    addTestInfo(new Test(
                            SmsID+"",
                            phoneNumber+"",
                            message+"",
                            "Calender Time After Sent Msg "+Calendar.getInstance().getTime()
                    ));

        }
    }





}

