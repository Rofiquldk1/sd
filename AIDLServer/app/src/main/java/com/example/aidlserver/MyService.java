package com.example.aidlserver;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import SeparatePk.IRemoteServiceCallback;
import SeparatePk.aidlInterface;
import SeparatePk.IRemoteServiceCallback;
import SeparatePk.IRemoteServiceCallback;

public class MyService extends Service {
    IRemoteServiceCallback iRemoteServiceCallback2;
    private SmsDatabaseHelper smsDatabaseHelper;
    private int SmsID;
    private List<String> phoneNumList;
    private String recipientNumbers,campaignMessage,campaignName;

    public MyService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stubObject;
    }

    aidlInterface.Stub stubObject = new aidlInterface.Stub() {
        @Override
        public void trigger(IRemoteServiceCallback iRemoteServiceCallback) throws RemoteException {
            iRemoteServiceCallback2 = iRemoteServiceCallback;
        }

        @Override
        public void SendSMS(int smsId) throws RemoteException {
            getSmsDetails(smsId);
        }
    };

    public void getSmsDetails(int SmsID) {
        ArrayList<Sms> sms = smsDatabaseHelper.getSmsByID(SmsID);
        recipientNumbers = sms.get(0).recipientsNumber;
        campaignMessage = sms.get(0).message;
        campaignName = sms.get(0).campaignName;
        String replace = recipientNumbers.replace("[","");
        String replace1 = replace.replace("]","");
        phoneNumList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        sendSMStoAllContacts(campaignMessage,campaignName);
    }

    private void sendSMStoAllContacts(String message, String name) {
        for (int i = 0; i < phoneNumList.size(); i++)
        {
            String tempMobileNumber = phoneNumList.get(i).trim();
            sendSMSandNotifyUser(tempMobileNumber, message,name,i+1);
        }

        try {
            iRemoteServiceCallback2.feedBack("freeClient1");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendSMSandNotifyUser(String phoneNumber, String message, String name,int sentMessageSLNo) {
        SmsManager sms = SmsManager.getDefault();
        // If message is too long for single sms split into multiple messages
        List<String> messages = sms.divideMessage(message);
        for (String msg : messages) {
            Intent intent = new Intent(getApplicationContext(), NotifyUser.class);

            // Pass recipient name and SmsID
            intent.putExtra("name", name);
            intent.putExtra("SmsID", SmsID);
            intent.putExtra("scheduleType", "exactTime");
            intent.putExtra("sentMessageSLNo", sentMessageSLNo);
            intent.putExtra("totalRecipients", phoneNumList.size());

            // Broadcast receiver to listen for the sentstatus of the message.
            PendingIntent sentIntent = PendingIntent.getBroadcast(getApplicationContext(), SmsID, intent, 0);
            sms.sendTextMessage(phoneNumber, null, msg, sentIntent, null);

        }
    }


}