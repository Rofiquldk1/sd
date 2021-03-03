package com.example.aidlserver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;

import SeparatePk.IRemoteServiceCallback;


public class NotifyUser extends BroadcastReceiver {
    public Context context;
    String recepientName;
    IRemoteServiceCallback iRemoteServiceCallback;
    @Override
    public void onReceive(Context context, Intent intent) {

        // Receive SmsID of the alarm to update the SMS status in database, Receive recepient name to display in notifications
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            recepientName = bundle.getString("campaignName");
            //iRemoteServiceCallback = (IRemoteServiceCallback) bundle.getString("iRemoteServiceCallback");

        }

        String messageSentStatus = "Message could not be sent.\nUnknown Error";
        setContext(context);
        // Get result code, update database and set notifications message
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                messageSentStatus = "Message has been sent.";
                updateStatus();
                //updateSmsToSent();
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                messageSentStatus = "Message could not be sent.\nGeneric Failure Error";
                updateStatus();
                //updateSmsToFailed();
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                messageSentStatus = "Message could not be sent.\nNo Service Available";
                updateStatus();
                //updateSmsToFailed();
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                messageSentStatus = "Message could not be sent.\nNull PDU";
                updateStatus();
                //updateSmsToFailed();
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                messageSentStatus = "Message could not be sent.\nRadio is off";
                updateStatus();
                //updateSmsToFailed();
                break;
            default:
                break;
        }

    }

    private void updateStatus() {

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}