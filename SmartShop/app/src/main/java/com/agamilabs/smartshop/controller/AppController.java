package com.agamilabs.smartshop.controller;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import androidx.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by sazzad on 6/27/2018.
 */

public class AppController extends MultiDexApplication {

    private static volatile AppController appController;
    private InAppNotifier inAppNotifier;
    private AppNetworkController appNetworkController;

    //private constructor.
    public AppController(){

        //Prevent form the reflection api.
        if (appController != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();

        appController = this;

        //FacebookSdk.sdkInitialize(getApplicationContext());

        getKeyHash();

    }

    private void getKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.agamilabs.betikrom",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                AppController.getAppController().getInAppNotifier().log("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

//            AppController.getAppController().getInAppNotifier().log("KeyHash:", info.);
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            AppController.getAppController().getInAppNotifier().log("KeyHashException", e.toString());
        }
    }

    public static AppController getAppController(){
        if(appController==null){
            synchronized (AppController.class){
                if(appController==null) appController = new AppController();
            }
        }


        return appController;
    }

    public InAppNotifier getInAppNotifier(){
        if(inAppNotifier==null){
            inAppNotifier = InAppNotifier.getInstance(getApplicationContext());
        }

        return inAppNotifier;
    }

    public AppNetworkController getAppNetworkController() {
        if(appNetworkController==null){
            appNetworkController = AppNetworkController.getInstance(getApplicationContext());
        }

        return appNetworkController;
    }
}