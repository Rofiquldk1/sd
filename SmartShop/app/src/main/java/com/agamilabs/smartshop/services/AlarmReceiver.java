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
    private aidlInterface aidlObject,aidlObject2,aidlObject3,aidlObject4,aidlObject5;
    String[] name = {"client1", "client2", "client3", "client4", "client5"} ;

    @Override
    public void onReceive(Context context, Intent intent) {
        setContext(context);

        smsDatabaseHelper = new SmsDatabaseHelper(context);

        int count = smsDatabaseHelper.getClientExtistence() ;
        if( count == 0 ){
            for(int i=0; i<5; i++){
                smsDatabaseHelper.insertClienInfo(name[i], "free") ;
            }
        }

        SharedPreferences TriggerPref = getContext().getSharedPreferences("triggerStatus", 0);
        boolean triggerStatus = TriggerPref.getBoolean("triggerClients", false);

        if(triggerStatus == false){
            try {
                aidlObject.trigger(stubObjectR);
                aidlObject2.trigger(stubObjectR);
                aidlObject3.trigger(stubObjectR);
                aidlObject4.trigger(stubObjectR);
                aidlObject5.trigger(stubObjectR);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = TriggerPref.edit();
            editor.putBoolean("triggerClients", true);
            editor.commit();
        }

        // Receive SmsID from alarm extra
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            SmsID = bundle.getInt("SmsID");
            smsQueue.add(SmsID);
        }

        bindToAIDLService();
        bindToAIDLService2();
        bindToAIDLService3();
        bindToAIDLService4();
        bindToAIDLService5();
        //getSmsDetails();

        while (smsQueue.size() > 0){
            assignWorkToClients();
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /*// Retrieve sms details from database using SmsID from alarm
    public void getSmsDetails() {
        ArrayList<Sms> sms = smsDatabaseHelper.getSmsByID(SmsID);
        recipientNumbers = sms.get(0).recipientsNumber;
        campaignMessage = sms.get(0).message;
        campaignName = sms.get(0).campaignName;
        String replace = recipientNumbers.replace("[","");
        String replace1 = replace.replace("]","");
        phoneNumList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        sendSMStoAllContacts(campaignMessage,campaignName);
    }*/

    private void assignWorkToClients() {
        if(smsDatabaseHelper.getClientStatus(name[0]).equalsIgnoreCase("free")){
            int head = smsQueue.peek();
            try {
                aidlObject.SendSMS(head);
                smsDatabaseHelper.updateClientStatus(name[0],"busy");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            smsQueue.remove();
        } else if(smsDatabaseHelper.getClientStatus(name[1]).equalsIgnoreCase("free")){
            int head = smsQueue.peek();
            try {
                aidlObject2.SendSMS(head);
                smsDatabaseHelper.updateClientStatus(name[1],"busy");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            smsQueue.remove();
        } else if(smsDatabaseHelper.getClientStatus(name[2]).equalsIgnoreCase("free")){
            int head = smsQueue.peek();
            try {
                aidlObject3.SendSMS(head);
                smsDatabaseHelper.updateClientStatus(name[2],"busy");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            smsQueue.remove();
        } else if(smsDatabaseHelper.getClientStatus(name[3]).equalsIgnoreCase("free")){
            int head = smsQueue.peek();
            try {
                aidlObject4.SendSMS(head);
                smsDatabaseHelper.updateClientStatus(name[3],"busy");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            smsQueue.remove();
        } else if(smsDatabaseHelper.getClientStatus(name[4]).equalsIgnoreCase("free")){
            int head = smsQueue.peek();
            try {
                aidlObject5.SendSMS(head);
                smsDatabaseHelper.updateClientStatus(name[4],"busy");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            smsQueue.remove();
        } else {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(60*1000);
                    } catch (InterruptedException e) {

                    }
                    assignWorkToClients();
                }
            }.start();
        }
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

    private void bindToAIDLService5() {
        Intent aidlServiceIntent = new Intent("connect_to_aidl_service5");
        getContext().bindService(implicitIntentToExplicitIntent(aidlServiceIntent,getContext()),serviceConnectionObject5,BIND_AUTO_CREATE);
    }

    private void bindToAIDLService4() {
        Intent aidlServiceIntent = new Intent("connect_to_aidl_service4");
        getContext().bindService(implicitIntentToExplicitIntent(aidlServiceIntent,getContext()),serviceConnectionObject4,BIND_AUTO_CREATE);
    }

    private void bindToAIDLService3() {
        Intent aidlServiceIntent = new Intent("connect_to_aidl_service3");
        getContext().bindService(implicitIntentToExplicitIntent(aidlServiceIntent,getContext()),serviceConnectionObject3,BIND_AUTO_CREATE);
    }

    private void bindToAIDLService2() {
        Intent aidlServiceIntent = new Intent("connect_to_aidl_service2");
        getContext().bindService(implicitIntentToExplicitIntent(aidlServiceIntent,getContext()),serviceConnectionObject2,BIND_AUTO_CREATE);
    }

    private void bindToAIDLService() {
        Intent aidlServiceIntent = new Intent("connect_to_aidl_service");
        getContext().bindService(implicitIntentToExplicitIntent(aidlServiceIntent,getContext()),serviceConnectionObject,BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnectionObject = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            aidlObject = aidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    ServiceConnection serviceConnectionObject2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            aidlObject2 = aidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    ServiceConnection serviceConnectionObject3 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            aidlObject3 = aidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    ServiceConnection serviceConnectionObject4 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            aidlObject4 = aidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    ServiceConnection serviceConnectionObject5 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            aidlObject5 = aidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    public Intent implicitIntentToExplicitIntent(Intent implicitIntent, Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(implicitIntent, 0);

        if (resolveInfoList == null || resolveInfoList.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfoList.get(0);
        ComponentName component = new ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }


    IRemoteServiceCallback stubObjectR = new IRemoteServiceCallback.Stub() {

        @Override
        public void feedBack(String msg) throws RemoteException {
            switch (msg){
                case "freeClient1":
                    smsDatabaseHelper.updateClientStatus(name[0],"free");
                    break;
                case "freeClient2":
                    smsDatabaseHelper.updateClientStatus(name[1],"free");
                    break;
                case "freeClient3":
                    smsDatabaseHelper.updateClientStatus(name[2],"free");
                    break;
                case "freeClient4":
                    smsDatabaseHelper.updateClientStatus(name[3],"free");
                    break;
                case "freeClient5":
                    smsDatabaseHelper.updateClientStatus(name[4],"free");
                    break;
            }
        }

        @Override
        public void sentStatus(String message) throws RemoteException {
            //Log.d("260",message);
            //Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    };



}

