package com.example.aidlserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static MainActivity INSTANCE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        INSTANCE = this;
    }

    public static MainActivity getActivityInstance()
    {
        return INSTANCE;
    }

    public void SendSMS(String phoneNumber, String message)  {
        Log.d("48",phoneNumber);
        Log.d("48","c2");
        SmsManager sms = SmsManager.getDefault();
        Log.d("48","c3");
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Log.d("48","c4");

        //iRemoteServiceCallback2.feedBack("freeClient1");
        //iRemoteServiceCallback2.sentStatus("Message Sent Successfully");
    }


}